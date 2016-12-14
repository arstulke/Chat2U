package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.cucumber.selenium.TestServer;
import de.chat2u.model.users.AuthenticationUser;
import de.chat2u.model.Message;
import de.chat2u.model.users.User;
import de.chat2u.utils.MessageBuilder;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created de.chat2u.cucumber.steps.PrivatChat in PACKAGE_NAME
 * by ARSTULKE on 22.11.2016.
 */
public class PrivatChat {

    private final AuthenticationUser user1 = new AuthenticationUser("user1", "user1_pass");
    private final AuthenticationUser user2 = new AuthenticationUser("user2", "user2_pass");
    private final AuthenticationUser user3 = new AuthenticationUser("user3", "user3_pass");
    private Message msg;

    @Gegebenseien("^drei angemeldete Benutzer$")
    public void dreiAngemeldeteBenutzer() throws Throwable {
        UserRepository<AuthenticationUser> repo = new UserRepository<>();
        repo.addUser(user1);
        repo.addUser(user2);
        repo.addUser(user3);
        ChatServer.initialize(new AuthenticationService(repo));

        ChatServer.login(user1.getUsername(), user1.getPassword(), TestServer.getMockSession());
        ChatServer.login(user2.getUsername(), user2.getPassword(), TestServer.getMockSession());
        ChatServer.login(user3.getUsername(), user3.getPassword(), TestServer.getMockSession());
    }

    @Wenn("^der Erste dem Zweiten eine Nachricht im privat Chat sendet$")
    public void derErsteDemZweitenEineNachrichtImPrivatChatSendet() throws Throwable {
        String msg = "Eine Nachricht";

        //chat erstellen
        Collection<User> users = Arrays.asList(user1, user2);
        String chatID = ChatServer.createGroup("test", users);

        //nachricht senden
        this.msg = new Message(user1.getUsername(), msg, chatID);
        ChatServer.sendTextMessageToChat(user1.getUsername(), msg, chatID);
    }

    @Dann("^wird diese nur im Chat des Ersten und des Zweiten sichtbar$")
    public void wirdDieseNurImChatDesErstenUndDesZweitenSichtbar() throws Throwable {
        RemoteEndpoint remote1 = ChatServer.getOnlineUsers().getByUsername(user1.getUsername()).getSession().getRemote();
        RemoteEndpoint remote2 = ChatServer.getOnlineUsers().getByUsername(user2.getUsername()).getSession().getRemote();
        verify(remote1).sendString(MessageBuilder.buildTextMessage(msg).toString());
        verify(remote2).sendString(MessageBuilder.buildTextMessage(msg).toString());
    }

    @Und("^nicht im Chat des Dritten$")
    public void nichtImChatDesDritten() throws Throwable {
        RemoteEndpoint remote3 = ChatServer.getOnlineUsers().getByUsername(user3.getUsername()).getSession().getRemote();
        verify(remote3, never()).sendString(MessageBuilder.buildTextMessage(msg).toString());
    }
}
