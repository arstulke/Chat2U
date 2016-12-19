package de.chat2u;

import de.chat2u.persistence.chats.OnlineChatContainer;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.persistence.users.OnlineDataBase;
import de.chat2u.network.ChatWebSocketHandler;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.sql2o.GenericDatasource;
import org.sql2o.Sql2o;
import spark.Spark;

/**
 * Created Server in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class Server {
    public static void main(String[] args) {
        try {
            switch (args[0]) {
                case "debug":
                    LogManager.getRootLogger().setLevel(Level.DEBUG);
                    break;
                case "error":
                    LogManager.getRootLogger().setLevel(Level.ERROR);
                    break;
                case "info":
                default:
                    LogManager.getRootLogger().setLevel(Level.INFO);
                    break;
            }
        } catch (Exception ignore) {
            LogManager.getRootLogger().setLevel(Level.INFO);
        }
        initialize();
        start();
    }

    private static void initialize() {
        Sql2o sql = new Sql2o(new GenericDatasource("mysql://localhost:3306/si", "root", ""));
        DataBase repo = new OnlineDataBase(sql);
        OnlineChatContainer chatContainer = new OnlineChatContainer(sql);
        ChatServer.initialize(repo, chatContainer);
    }

    public static void start() {
        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.staticFileLocation("/public");
        Spark.port(80);
        Spark.init();
    }
}
