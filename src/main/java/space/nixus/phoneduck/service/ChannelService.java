package space.nixus.phoneduck.service;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.nixus.phoneduck.error.ChannelNotFoundException;
import space.nixus.phoneduck.model.Message;
import space.nixus.phoneduck.repository.ChannelRepository;
import space.nixus.phoneduck.repository.MessageRepository;

/**
 * Channel service for interacting with channels.
 * Get, create and delete messages.
 */
@Service
public class ChannelService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChannelRepository channelRepository;

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Message createMessages(long channelId, String sender, String text)
            throws ChannelNotFoundException {
        var message = new Message(null, sender, channelId, Instant.now().toEpochMilli(), text);
        if(!channelRepository.existsById(channelId)) {
            throw new ChannelNotFoundException();
        }
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChannelId(long channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    public void deleteMessagesByChannelId(long channelId) {
        messageRepository.deleteByChannelId(channelId);
    }
}
