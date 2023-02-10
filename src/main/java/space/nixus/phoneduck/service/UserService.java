package space.nixus.phoneduck.service;

import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.nixus.phoneduck.model.Token;
import space.nixus.phoneduck.model.UserPass;
import space.nixus.phoneduck.model.PhoneduckUser;
import space.nixus.phoneduck.repository.UserRepository;
import space.nixus.phoneduck.repository.TokenRepository;
import space.nixus.phoneduck.error.AuthenticationException;

/**
 * User service for 
 */
@Service
public class UserService {

    private static final long EXPIRE_OFFSET = 3600000; // 1h

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;


    /**
     * Create new user.
     * 
     * @param username
     * @param password
     * @return PhoneduckUser
     */
    public PhoneduckUser createUser(String username, String displayName, String password, boolean isAdmin) {
        var user = new PhoneduckUser(null, username, displayName, password, isAdmin);
        return userRepository.save(user);
    }

    /**
     * Create new user token.
     * 
     * @param username
     * @param password
     * @return Token
     * @throws AuthenticationException
     */
    public Token createToken(String username, String password) throws AuthenticationException{
        var user = userRepository.getReferenceByUsername(username);
        var passwordHash = password; // TODO encode to hash
        if(user != null && user.getPassword().equals(passwordHash)) {
            var tokenValue = UUID.randomUUID().toString();
            var token = new Token(null, user.getId(), tokenValue, Instant.now().toEpochMilli() + EXPIRE_OFFSET);
            tokenRepository.save(token);
            return token;
        }
        throw new AuthenticationException();
    }

    /**
     * Get Token instance using token value.
     * 
     * @param tokenValue
     * @return Token
     * @throws AuthenticationException
     */
    public Token getToken(String tokenValue) throws AuthenticationException {
        var now = Instant.now().toEpochMilli();
        var token = tokenRepository.getReferenceByToken(tokenValue);
        if(token == null || token.getExpires() <= now) {
            throw new AuthenticationException();
        }
        return token;
    }

    /**
     * Get PhoneduckUser instance by id.
     * 
     * @param id
     * @return PhoneduckUser
     */
    public PhoneduckUser getUserById(long id) {
        return userRepository.getReferenceById(id);
    }
    
    /**
     * Get PhoneduckUser instance using token value.
     * 
     * @param tokenValue
     * @return
     * @throws AuthenticationException
     */
    public PhoneduckUser getUserByToken(String tokenValue) throws AuthenticationException {
        var token = getToken(tokenValue);
        return userRepository.getReferenceById(token.getId());
    }
}