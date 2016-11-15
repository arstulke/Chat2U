package de.chat2u.client.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;

/**
 * Created de.chat2u.client.cucumber.steps.LogoutSteps in PACKAGE_NAME
 * by ARSTULKE on 15.11.2016.
 */
public class LogoutSteps {
    @Gegebensei("^der Teilnehmer \"([^\"]*)\" in der Liste der Teilnehmer, die gerade Online sind.$")
    public void derTeilnehmerInDerListeDerTeilnehmerDieGeradeOnlineSind(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Wenn("^\"([^\"]*)\" sich abmeldet,$")
    public void sichAbmeldet(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^sehe ich \"([^\"]*)\" nicht mehr in der Liste der Teilnehmer, die gerade Online sind.$")
    public void seheIchNichtMehrInDerListeDerTeilnehmerDieGeradeOnlineSind(String arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
