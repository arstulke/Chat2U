package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.Permissions;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.authentication.UserRepository;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

/**
 * Created LoginSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class LoginSteps {
    private String response;
    private Exception exception;

    @Gegebensei("^der registrierte Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\".$")
    public void derRegistrierteTeilnehmerMitDemPasswort(String username, String password) throws Throwable {
        UserRepository<AuthenticationUser> userRepository = new UserRepository<>();
        userRepository.addUser(new AuthenticationUser(username, password, Permissions.USER));
        ChatServer.initialize(new AuthenticationService(userRepository));
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\" anmelde,$")
    public void ichMichAlsTeilnehmerMitDemPasswortAnmelde(String username, String password) throws Throwable {
        try{
            response = ChatServer.login(username, password, null);
        }catch (Exception e){
            exception = e;
            response = e.getMessage();
        }
    }

    @Dann("^sehe ich \"([^\"]*)\" in der Liste der Teilnehmer, die gerade Online sind$")
    public void seheIchInDerListeDerTeilnehmerDieGeradeOnlineSind(String username) throws Throwable {
        Assert.assertThat(ChatServer.getOnlineUsers().getUsernameList(), contains(username));
    }

    @Dann("^wird der Zugriff verweigert$")
    public void wirdDerZugriffVerweigert() throws Throwable {
        Assert.assertTrue(exception.getClass().getSuperclass().equals(IllegalArgumentException.class));
    }

    @Und("^die Nachricht \"([^\"]*)\" erscheint.$")
    public void dieNachrichtErscheint(String message) throws Throwable {
        Assert.assertThat(response, is(message));
    }
}