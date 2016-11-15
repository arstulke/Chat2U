package de.chat2u.client.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;
import de.chat2u.client.Client;
import de.chat2u.client.Result;
import de.chat2u.client.Security;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

/**
 * Created RegisterSteps in de.chat2u.client.cucumber.steps
 * by ARSTULKE on 15.11.2016.
 */
public class RegisterSteps {

    private Client client;
    private Result result;

    @Gegebensei("^der Registriertoken \"([^\"]*)\".$")
    public void derRegistriertoken(String token) throws Throwable {
    }

    //region Wenn
    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" mit dem Token \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortMitDemTokrnRegistriere(String nickname, String password, String token) throws Throwable {
        String ip = "127.0.0.1";             //10.250.25.78
        int port = 8080;
        client = new Client(ip, port);
        result = client.sendWithResult("/register " + nickname + " " + Security.createHash(password) + " " + token);
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortRegistriere(String nickname, String password) throws Throwable {
        String ip = "127.0.0.1";             //10.250.25.78
        int port = 8080;
        client = new Client(ip, port);
        result = client.sendWithResult("/register " + nickname + " " + Security.createHash(password));

    }
    //endregion

    //region Dann
    @Dann("^wird das Registrieren genehmigt mit der Nachricht \"([^\"]*)\"$")
    public void wirdDasRegistrierenGenehmigtMitDerNachricht(String message) throws Throwable {
        Assert.assertThat(result, CoreMatchers.is(new Result(200, message)));
    }

    @Dann("^wird das Registrieren verweigert mit der Nachricht \"([^\"]*)\"$")
    public void wirdDasRegistrierenVerweigertMitDerNachricht(String message) throws Throwable {
        Assert.assertThat(result, CoreMatchers.is(new Result(403, message)));
    }
    //endregion
}
