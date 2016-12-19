package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.cucumber.selenium.OfflineChatContainer;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.cucumber.selenium.OfflineDataBase;
import de.chat2u.model.users.User;
import org.json.JSONObject;

import static de.chat2u.ChatServer.register;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created RegisterSteps in de.chat2u.server.cucumber.steps
 * by ARSTULKE on 15.11.2016.
 */
public class RegisterSteps {

    private JSONObject response;

    private void initialize(DataBase repo) {
        ChatServer.initialize(repo, new OfflineChatContainer());
    }

    //region Gegeben sei
    @Gegebensei("^der registrierte Teilnehmer \"([^\"]*)\"$")
    public void derRegistrierteTeilnehmerMitDemPasswort(String username) throws Throwable {
        DataBase userRepository = new OfflineDataBase();
        userRepository.addUser(new User(username), "geheim");
        initialize(userRepository);
    }

    @Gegebenseien("^keine registrierten Benutzer$")
    public void keineRegistriertenBenutzer() throws Throwable {
        initialize(new OfflineDataBase());
    }
    //endregion

    //region Wenn
    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortRegistriere(String username, String password) throws Throwable {
        response = register(username, password);
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" mit dem Token registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortMitDemTokenRegistriere(String username, String password) throws Throwable {
        response = ChatServer.register(username, password);
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" mit einem anderen Token registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortMitEinemAnderenTokenRegistriere(String username, String password) throws Throwable {
        response = register(username, password);
    }
    //endregion

    @Dann("^wird das Registrieren abgeschlossen$")
    public void wirdDasRegistrierenAbgeschlossen() throws Throwable {
        assertThat(response.toString(), is("{\"type\":\"statusRegister\",\"primeData\":true}"));
    }

    @Dann("^wird das Registrieren abgeschlossen mit der Nachricht \"([^\"]*)\"$")
    public void wirdDasRegistrierenGenehmigtMitDerNachricht(String message) throws Throwable {
        assertThat(response.toString(), is("{\"secondData\":\"" + message + "\",\"type\":\"statusRegister\",\"primeData\":false}"));
    }

    //endregion
}
