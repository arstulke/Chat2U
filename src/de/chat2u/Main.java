package de.chat2u;

import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;

/**
 * Created Main in de.chat2u
 * by ARSTULKE on 17.11.2016.
 */
public class Main {
    public static void main(String[] args) {
        new ChatServer(new AuthenticationService(new UserRepository<>()));
        /*
        spark Server
        * */
    }
}
