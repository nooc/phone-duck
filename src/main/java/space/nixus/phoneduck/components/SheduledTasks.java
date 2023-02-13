package space.nixus.phoneduck.components;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.nixus.phoneduck.repository.TokenRepository;

/**
 * Cheduled tasks.
 */
@Component
public class SheduledTasks {

    @Autowired
    private TokenRepository tokenRepository;
    
    /**
     * Clean up expired tokens.
     */
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    void tokenCleanup() {
        tokenRepository.deleteAllExpired(Instant.now().toEpochMilli());
    }
}
