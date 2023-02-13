package space.nixus.phoneduck.utils;

import org.springframework.web.socket.WebSocketSession;
import space.nixus.phoneduck.config.ApplicationConfig;
import space.nixus.phoneduck.error.UnauthorizedException;
import space.nixus.phoneduck.model.PhoneduckUser;
import space.nixus.phoneduck.service.UserService;

public class AuthHelper {

    /**
     * Get user by bearer token.
     * If invalid, throw AuthenticationException.
     * 
     * @param userService
     * @param authorization
     * @throws UnauthorizedException
     * @return PhoneduckUser
     */
    public static PhoneduckUser checkAuth(UserService userService, String authorization) throws UnauthorizedException {
        return userService.getUserByToken(authorization);
    }

    public static PhoneduckUser checkAuth(UserService userService, WebSocketSession session) throws UnauthorizedException {
        var headers = session.getHandshakeHeaders();
        return checkAuth(userService, headers.getFirst(ApplicationConfig.AUTH_HEADER));
    }
}
