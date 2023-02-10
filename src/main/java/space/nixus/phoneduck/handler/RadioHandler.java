package space.nixus.phoneduck.handler;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import space.nixus.phoneduck.model.Channel;
import space.nixus.phoneduck.model.SimpleChannel;
import space.nixus.phoneduck.service.RadioService;


@Component
public class RadioHandler extends TextWebSocketHandler {

    @Autowired
    private RadioService radioService;

    private final Map<String,WebSocketSession> sessions;
    private final ObjectMapper mapper;

    public RadioHandler() {
        sessions = new HashMap<>();
        mapper = new ObjectMapper(); 
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        dumpChannels(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
            throws Exception {
        sessions.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    /**
     * Announce channel.
     * 
     * @param notifications
     * @throws IOException
     */
    public void announce(Channel channel) throws IOException, JsonProcessingException {
        // message to send
        var payload = new TextMessage(mapper.writeValueAsString(new SimpleChannel(channel)));
        // iterate all connections
        for (var target : sessions.values()) {
            // send to info channel
            target.sendMessage(payload);
        }
    }

    private void dumpChannels(WebSocketSession session) {
        var channels = radioService.getChannels();
        for(var channel : channels) {
            if(channel.isActive()) {
                var simple = new SimpleChannel(channel);
                try {
                    session.sendMessage(new TextMessage(mapper.writeValueAsString(simple)));
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
