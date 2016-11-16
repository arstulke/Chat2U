package de.chat2u;

import java.util.ArrayList;
import java.util.List;

/**
 * Created Chat in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class Chat {
    private final UserRepository userRepository;
    private final List<User> onlineUsers = new ArrayList<>();

    public Chat(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(String username, String password) {
        User user = userRepository.authenticate(username, password);
        if(user != null){
            onlineUsers.add(user);
        }
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }
}
