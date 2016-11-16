package de.chat2u;

import spark.Spark;

public class Main {
    public static void main(String[] args) {
        Spark.staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.setPort(80);
        Spark.init();
    }
}