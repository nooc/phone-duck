package space.nixus.phoneduck.controller;

import space.nixus.phoneduck.error.AuthenticationException;
import space.nixus.phoneduck.error.ChannelNotFoundException;
import space.nixus.phoneduck.handler.RadioHandler;
import space.nixus.phoneduck.model.Channel;
import space.nixus.phoneduck.model.Message;
import space.nixus.phoneduck.service.RadioService;
import space.nixus.phoneduck.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.websocket.server.PathParam;
import space.nixus.phoneduck.utils.AuthHelper;

@RestController
public class ChannelController {

    @Autowired
    RadioService radioService;
    
    @Autowired
    private UserService userService;

    @Autowired
    RadioHandler radioHandler;

    @GetMapping("/channels/")
    List<Channel> getChannels(
            @RequestHeader("Authorization") String auth) throws AuthenticationException {
        AuthHelper.checkAuth(userService, auth);
        return radioService.getChannels();
    }

    @PostMapping("/channels/")
    Channel addChannel(
            @RequestHeader("Authorization") String auth,
            @RequestParam("title") String title) throws AuthenticationException, JsonProcessingException, IOException {
        var user = AuthHelper.checkAuth(userService, auth);
        var channel = radioService.createChannel(user.getId(), title);
        radioHandler.announce(channel);
        return channel;
    }

    @DeleteMapping("/channels/{id}")
    void removeChannel(
            @RequestHeader("Authorization") String auth,
            @PathParam("id") long id) throws AuthenticationException, ChannelNotFoundException {
        AuthHelper.checkAuth(userService, auth);
        radioService.removeChannel(id);
    }


}
