package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.network.ChatWebSocketHandler;
import spark.Spark;

/**
 * Created Server in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
class Server {
    public static void main(String[] args) {
        initialize();
        start();
    }

    private static void initialize() {
        UserRepository<AuthenticationUser> repo = new UserRepository<>();
        repo.addUser(new AuthenticationUser("Kito", "Test123"));
        repo.addUser(new AuthenticationUser("Arne", "Test123"));
        ChatServer.initialize(new AuthenticationService(repo));
    }

    private static void start() {
        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.staticFileLocation("/public");
        Spark.port(80);
        Spark.init();
    }
}
