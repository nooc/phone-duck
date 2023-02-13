package space.nixus.phoneduck.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Not enough privileges.")
public class PrivilegeException extends Exception { 
    public PrivilegeException() { super("Not enough privileges."); }
    public PrivilegeException(String message) { super(message); }
}
