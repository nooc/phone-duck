package space.nixus.phoneduck;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import space.nixus.phoneduck.model.PhoneduckUser;
import space.nixus.phoneduck.repository.UserRepository;

@SpringBootApplication
public class PhoneduckApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoneduckApplication.class, args);
    }

    @Autowired
    UserRepository userRepo;

    @EventListener
    public void handleReadyEvent(ApplicationReadyEvent event) {
        
        // add test accounts if id 1 not in db
        userRepo.saveAllAndFlush(List.of(
            new PhoneduckUser(1L,"admin","Admin","admin",true),
            new PhoneduckUser(2L,"user","User","user",false)
        ));

    }
}