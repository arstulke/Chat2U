package de.chat2u.cucumber.steps;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.TestServer;
import de.chat2u.model.AuthenticationUser;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static de.chat2u.TestServer.getMockSession;
import static de.chat2u.TestServer.user1;
import static de.chat2u.cucumber.SeleniumHelper.registerUser;
import static org.hamcrest.CoreMatchers.is;

/**
 * Chat2U:
 * * PACKAGE_NAME:
 * * * Created by KAABERT on 24.11.2016.
 */
public class FrontEndSteps {

    private WebDriver mainUser;

    @Before("@startSzenario")
    public void beforeScenario() {
        TestServer.initialize();
        TestServer.start();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }

    @Wenn("^\"([^\"]*)\" sich mit dem Passwort \"([^\"]*)\" registriert und einlogggt$")
    public void sichMitDemPasswortRegistriertUndEinlogggt(String username, String password) throws Throwable {
        mainUser = registerUser(new AuthenticationUser(username, password));
    }

    @Dann("^ist die Anmeldeaufforderung verschwunden$")
    public void istDieAnmeldeaufforderungVerschwunden() throws Throwable {
        Assert.assertThat(mainUser.findElement(By.id("popupbox")).getAttribute("style"), is("visibility: hidden; position: fixed;"));
    }

    @Und("^er erscheint in der Liste der Benutzer, welche online sind$")
    public void erErscheintInDerListeDerBenutzerWelcheOnlineSind() throws Throwable {
        mainUser.findElement(By.id("userlist")).findElement(By.id("user_" + user1.getUsername()));
    }

    @Gegebenseien("^die angemeldeten Benutzer$")
    public void dieAngemeldetenBenutzer(List<String> loggedInUsers) throws Throwable {
        for(String username: loggedInUsers){
            ChatServer.register(username,"geheim");
            ChatServer.login(username,"geheim",getMockSession());
        }
    }

    @Wenn("^\"([^\"]*)\" nach \"([^\"]*)\" sucht$")
    public void nachSucht(String webdriver, String search) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Dann("^werden bei \"([^\"]*)\" die Benutzer angezeigt:$")
    public void werdenBeiDieBenutzerAngezeigt(String webdriver, List<String> onlineUsers) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
