package de.chat2u;

import de.chat2u.network.ChatWebSocketHandler;
import de.chat2u.persistence.chats.ChatContainer;
import de.chat2u.persistence.chats.JPAChatContainer;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.persistence.users.JPADataBase;
import spark.Spark;

/**
 * Created Server in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class Server {
    public static void main(String[] args) {
        initialize();
        start();
    }

    private static void initialize() {
        //Sql2o sql = new Sql2o(new GenericDatasource("mysql://localhost:3306/si", "root", ""));
        DataBase repo = new JPADataBase();
        ChatContainer chatContainer = new JPAChatContainer();
        ChatServer.initialize(repo, chatContainer);

    }

    public static void start() {
        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.staticFileLocation("/public");
        Spark.port(80);
        Spark.init();
    }
}
