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
        if(userRepo.getReferenceByUsername("admin")==null) {
            userRepo.save(new PhoneduckUser(null,"admin","Admin","admin",true));
        }
        if(userRepo.getReferenceByUsername("user")==null) {
            userRepo.save(new PhoneduckUser(null,"user","User","user",false));
        }
    }
}