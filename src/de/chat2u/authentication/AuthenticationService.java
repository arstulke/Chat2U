package de.chat2u.authentication;

import de.chat2u.exceptions.AccessDeniedException;
import de.chat2u.exceptions.UsernameExistException;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created AuthenticationService in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class AuthenticationService {
    private UserRepository<AuthenticationUser> repo = new UserRepository<>();
    private Map<String, Permissions> tokens;

    public AuthenticationService(UserRepository<AuthenticationUser> registeredUsers) {
        repo = registeredUsers;
        tokens = new HashMap<>();
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
     */
    public void addUser(AuthenticationUser authUser) {
        if (userExist(authUser.getUsername()))
            throw new UsernameExistException("Benutzername bereits vergeben");
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

    /**
     * Gibt das Berechtigungslevel für einen generierten Berechtigungsschlüssel zurück.
     * <p>
     *
     * @param token ist der Berechtigungsschlüssel
     * @return das {@link Permissions Berechtigungslevel}
     * @throws AccessDeniedException wenn der Berechtigungsschlüssel ungültig ist
     * */
    public Permissions verifyToken(String token) {
        if (token == null)
            return Permissions.USER;
        else if (!tokens.containsKey(token))
            throw new AccessDeniedException("Ungültiger Token");
        Permissions permissions = tokens.get(token);
        tokens.remove(token);
        return permissions;
    }

    /**
     * Fügt einen neu generierten Token zur Liste hinzu.
     * <p>
     *
     * @param permissions ist das {@link Permissions Berechtigungslevel} für den Token
     * @return den generierten Berechtigungsschlüssel für das Berechtigungslevel
     * */
    public String generateToken(Permissions permissions) {
        String token = RandomStringUtils.random(32, true, true);
        if (tokens.containsKey(token))
            return generateToken(permissions);
        tokens.put(token, permissions);
        return token;
    }

    public User getUserByName(String username) {
        return repo.getByUsername(username).getSimpleUser();
    }
}
