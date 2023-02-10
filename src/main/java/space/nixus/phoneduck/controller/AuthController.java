package space.nixus.phoneduck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.nixus.phoneduck.model.SimpleToken;
import space.nixus.phoneduck.model.UserPass;
import space.nixus.phoneduck.service.UserService;
import space.nixus.phoneduck.error.AuthenticationException;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    SimpleToken authenticate(@RequestBody UserPass login) throws AuthenticationException {
        return new SimpleToken(userService.createToken(login.getUser(), login.getPass()));
    }
}
