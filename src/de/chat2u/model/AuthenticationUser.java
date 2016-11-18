package de.chat2u.model;


import de.chat2u.authentication.Permissions;

/**
 * Created AuthenticationUser in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class AuthenticationUser extends User {
    private String password;

    /**
     * @see User
     */
    public AuthenticationUser(String username, String password, Permissions permissions) {
        super(username, permissions);
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
        return new User(getUsername(), getPermissions(), getHistory(), getSession());
    }
}
