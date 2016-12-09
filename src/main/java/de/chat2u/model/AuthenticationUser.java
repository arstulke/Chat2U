package de.chat2u.model;


/**
 * Created AuthenticationUser in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class AuthenticationUser extends User {
    private final String password;

    /**
     * @see User
     */
    public AuthenticationUser(String username, String password) {
        super(username);
        this.password = password;
    }

    /**
     * @return das Password des Users
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return einen neuen {@link User} ohne Passwort
     */
    public User getSimpleUser() {
        return new User(getUsername(), getHistory(), getSession());
    }
}
