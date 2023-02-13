package space.nixus.phoneduck.components;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import space.nixus.phoneduck.config.ApplicationConfig;
import space.nixus.phoneduck.error.ChannelNotFoundException;
import space.nixus.phoneduck.model.Message;
import space.nixus.phoneduck.model.SessionWrapper;
import space.nixus.phoneduck.service.ChannelService;
import space.nixus.phoneduck.service.UserService;
import space.nixus.phoneduck.utils.AuthHelper;

/**
 * Handle channel connection.
 */
@Component
public class ChannelHandler extends TextWebSocketHandler {
    // regex
    private static final String RE_PATTERN = "^MESSAGE\\s+(\\d+)\\s+(.+)$";
    private static final int RE_GRP_CH = 1;
    private static final int RE_GRP_BODY = 2;

    @Autowired
    private ChannelService channelService;
    @Autowired
    private UserService userService;
    // map of sessions
    private Map<String, SessionWrapper> sessions;
    // message format parser
    private final Pattern messageRegex;
    // json converter
    private final ObjectMapper mapper;

    /**
     * Constructor
     */
    public ChannelHandler() {
        mapper = new ObjectMapper();
        messageRegex = Pattern.compile(RE_PATTERN, Pattern.DOTALL);
        sessions = new HashMap<>();
    }

    /**
     * Handle new session.
     * Create a wrapper and add it to the session map.
     * Also lists currently active channels.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // check authorization
        var user = AuthHelper.checkAuth(userService, session);
        // get requested subscriptions
        var subscriptionString = session.getHandshakeHeaders()
            .getFirst(ApplicationConfig.SUBSCRIPTION_HEADER);
        // handle subscriptions
        if(subscriptionString != null) {
            var subListStringList = subscriptionString.trim().split("\\s+");
            // create wrapper
            var wrapper = new SessionWrapper(session, user);
            try {
                // parse and add subscription ids
                for(var stringId : subListStringList) {
                    wrapper.addSubscription(Long.parseLong(stringId));
                }
                if(wrapper.getSubscriptions().size() > 0) {
                    //store session
                    sessions.put(session.getId(), wrapper);
                    // send messages
                    dumpMessages(wrapper);
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // error or no subscriptions
        session.close();
    }

    /**
     * Remove session.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        sessions.remove(session.getId());
    }

    /**
     * Handle incoming messages.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        var match = messageRegex.matcher(message.getPayload());
        if(match.matches()) {
            var wrapper = sessions.get(session.getId());
            var channelId = Long.parseLong(match.group(RE_GRP_CH)); // get channel
            if(wrapper.getSubscriptions().contains(channelId)) {
                // create message
                var messageEntity = channelService.createMessages(
                    channelId, // channel
                    wrapper.getUser().getName(), // sender
                    match.group(RE_GRP_BODY) // body
                    );
                broadcastMessage(messageEntity);
            }
        }
    }
 

    /**
     * Broadcast message.
     * Ignore old notifications.
     * @param message
     * @throws IOException
     */
    public void broadcastMessage(Message message) throws IOException, JsonProcessingException, ChannelNotFoundException {
        var channelId = message.getChannelId(); // get channel
        // message to send as json
        var payload = new TextMessage(mapper.writeValueAsString(message));
        // iterate all connections
        for (var target : sessions.values()) {
            // send if subscribed to channel
            if(target.getSubscriptions().contains(channelId)) {
                target.getSession().sendMessage(payload);
            }
        }
    }

    /**
     * Dump all messages to socket.
     * 
     * @param wrapper session wrapper
     * @throws IOException
     */
    public void dumpMessages(SessionWrapper wrapper) throws IOException, JsonProcessingException {
        // iterate subscriptions
        for(var channelId : wrapper.getSubscriptions()) {
            // get messages from channel
            var messages = channelService.getMessagesByChannelId(channelId);
            // iterate messages
            for(var message : messages) {
                // send message
                var payload = new TextMessage(mapper.writeValueAsString(message));
                wrapper.getSession().sendMessage(payload);
            }
        }
    }

    /**
     * Remove subscriptions for channel.
     * 
     * @param channelId
     */
    public void channelRemoved(long channelId) {
        for(var key : sessions.keySet()) {
            var wrapper = sessions.get(key);
            wrapper.removeSubscription(channelId);
            if(!wrapper.hasSubscription()) {
                try {
                    wrapper.getSession().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
