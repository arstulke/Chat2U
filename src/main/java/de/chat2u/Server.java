package de.chat2u;

import de.chat2u.network.ChatWebSocketHandler;
import de.chat2u.persistence.chats.ChatContainer;
import de.chat2u.persistence.chats.JPAChatContainer;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.persistence.users.JPADataBase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import spark.Spark;

/**
 * Created Server in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class Server {
    public static void main(String[] args) {
        /*LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(Level.INFO);
        ctx.updateLoggers();*/

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
