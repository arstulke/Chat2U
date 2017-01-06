package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.cucumber.selenium.OfflineChatContainer;
import de.chat2u.persistence.users.DataBase;
import de.chat2u.cucumber.selenium.OfflineDataBase;
import de.chat2u.model.User;
import org.json.JSONObject;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Created LoginSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class LoginSteps {
    private JSONObject response;

    @Gegebensei("^der registrierte Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\".$")
    public void derRegistrierteTeilnehmerMitDemPasswort(String username, String password) throws Throwable {
        DataBase userRepository = new OfflineDataBase();
        userRepository.addUser(new User(username), password);
        ChatServer.initialize(userRepository, new OfflineChatContainer());
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\" anmelde,$")
    public void ichMichAlsTeilnehmerMitDemPasswortAnmelde(String username, String password) throws Throwable {
        response = ChatServer.login(username, password, null);
    }

    @Dann("^sehe ich \"([^\"]*)\" in der Liste der Teilnehmer, die gerade Online sind$")
    public void seheIchInDerListeDerTeilnehmerDieGeradeOnlineSind(String username) throws Throwable {
        assertThat(ChatServer.getOnlineUsers().getUsernameList(), contains(username));
        ChatServer.logout(username);
    }

    @Dann("^die Nachricht \"([^\"]*)\" erscheint.$")
    public void dieNachrichtErscheint(String message) throws Throwable {
        Assert.assertThat(response.toString(), is("{\"secondData\":\"" + message + "\",\"type\":\"statusLogin\",\"primeData\":false}"));
    }
}