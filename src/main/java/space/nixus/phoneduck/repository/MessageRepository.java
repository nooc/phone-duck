package space.nixus.phoneduck.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import space.nixus.phoneduck.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

/**
 * Repository of message entities.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByChannelId(Long channelId);

    @Modifying
    void deleteByChannelId(Long channelId);
}
