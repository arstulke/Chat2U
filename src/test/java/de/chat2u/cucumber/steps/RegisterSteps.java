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

import static de.chat2u.ChatServer.register;
import static de.chat2u.authentication.Permissions.ADMIN;
import static de.chat2u.authentication.Permissions.MOD;
import static de.chat2u.authentication.Permissions.USER;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created RegisterSteps in de.chat2u.server.cucumber.steps
 * by ARSTULKE on 15.11.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ChatServer.class})
public class RegisterSteps {

    private String response;
    private Exception exception;
    private String token;

    public void initialize(UserRepository<AuthenticationUser> repo) {
        AuthenticationService authenticationService = new AuthenticationService(repo);
        ChatServer.initialize(authenticationService);
    }

    //region Gegeben sei
    @Gegebensei("^der registrierte Teilnehmer \"([^\"]*)\"$")
    public void derRegistrierteTeilnehmerMitDemPasswort(String username) throws Throwable {
        UserRepository<AuthenticationUser> userRepository = new UserRepository<>();
        userRepository.addUser(new AuthenticationUser(username, "geheim", USER));
        initialize(userRepository);
    }

    @Gegebenseien("^keine registrierten Benutzer$")
    public void keineRegistriertenBenutzer() throws Throwable {
        initialize(new UserRepository<>());
    }

    @Gegebensei("^ein zufällig generierter Registriertoken mit \"([^\"]*)\" Berechtigungen$")
    public void einZufälligGenerierterRegistriertokenMitBerechtigungen(String permissions) throws Throwable {
        initialize(new UserRepository<>());
        token = ChatServer.generateToken(getPermissions(permissions));
    }
    //endregion

    //region Wenn
    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortRegistriere(String username, String password) throws Throwable {
        try {
            response = register(username, password);
        } catch (Exception e) {
            exception = e;
            response = e.getMessage();
        }
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" mit dem Token registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortMitDemTokenRegistriere(String username, String password) throws Throwable {
        try {
            response = ChatServer.register(username, password, token);
        } catch (Exception e) {
            exception = e;
            response = e.getMessage();
        }
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" mit einem anderen Token registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortMitEinemAnderenTokenRegistriere(String username, String password) throws Throwable {
        //falscher token
        try {
            response = register(username, password, "abc");
        } catch (Exception e) {
            exception = e;
            response = e.getMessage();
        }
    }
    //endregion

    @Dann("^wird das Registrieren abgeschlossen mit der Nachricht \"([^\"]*)\"$")
    public void wirdDasRegistrierenGenehmigtMitDerNachricht(String message) throws Throwable {
        assertThat(response, is(message));
        if (exception != null)
            Assert.assertTrue(exception.getClass().getSuperclass().equals(IllegalArgumentException.class));
    }

    //region NetworkUtils
    public static Permissions getPermissions(String lvlString) {
        switch (lvlString) {
            case "Administrator":
                return ADMIN;

            case "Teilnehmer":
                return USER;

            case "Moderator":
                return MOD;
        }
        return null;
    }
    //endregion
}
