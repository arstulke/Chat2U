package de.chat2u.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;

/**
 * Created SendMessageSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class SendMessageSteps {
    @Gegebensei("^ein Chat mit den Teilnehmern \"([^\"]*)\" und \"([^\"]*)\"$")
    public void einChatMitDenTeilnehmernUnd(String arg0, String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wenn("^\"([^\"]*)\" eine Nachricht sendet$")
    public void eineNachrichtSendet(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^soll diese im Chat angezeigt werden.$")
    public void sollDieseImChatAngezeigtWerden() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
