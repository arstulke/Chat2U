package de.chat2u.authentication;

import de.chat2u.model.users.AuthenticationUser;
import de.chat2u.model.users.User;

/**
 * Created AuthenticationService in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class AuthenticationService {
    private UserRepository<AuthenticationUser> repo = new UserRepository<>();

    public AuthenticationService(UserRepository<AuthenticationUser> registeredUsers) {
        repo = registeredUsers;
    }

    /**
     * Überprüft ob ein User, welcher sich einloggen möchte, mit den richtigen
     * Daten in dem {@link UserRepository} steht.
     * <p>
     *
     * @param username ist der Username, des einzuloggenden Benutzers
     * @param password ist das Passwort, des einzuloggenden Benutzers
     * @return den Datenbankuser aus dem {@link UserRepository}.
     */
    public User authenticate(String username, String password) {
        AuthenticationUser authenticationUser = repo.getByUsername(username);
        if (authenticationUser != null && authenticationUser.getPassword().equals(password)) {
            return authenticationUser.getSimpleUser();
        }
        return null;
    }

    /**
     * Überprüft ob der Username bereits existiert und
     * fügt einen neuen User zum {@link UserRepository} hinzu.
     * <p>
     *
     * @param authUser ist der zu hinzuzufügende benutzer
     */
    public void addUser(AuthenticationUser authUser) {
        repo.addUser(authUser);
    }

    /**
     * Überprüft, ob ein Benutzer bereit anhand seines Namens existiert.
     * <p>
     *
     * @param username ist der Benutzername des gesuchten Benutzers
     * @return {@code false} wenn der Benutzer nicht existiert
     **/
    public boolean userExist(String username) {
        return repo.containsUsername(username);
    }

    public void logout(User user) {
        String password = repo.getByUsername(user.getUsername()).getPassword();
        repo.addUser(new AuthenticationUser(user, password));
    }
}
