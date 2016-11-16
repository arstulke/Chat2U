package de.chat2u;

/**
 * Created User in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class User {
    private final String username, password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
