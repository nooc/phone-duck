package space.nixus.phoneduck.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.nixus.phoneduck.error.ChannelNotFoundException;
import space.nixus.phoneduck.model.Channel;
import space.nixus.phoneduck.repository.ChannelRepository;
import space.nixus.phoneduck.repository.MessageRepository;

/**
 * Radio service for interacting with the radio.
 * List, create and delete channels.
 */
@Service
public class RadioService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private MessageRepository messageRepository;

    
    /**
     * List all channels.
     * 
     * @return list of cghannels
     */
    public List<Channel> getChannels() {
        return channelRepository.findAll();
    }

    public Channel createChannel(long userId, String title) {
        var channel = new Channel(null, userId, true, title);
        return channelRepository.save(channel);
    }

    public Channel getChannel(long channelId) {
        var opt = channelRepository.findById(channelId);
        return opt.get();
    }

    /**
     * Remove a channel.
     * @param channelId channel id to remove
     * @return success/fail
     * @throws ChannelNotFoundException
     */
    public boolean removeChannel(long channelId) throws ChannelNotFoundException {
        if(channelRepository.existsById(channelId)) {
            channelRepository.deleteById(channelId);
            messageRepository.deleteByChannelId(channelId);
            return true;
        }
        else {
            throw new ChannelNotFoundException(channelId);
        }
    }
}
