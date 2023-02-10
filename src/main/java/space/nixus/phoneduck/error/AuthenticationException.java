package space.nixus.phoneduck.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Authentication Failed")
public class AuthenticationException extends Exception {
    public AuthenticationException() { super("Authentication Failed"); }
    public AuthenticationException(String username) { super("Authentication failed for "+username+"."); }
}
