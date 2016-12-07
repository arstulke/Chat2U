package de.chat2u.cucumber.selenium;

import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.network.ChatWebSocketHandler;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.openqa.selenium.WebDriver;
import spark.Spark;

import java.util.TreeMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created TestServer in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
public class TestServer {
    public static final AuthenticationUser user1 = new AuthenticationUser("Carsten", "pw1");

    public static final TreeMap<String, WebDriver> client = new TreeMap<>();
    public static AuthenticationService authenticationService = new AuthenticationService(new UserRepository<>());

    public static WebSocketSession getMockSession() {
        WebSocketSession session = mock(WebSocketSession.class);
        RemoteEndpoint remote = mock(RemoteEndpoint.class);
        when(session.getRemote()).thenReturn(remote);
        return session;
    }

    public static void start() {
        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.staticFileLocation("/public");
        Spark.port(80);
        Spark.init();
    }
}
