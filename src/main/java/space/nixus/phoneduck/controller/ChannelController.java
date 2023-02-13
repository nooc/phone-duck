package space.nixus.phoneduck.controller;

import space.nixus.phoneduck.components.RadioHandler;
import space.nixus.phoneduck.config.ApplicationConfig;
import space.nixus.phoneduck.error.UnauthorizedException;
import space.nixus.phoneduck.error.ChannelNotFoundException;
import space.nixus.phoneduck.error.PrivilegeException;
import space.nixus.phoneduck.model.Channel;
import space.nixus.phoneduck.model.ChannelParams;
import space.nixus.phoneduck.service.RadioService;
import space.nixus.phoneduck.service.UserService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
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
            @RequestHeader(ApplicationConfig.AUTH_HEADER) String auth) throws UnauthorizedException {
        AuthHelper.checkAuth(userService, auth);
        return radioService.getChannels();
    }

    @PostMapping("/channels/")
    Channel addChannel(
            @RequestHeader(ApplicationConfig.AUTH_HEADER) String auth,
            @RequestBody ChannelParams params) throws UnauthorizedException, JsonProcessingException, IOException {
        var user = AuthHelper.checkAuth(userService, auth);
        var channel = radioService.createChannel(user.getId(), params.getTitle());
        radioHandler.announce(channel);
        return channel;
    }

    @DeleteMapping("/channels/{id}")
    void removeChannel(
            @RequestHeader(ApplicationConfig.AUTH_HEADER) String auth,
            @PathVariable("id") Long id) throws UnauthorizedException, ChannelNotFoundException, PrivilegeException {
        var user = AuthHelper.checkAuth(userService, auth);
        var channel = radioService.getChannel(id);
        if(user.getSuperuser() || user.getId().equals(channel.getOwnerId())) {
            radioService.removeChannel(id);
            return;
        }
        throw new PrivilegeException("Channel removal not allowed by this user.");
    }
}
