package space.nixus.phoneduck.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Authentication Failed")
public class UnauthorizedException extends Exception {
    public UnauthorizedException() { super("Not Authorized"); }
    public UnauthorizedException(String username) { super("User " + username+" is not authorized."); }
}
