package space.nixus.phoneduck.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Login credentials.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserPass {
    private String user;
    private String pass;
}
