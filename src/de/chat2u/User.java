package de.chat2u;

/**
 * Created User in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class User {
    private String username;
    private String token;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return  token;
    }
}
