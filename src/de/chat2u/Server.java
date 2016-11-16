package de.chat2u;

import spark.Spark;

/**
 * Created Server in de.chat2u
 * by ARSTULKE on 16.11.2016.
 */
public class Server {
    public static int port = 8080;

    public static void start() {
        Spark.staticFileLocation("/public");
        Spark.webSocket("/", ChatWebSocketHandler.class);
        Spark.setPort(port);
        Spark.init();
    }
}
