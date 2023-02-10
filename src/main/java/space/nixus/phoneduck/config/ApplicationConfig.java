package space.nixus.phoneduck.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import space.nixus.phoneduck.service.UserService;

@Configuration
public class ApplicationConfig {

    public static final String SUBSCRIPTION_HEADER = "X-Subscriptions";
    public static final String AUTH_HEADER = "Authorization";

    @Autowired
    UserService userService;

    @EventListener(ContextRefreshedEvent.class)
    void preRequestInitializer() {
        userService.createUser("user", "Bob", "user", false);
        userService.createUser("admin", "Sam", "admin", true);
    }
}
