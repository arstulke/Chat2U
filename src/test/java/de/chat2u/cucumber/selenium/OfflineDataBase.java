package de.chat2u.cucumber.selenium;

import de.chat2u.persistence.users.DataBase;
import de.chat2u.model.users.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created DataBase in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class OfflineDataBase implements DataBase {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> passwords = new ConcurrentHashMap<>();

    /**
     * Fügt einen {@link User} zur Liste hinzu.
     * <p>
     *
     * @param user     ist der hinzu zufügende User.
     * @param password
     */
    public synchronized void addUser(User user, String password) {
        users.put(user.getUsername(), user);
        passwords.put(user.getUsername(), password);
    }

    /**
     * Gibt einen User aus der Liste zurück, anhand des Benutzernames.
     * <p>
     *
     * @param username ist der Benutzername, des Benutzers
     * @return einen User, wenn dieser gefunden wurde oder {@code null} wenn
     * dieser nicht gefunden werden konnte
     */
    public synchronized User getByUsername(String username) {
        return users.get(username);
    }

    @Override
    public boolean contains(String username) {
        return users.containsKey(username);
    }

    @Override
    public void removeUser(User user) {
        users.remove(user.getUsername());
        passwords.remove(user.getUsername());
    }

    @Override
    public User authenticate(String username, String password) {
        User user = getByUsername(username);
        if (user != null && passwords.get(username).equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public void close() throws Exception {
    }
}
