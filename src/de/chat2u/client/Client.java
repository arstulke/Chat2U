package de.chat2u.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Chat2U:
 * * PACKAGE_NAME:
 * * * Created by KAABERT on 15.11.2016.
 */
public class Client {
    private Socket connection;
    private List<Message> messages;

    public Client(String ip, int port) throws IOException {
        messages = new ArrayList<>();

        connection = new Socket(ip, port);
        (new Listener(false)).start();
    }

    /**
     * Sendet das Object an den Server
     */
    public void sendMessage(Object obj) {

    }

    /**
     * Sende ein Request und bekomme eine direkte Antwort.
     * Die Methode ist für aktive abfragen zu benutzen,
     * wie zum Beispiel das Anmelden.
     * <p>
     *
     * @param obj ist das zu sendene Objekt
     * @return die Antwort des Servers;
     */
    public Result sendWithResult(Object obj) {
        Listener l = (new Listener(true));
        l.start();
        System.out.println(l.getResult());
        return new Result(501, null);
    }

    public void writeInChat(Object input) {

    }

    private class Listener extends Thread {
        private final boolean resultMode;
        private Result result;

        public Listener(boolean resultMode) {
            this.resultMode = resultMode;
        }

        public void run() {
            while (connection.isConnected()) {

                try (ObjectInputStream input = new ObjectInputStream(connection.getInputStream())) {
                    Result res = (Result) input.readObject();
                    if(resultMode)
                        result = res;
                    else
                    {
                        /*
                        * Message handling
                        * */
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        public Result getResult() {
            return result;
        }
    }
}
