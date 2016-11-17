package de.chat2u.authentication;

import de.chat2u.exceptions.UsernameExistException;
import de.chat2u.model.AuthenticationUser;

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
     *
     * @return den Datenbankuser aus dem {@link UserRepository}.
     * */
    public AuthenticationUser authenticate(String username, String password) {
        AuthenticationUser authenticationUser = repo.getByUsername(username);
        if (authenticationUser != null && authenticationUser.getPassword().equals(password)) {
            return authenticationUser;
        }
        return null;
    }

    /**
     * Überprüft ob der Username bereits existiert und
     * fügt einen neuen User zum {@link UserRepository} hinzu.
     * <p>
     *
     * @param authUser ist der zu hinzuzufügende benutzer
     * @throws UsernameExistException wenn der Username bereits existiert.
     * */
    public void addUser(AuthenticationUser authUser) {
        if (repo.containsUsername(authUser.getUsername()))
            throw new UsernameExistException("Benutzername bereits vergeben");
        repo.addUser(authUser);
    }
}
