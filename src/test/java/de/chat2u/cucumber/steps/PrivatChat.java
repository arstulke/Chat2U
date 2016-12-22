package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.cucumber.selenium.OfflineChatContainer;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.cucumber.selenium.OfflineDataBase;
import de.chat2u.cucumber.selenium.TestServer;
import de.chat2u.model.Message;
import de.chat2u.model.User;
import de.chat2u.utils.MessageBuilder;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Created de.chat2u.cucumber.steps.PrivatChat in PACKAGE_NAME
 * by ARSTULKE on 22.11.2016.
 */
public class PrivatChat {

    private final User user1 = new User("user1");
    private final User user2 = new User("user2");
    private final User user3 = new User("user3");
    private Message msg;

    @Gegebenseien("^drei angemeldete Benutzer$")
    public void dreiAngemeldeteBenutzer() throws Throwable {
        DataBase repo = new OfflineDataBase();
        repo.addUser(user1, "user1_pass");
        repo.addUser(user2, "user2_pass");
        repo.addUser(user3, "user3_pass");
        ChatServer.initialize(repo, new OfflineChatContainer());

        ChatServer.login(user1.getUsername(), "user1_pass", TestServer.getMockSession());
        ChatServer.login(user2.getUsername(), "user1_pass", TestServer.getMockSession());
        ChatServer.login(user3.getUsername(), "user1_pass", TestServer.getMockSession());
    }

    @Wenn("^der Erste dem Zweiten eine Nachricht im privat Chat sendet$")
    public void derErsteDemZweitenEineNachrichtImPrivatChatSendet() throws Throwable {
        String msg = "Eine Nachricht";

        //chat erstellen
        Set<User> users = new HashSet<>(Arrays.asList(user1, user2));
        String chatID = ChatServer.createGroup("Test", users);

        //nachricht senden
        this.msg = new Message(user1.getUsername(), msg, chatID);
        ChatServer.sendTextMessageToChat(user1.getUsername(), msg, chatID);
    }

    @Dann("^wird diese nur im Chat des Ersten und des Zweiten sichtbar$")
    public void wirdDieseNurImChatDesErstenUndDesZweitenSichtbar() throws Throwable {
        RemoteEndpoint remote1 = ChatServer.getOnlineUsers().getSessionByName(user1.getUsername()).getRemote();
        RemoteEndpoint remote2 = ChatServer.getOnlineUsers().getSessionByName(user2.getUsername()).getRemote();
        verify(remote1).sendString(MessageBuilder.buildTextMessage(msg).toString());
        verify(remote2).sendString(MessageBuilder.buildTextMessage(msg).toString());
    }

    @Und("^nicht im Chat des Dritten$")
    public void nichtImChatDesDritten() throws Throwable {
        RemoteEndpoint remote3 = ChatServer.getOnlineUsers().getSessionByName(user3.getUsername()).getRemote();
        verify(remote3, never()).sendString(MessageBuilder.buildTextMessage(msg).toString());
    }
}
