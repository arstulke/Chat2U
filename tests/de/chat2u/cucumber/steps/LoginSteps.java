package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.Chat;
import de.chat2u.Server;
import de.chat2u.User;
import de.chat2u.UserRepository;
import org.junit.Assert;

import static org.hamcrest.Matchers.contains;

/**
 * Created LoginSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class LoginSteps {
    private Chat chat;
    private User user;

    @Gegebensei("^der registrierte Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\".$")
    public void derRegistrierteTeilnehmerMitDemPasswort(String username, String password) throws Throwable {
        User u = new User(username, password);
        UserRepository userRepository = new UserRepository();
        userRepository.addUser(u);
        this.user = u;

        chat = new Chat(userRepository);

        Server.start();
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\" anmelde,$")
    public void ichMichAlsTeilnehmerMitDemPasswortAnmelde(String username, String password) throws Throwable {
        chat.login(username, password);
    }

    @Dann("^sehe ich \"([^\"]*)\" in der Liste der Teilnehmer, die gerade Online sind$")
    public void seheIchInDerListeDerTeilnehmerDieGeradeOnlineSind(String username) throws Throwable {
        Assert.assertThat(chat.getOnlineUsers(), contains(user));
    }

    @Dann("^wird der Zugriff verweigert$")
    public void wirdDerZugriffVerweigert() throws Throwable {

    }

    @Und("^die Nachricht \"([^\"]*)\" erscheint.$")
    public void dieNachrichtErscheint(String message) throws Throwable {

    }
}