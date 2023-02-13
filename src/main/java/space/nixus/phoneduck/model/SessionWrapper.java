package space.nixus.phoneduck.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.socket.WebSocketSession;
import lombok.Getter;

/**
 * Wrap session together with user and subscriptions.
 */
@Getter
public class SessionWrapper {

    private final WebSocketSession session;
    private final List<Long> subscriptions;
    private final PhoneduckUser user;
    
    /**
     * Construct wrapper.
     * @param session
     * @param user
     */
    public SessionWrapper(WebSocketSession session, PhoneduckUser user) {
        this.session = session;
        this.user = user;
        this.subscriptions = new ArrayList<>();
    }

    /**
     * Add a subscription.
     * @param channelId
     */
    public void addSubscription(Long channelId) {
        if(!subscriptions.contains(channelId)) {
            subscriptions.add(channelId);
        }
    }

    public void removeSubscription(Long channelId) {
        subscriptions.remove(channelId);
    }

    public boolean hasSubscription() {
        return !subscriptions.isEmpty();
    }
}