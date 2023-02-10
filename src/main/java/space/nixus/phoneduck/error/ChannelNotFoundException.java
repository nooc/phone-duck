package space.nixus.phoneduck.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Channel Not Found")
public class ChannelNotFoundException extends Exception { 
    public ChannelNotFoundException() { super("Channel not found."); }
    public ChannelNotFoundException(long channelId) { super("Channel "+channelId+" not found."); }
}
