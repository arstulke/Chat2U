package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.network.ChatWebSocketHandler;
import spark.Spark;

import static spark.Spark.get;

/**
 * Created Main in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        UserRepository<AuthenticationUser> repo = new UserRepository<>();
        repo.addUser(new AuthenticationUser("Kito", "Test123", null));
        repo.addUser(new AuthenticationUser("Arne", "Test123", null));
        ChatServer.initialize(new AuthenticationService(repo));

        Spark.webSocket("/chat", ChatWebSocketHandler.class);
        Spark.staticFileLocation("/public");
        Spark.port(80);
        Spark.init();
    }
}
