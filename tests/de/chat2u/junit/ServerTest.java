package de.chat2u.junit;

import de.chat2u.ChatWebSocketHandler;
import de.chat2u.Server;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created ServerTest in de.chat2u.junit
 * by ARSTULKE on 16.11.2016.
 */
public class ServerTest {

    @Test
    public void startServer() throws IOException {
        //given
        Server.start();

        //when
        FakeClient.connectToWebSocket("localhost", Server.port, "hello");

        //then
        assertThat(ChatWebSocketHandler.getMessage(), is("hello"));

    }

}
