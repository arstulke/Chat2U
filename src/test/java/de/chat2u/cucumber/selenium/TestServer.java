package de.chat2u.cucumber.selenium;

import de.chat2u.ChatServer;
import de.chat2u.Server;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created TestServer in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
public class TestServer {

    private final DataBase dataBase = new OfflineDataBase();

    public static void main(String[] args) {
        new TestServer();
    }

    private TestServer() {
        HashMap<String, String> userData = generateUserData();
        userData.forEach((username, pw) -> dataBase.addUser(new User(username), pw));
        dataBase.addUser(new User("Arne"), "Test123");
        dataBase.addUser(new User("Kito"), "Test123");

        ChatServer.initialize(dataBase, new OfflineChatContainer());

        userData.forEach((username, pw) -> ChatServer.login(username, pw, getMockSession()));

        Server.start();
    }

    private HashMap<String, String> generateUserData() {
        List<String> names = Arrays.asList("Marco", "Heike", "Gabriele", "Andreas", "Sophia", "Robert", "Alexander", "Mario", "Patrick", "Mandy", "Andrea", "Jörg", "Leah", "Bernd", "Dirk", "Brigitte", "Klaus", "Tom", "Michael", "Eric", "Mike", "Maik", "Florian", "Martina", "Christin", "Juliane", "Nicole", "Luca", "Angelika", "Daniela", "Simone", "Stephanie", "Vanessa", "Thomas", "Franziska", "Melanie", "Sara", "Matthias", "Felix", "Leonie", "Kevin", "Jessica", "Jan", "Laura", "Petra", "Sven", "Sandra", "Ines", "Julia", "Steffen", "Stephan", "Jana", "Katja", "Dominik", "Thorsten", "Lisa", "Leon", "Stefanie", "Anne", "Uwe", "Jens", "Ursula", "Birgit", "Diana", "Jessika", "Tim", "Ulrike", "Marina", "Philipp", "Mathias", "Marcel", "Kristian", "Lukas", "Lea", "René", "Marko", "Yvonne", "Lucas", "Sabine", "Michelle", "Uta", "Katrin", "Maximilian", "Klaudia", "Jürgen", "Stefan", "Paul", "Sabrina", "Maria", "Dieter", "Peter", "Silke", "Wolfgang", "Tobias", "Monika", "Sophie", "Doreen", "Antje", "Nadine", "Ulrich", "Johanna", "Manuela", "Erik", "Benjamin", "Ute", "Karin");
        HashMap<String, String> userData = new HashMap<>();
        names.forEach(name -> userData.put(name, RandomStringUtils.random(8, true, true)));
        return userData;
    }

    public static Session getMockSession() {
        Session session = mock(Session.class);
        RemoteEndpoint remote = mock(RemoteEndpoint.class);
        when(session.getRemote()).thenReturn(remote);
        return session;
    }
}
