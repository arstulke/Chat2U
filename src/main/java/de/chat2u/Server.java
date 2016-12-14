package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.users.AuthenticationUser;
import de.chat2u.network.ChatWebSocketHandler;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
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
        UserRepository<AuthenticationUser> repo = new UserRepository<>();
        repo.addUser(new AuthenticationUser("Kito", "Test123"));
        repo.addUser(new AuthenticationUser("Arne", "Test123"));
        ChatServer.initialize(new AuthenticationService(repo));
    }

    public static void start() {
        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.staticFileLocation("/public");
        Spark.port(80);
        Spark.init();
    }
}
