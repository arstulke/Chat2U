package de.chat2u.junit.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created ChatServer in de.chat2u.junit.test
 * by ARSTULKE on 24.11.2016.
 */
public class ChatServer {
    public static List<String> eingeloggteBenutzer = new ArrayList<>();

    public static void einloggen(String benutzer, String passwort) {
        eingeloggteBenutzer.add(benutzer);
    }
}
