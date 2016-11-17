package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.authentication.AuthenticationService;
import de.chat2u.authentication.Permissions;
import de.chat2u.authentication.UserRepository;
import de.chat2u.model.AuthenticationUser;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created RegisterSteps in de.chat2u.server.cucumber.steps
 * by ARSTULKE on 15.11.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ChatServer.class})
public class RegisterSteps {

    private String response;
    private Exception exception;

    public void initialize() {
        AuthenticationService authenticationService = new AuthenticationService(new UserRepository<>());
        new ChatServer(authenticationService);
    }

    @Gegebenseien("^keine registrierten Benutzer$")
    public void keineRegistriertenBenutzer() throws Throwable {
        initialize();
    }

    @Gegebensei("^der registrierte Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\"$")
    public void derRegistrierteTeilnehmerMitDemPasswort(String username, String password) throws Throwable {
        UserRepository<AuthenticationUser> userRepository = new UserRepository<>();
        userRepository.addUser(new AuthenticationUser(username, password, Permissions.USER));
        new ChatServer(new AuthenticationService(userRepository));
    }

    //region Wenn
    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortRegistriere(String nickname, String password) throws Throwable {
        try {
            response = ChatServer.register(nickname, password);
        } catch (Exception e) {
            exception = e;
            response = e.getMessage();
        }
    }
    //endregion

    @Dann("^wird das Registrieren abgeschlossen mit der Nachricht \"([^\"]*)\"$")
    public void wirdDasRegistrierenGenehmigtMitDerNachricht(String message) throws Throwable {
        Assert.assertThat(response, is(message));
        if (exception != null)
            Assert.assertTrue(exception.getClass().getSuperclass().equals(IllegalArgumentException.class));
    }
}
