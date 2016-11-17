package de.chat2u.authentication;

/**
 * Created Permissions in de.chat2u.authentication
 * by ARSTULKE on 17.11.2016.
 */
public enum Permissions {
    ADMIN("Administrator"), USER("Teilnehmer");

    private final String lvlString;

    Permissions(String lvlString) {
        this.lvlString = lvlString;
    }

    @Override
    public String toString() {
        return lvlString;
    }
}
