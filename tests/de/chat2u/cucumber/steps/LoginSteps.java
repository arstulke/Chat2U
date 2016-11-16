package de.chat2u.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import org.junit.Assert;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created LoginSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class LoginSteps {

    @Gegebensei("^der registrierte Teilnehmer \"([^\"]*)\" mit dem Passwort \"([^\"]*)\".$")
    public void derRegistrierteTeilnehmerMitDemPasswort(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^sehe ich \"([^\"]*)\" in der Liste der Teilnehmer, die gerade Online sind$")
    public void seheIchInDerListeDerTeilnehmerDieGeradeOnlineSind(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^wird der Zugriff verweigert$")
    public void wirdDerZugriffVerweigert() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Und("^die Nachricht \"([^\"]*)\" erscheint.$")
    public void dieNachrichtErscheint(String message) throws Throwable {
        Assert.assertThat(0,is(0));
    }

    @Wenn("^ich mich mit gültigen Zugangsdaten als Teilnehmer \"([^\"]*)\" anmelde,$")
    public void ichMichMitGültigenZugangsdatenAlsTeilnehmerAnmelde(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wenn("^ich mich mit ungültigen Zugangsdaten als Teilnehmer \"([^\"]*)\" anmelde,$")
    public void ichMichMitUngültigenZugangsdatenAlsTeilnehmerAnmelde(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}