package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.Permissions;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.network.ChatWebSocketHandler;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import spark.Spark;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created TestServer in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
public class TestServer {
    public static final AuthenticationUser user1 = new AuthenticationUser("Carsten", "pw1", Permissions.USER);
    public static final AuthenticationUser user2 = new AuthenticationUser("Marianne", "pw2", Permissions.USER);
    public static final AuthenticationUser user3 = new AuthenticationUser("Thorsten", "pw3", Permissions.USER);

    public static void initialize() {
        UserRepository<AuthenticationUser> repo = new UserRepository<>();
        repo.addUser(user1);
        repo.addUser(user2);
        repo.addUser(user3);
        ChatServer.initialize(new AuthenticationService(repo));
    }

    public static Session getMockSession() {
        Session session = mock(Session.class);
        RemoteEndpoint remote = mock(RemoteEndpoint.class);
        when(session.getRemote()).thenReturn(remote);
        return session;
    }

    public static void start(){
        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.staticFileLocation("/public");
        Spark.port(80);
        Spark.init();
    }
}
