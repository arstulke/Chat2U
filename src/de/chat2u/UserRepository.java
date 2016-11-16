package de.chat2u;

import java.util.HashMap;
import java.util.Map;

/**
 * Created UserRepository in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
