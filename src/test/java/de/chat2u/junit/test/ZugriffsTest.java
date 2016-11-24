package de.chat2u.junit.test;

import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
@SuppressWarnings("SpellCheckingInspection")
/**
 * Created ZugriffsTest in de.chat2u.junit
 * by ARSTULKE on 24.11.2016.
 */

public class ZugriffsTest {

    @Test
    public void benutzerEinloggen() {
        //gegeben
        Datenbank.neuerBenutzer("Max", "geheim");

        //wenn
        ChatServer.einloggen("Max", "geheim");

        //dann
        assertThat(ChatServer.eingeloggteBenutzer, contains("Max"));
    }
}
