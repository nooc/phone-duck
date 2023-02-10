package space.nixus.phoneduck.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import space.nixus.phoneduck.handler.ChannelHandler;
import space.nixus.phoneduck.handler.RadioHandler;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Autowired
    RadioHandler radioHandler;

    @Autowired
    ChannelHandler channelHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(radioHandler,"/sub/channels/");
        registry.addHandler(channelHandler,"/sub/chat/");
    }
}
