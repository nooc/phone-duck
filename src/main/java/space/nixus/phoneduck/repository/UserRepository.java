package space.nixus.phoneduck.repository;

import org.springframework.stereotype.Repository;
import space.nixus.phoneduck.model.PhoneduckUser;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository of user entnties.
 */
@Repository
public interface UserRepository extends JpaRepository<PhoneduckUser,Long> {

    PhoneduckUser getReferenceByUsername(String username);
}
