package space.nixus.phoneduck.repository;

import org.springframework.stereotype.Repository;
import space.nixus.phoneduck.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository of token entities.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    Token getReferenceByToken(String tokenValue);
}
