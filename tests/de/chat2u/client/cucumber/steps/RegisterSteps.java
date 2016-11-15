package de.chat2u.client.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;

/**
 * Created RegisterSteps in de.chat2u.client.cucumber.steps
 * by ARSTULKE on 15.11.2016.
 */
public class RegisterSteps {

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortRegistriere(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^wird \"([^\"]*)\" zur Teilnehmerliste hinzugefügt.$")
    public void wirdZurTeilnehmerlisteHinzugefügt(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^wird das Registrieren verweigert$")
    public void wirdDasRegistrierenVerweigert() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Und("^ich den Token \"([^\"]*)\" eingebe$")
    public void ichDenTokenEingebe(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Und("^wird unter \"([^\"]*)\" zur Administratorenliste hinzugefügt$")
    public void wirdUnterZurAdministratorenlisteHinzugefügt(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Gegebensei("^der Registriertoken \"([^\"]*)\".$")
    public void derRegistriertoken(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
