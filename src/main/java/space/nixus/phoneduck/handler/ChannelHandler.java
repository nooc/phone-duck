package space.nixus.phoneduck.handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import space.nixus.phoneduck.config.ApplicationConfig;
import space.nixus.phoneduck.error.ChannelNotFoundException;
import space.nixus.phoneduck.model.Message;
import space.nixus.phoneduck.model.PhoneduckUser;
import space.nixus.phoneduck.repository.ChannelRepository;
import space.nixus.phoneduck.service.ChannelService;
import space.nixus.phoneduck.service.UserService;
import space.nixus.phoneduck.utils.AuthHelper;

/**
 * Handle channel connection.
 */
@Component
public class ChannelHandler extends TextWebSocketHandler {

    @AllArgsConstructor
    class Wrapper {
        WebSocketSession session;
        List<Long> subscriptions;
        PhoneduckUser user;
    }

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private UserService userService;

    private final Map<String, Wrapper> sessions;
    private final ObjectMapper mapper;

    public ChannelHandler() {
        sessions = new HashMap<>();
        mapper = new ObjectMapper(); 
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var headers = session.getHandshakeHeaders();
        var user = AuthHelper.checkAuth(userService, headers.getFirst(ApplicationConfig.AUTH_HEADER));
        var subscriptionString = headers.getFirst(ApplicationConfig.SUBSCRIPTION_HEADER);
        var wrapper = new Wrapper(session, new ArrayList<>(), user);

        try {
            if(subscriptionString != null) {
                var subListStringList = subscriptionString.trim().split("\\s+");
                for(var stringId : subListStringList) {
                    wrapper.subscriptions.add(Long.parseLong(stringId));
                }
                if(wrapper.subscriptions.size() > 0) {
                    sessions.put(session.getId(), wrapper);
                    dumpMessages(wrapper);
                    return;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        sessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        var wrapper = sessions.get(session.getId());
        var command = message.getPayload().split("\\s+", 3);
        // command: MESSAGE <channel> BODY...
        if(command[0].equals("MESSAGE") && command.length == 3) {
            var channelId = Long.parseLong(command[1]);
            channelService.createMessages(channelId, wrapper.user.getDisplayName(), command[2]);
        }
    }
 

    /**
     * Broadcast message.
     * Ignore old notifications.
     * @param notifications
     * @throws IOException
     */
    public void broadcastMessage(Message message) throws IOException, JsonProcessingException, ChannelNotFoundException {
        var channelId = message.getChannelId();
        // message to send
        var payload = new TextMessage(mapper.writeValueAsString(message));
        // iterate all connections
        for (var target : sessions.values()) {
            // send if subscribed
            if(target.subscriptions.contains(channelId)) {
                target.session.sendMessage(payload);
            }
        }
    }

    /**
     * Dump all messages to socket.
     * 
     * @param notifications
     * @throws IOException
     */
    public void dumpMessages(Wrapper wrapper) throws IOException, JsonProcessingException {
        for(var channelId : wrapper.subscriptions) {
            var messages = channelService.getMessagesByChannelId(channelId);
            for(var message : messages) {
                // message to send
                var payload = new TextMessage(mapper.writeValueAsString(message));
                wrapper.session.sendMessage(payload);
            }
        }
    }
}
