package de.chat2u.persistence.users;

import de.chat2u.model.users.User;

/**
 * Created DataBase in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public interface DataBase extends AutoCloseable {
    /**
     * Überprüft ob ein User, welcher sich einloggen möchte, mit den richtigen
     * Daten in dem {@link DataBase} steht.
     * <p>
     *
     * @param username ist der Username, des einzuloggenden Benutzers
     * @param password ist das Passwort, des einzuloggenden Benutzers
     * @return den Datenbankuser aus dem {@link DataBase}.
     */
    User authenticate(String username, String password);

    void addUser(User user, String password);

    /**
     * Gibt einen User aus der Liste zurück, anhand des Benutzernames.
     * <p>
     *
     * @param username ist der Benutzername, des Benutzers
     * @return einen User, wenn dieser gefunden wurde oder {@code null} wenn
     * dieser nicht gefunden werden konnte
     */
    User getByUsername(String username);

    boolean contains(String username);

    void removeUser(User user);
}
