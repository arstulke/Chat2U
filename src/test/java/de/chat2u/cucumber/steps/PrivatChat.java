package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.Permissions;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.Message;
import de.chat2u.utils.MessageBuilder;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.mockito.stubbing.Answer;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created de.chat2u.cucumber.steps.PrivatChat in PACKAGE_NAME
 * by ARSTULKE on 22.11.2016.
 */
public class PrivatChat {

    private AuthenticationUser user1 = new AuthenticationUser("user1", "user1_pass", Permissions.USER);
    private AuthenticationUser user2 = new AuthenticationUser("user2", "user2_pass", Permissions.USER);
    private String msg;

    @Gegebenseien("^zwei angemeldete Benutzer$")
    public void zweiAngemeldeteBenutzer() throws Throwable {
        UserRepository<AuthenticationUser> repo = new UserRepository<>();
        repo.addUser(user1);
        repo.addUser(user2);
        ChatServer.initialize(new AuthenticationService(repo));

        Session session1 = mock(Session.class);
        Session session2 = mock(Session.class);
        RemoteEndpoint remote1 = mock(RemoteEndpoint.class);
        RemoteEndpoint remote2 = mock(RemoteEndpoint.class);
        when(session1.getRemote()).thenReturn(remote1);
        when(session2.getRemote()).thenReturn(remote2);

        ChatServer.login(user1.getUsername(), user1.getPassword(), session1);
        ChatServer.login(user2.getUsername(), user2.getPassword(), session2);
    }

    @Wenn("^der Erste dem Zweiten eine Nachricht im privat Chat sendet$")
    public void derErsteDemZweitenEineNachrichtImPrivatChatSendet() throws Throwable {
        msg = "Eine Nachricht";
        ChatServer.sendPrivateMessage(msg, user1.getUsername(), user2.getUsername());
    }

    @Dann("^wird diese nur im privaten Chat sichtbar$")
    public void wirdDieseNurImPrivatenChatSichtbar() throws Throwable {
        RemoteEndpoint remote = ChatServer.getOnlineUsers().getByUsername(user2.getUsername()).getSession().getRemote();
        verify(remote).sendString(MessageBuilder.buildMessage(new Message(user1.getUsername(), msg, ChatServer.PRIVAT)));
    }
}
