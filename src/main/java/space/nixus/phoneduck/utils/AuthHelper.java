package space.nixus.phoneduck.utils;

import space.nixus.phoneduck.error.AuthenticationException;
import space.nixus.phoneduck.model.PhoneduckUser;
import space.nixus.phoneduck.service.UserService;

public class AuthHelper {

    /**
     * Get user by bearer token.
     * If invalid, throw AuthenticationException.
     * 
     * @param userService
     * @param authorization
     * @throws AuthenticationException
     * @return PhoneduckUser
     */
    public static PhoneduckUser checkAuth(UserService userService, String authorization) throws AuthenticationException {
        if(authorization.startsWith("Bearer ")) {
            var tokenValue = authorization.substring(7);
            return userService.getUserByToken(tokenValue);
        }
        throw new AuthenticationException();
    }
}
