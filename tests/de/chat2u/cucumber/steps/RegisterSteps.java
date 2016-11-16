package de.chat2u.cucumber.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebensei;
import cucumber.api.java.de.Wenn;

/**
 * Created RegisterSteps in de.chat2u.server.cucumber.steps
 * by ARSTULKE on 15.11.2016.
 */
public class RegisterSteps {

    @Gegebensei("^der Registriertoken \"([^\"]*)\".$")
    public void derRegistriertoken(String token) throws Throwable {
    }

    //region Wenn
    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" mit dem Token \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortMitDemTokrnRegistriere(String nickname, String password, String token) throws Throwable {
    }

    @Wenn("^ich mich als Teilnehmer \"([^\"]*)\" und dem Passwort \"([^\"]*)\" registriere,$")
    public void ichMichAlsTeilnehmerUndDemPasswortRegistriere(String nickname, String password) throws Throwable {

    }
    //endregion

    //region Dann
    @Dann("^wird das Registrieren genehmigt mit der Nachricht \"([^\"]*)\"$")
    public void wirdDasRegistrierenGenehmigtMitDerNachricht(String message) throws Throwable {

    }

    @Dann("^wird das Registrieren verweigert mit der Nachricht \"([^\"]*)\"$")
    public void wirdDasRegistrierenVerweigertMitDerNachricht(String message) throws Throwable {

    }
    //endregion
}
