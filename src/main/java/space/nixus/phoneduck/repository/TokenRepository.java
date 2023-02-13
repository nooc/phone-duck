package space.nixus.phoneduck.repository;

import org.springframework.stereotype.Repository;
import space.nixus.phoneduck.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * Repository of token entities.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token,Long> {

    Token getReferenceByToken(String tokenValue);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.expires <= :now")
    void deleteAllExpired(@Param("now") Long now);
}
