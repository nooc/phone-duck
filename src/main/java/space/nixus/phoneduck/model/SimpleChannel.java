package space.nixus.phoneduck.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple channel format to send to client as json.
 */
@Getter
@Setter
public class SimpleChannel {
    private Long id;
    private String title;
    private boolean active;

    public SimpleChannel(Channel channel) {
        this.id = channel.getId();
        this.title = channel.getTitle();
        this.active = channel.isActive();
    }
}
