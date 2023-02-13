package space.nixus.phoneduck.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple token format to send to client as json.
 */
@Getter
@Setter
public class SimpleToken {
    private String value;
    private Long expires;

    public SimpleToken(Token token) {
        this.value = token.getToken();
        this.expires = token.getExpires();
    }
}
