package de.chat2u.junit;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


/**
 * Created FakeClient in de.chat2u.junit
 * by ARSTULKE on 16.11.2016.
 */
public class FakeClient {
    public static void connectToWebSocket(String host, int port, String fakeMessage) throws IOException {
        Socket client = new Socket(host, port);
        OutputStream out = client.getOutputStream();
        out.write(fakeMessage.getBytes());
        out.close();
        client.close();
    }
}
