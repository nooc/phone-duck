package space.nixus.phoneduck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.nixus.phoneduck.model.SimpleToken;
import space.nixus.phoneduck.model.UserPass;
import space.nixus.phoneduck.service.UserService;
import space.nixus.phoneduck.error.UnauthorizedException;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    SimpleToken requestToken(@RequestBody UserPass login) throws UnauthorizedException {
        return new SimpleToken(userService.createToken(login.getUser(), login.getPass()));
    }
}
