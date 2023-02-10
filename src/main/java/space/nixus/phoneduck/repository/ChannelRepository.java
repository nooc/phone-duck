package space.nixus.phoneduck.repository;

import org.springframework.stereotype.Repository;
import space.nixus.phoneduck.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository of channel entities.
 */
@Repository
public interface ChannelRepository extends JpaRepository<Channel,Long> {
}
