package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.users.AuthenticationUser;
import org.junit.Assert;

/**
 * Created LogoutSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class LogoutSteps {

    @Gegebensei("^der angemeldete Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\".$")
    public void derAngemeldeteTeilnehmerMitDemPasswort(String username, String password) throws Throwable {
        UserRepository<AuthenticationUser> userRepository = new UserRepository<>();
        userRepository.addUser(new AuthenticationUser(username, password));
        ChatServer.initialize(new AuthenticationService(userRepository));

        ChatServer.login(username, password, null);
    }

    @Wenn("^\"([^\"]*)\" sich abmeldet,$")
    public void sichAbmeldet(String username) throws Throwable {
        ChatServer.logout(username);
    }

    @Dann("^sehe ich \"([^\"]*)\" nicht mehr in der Liste der Teilnehmer, die gerade Online sind.$")
    public void seheIchNichtMehrInDerListeDerTeilnehmerDieGeradeOnlineSind(String username) throws Throwable {
        Assert.assertFalse(ChatServer.getOnlineUsers().getUsernameList().contains(username));
    }
}
