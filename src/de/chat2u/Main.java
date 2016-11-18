package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.network.ChatWebsocketHandler;
import spark.Spark;

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


        Spark.staticFileLocation("/public");
        Spark.webSocket("/chat", ChatWebsocketHandler.class);
        Spark.port(8080);
        Spark.init();
    }
}
