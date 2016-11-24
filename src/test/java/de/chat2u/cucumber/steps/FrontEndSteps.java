package de.chat2u.cucumber.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Gegebenseien;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.ChatServer;
import de.chat2u.cucumber.selenium.TestServer;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Collection;
import java.util.List;

import static de.chat2u.cucumber.selenium.SeleniumHelper.registerUser;
import static de.chat2u.cucumber.selenium.TestServer.getMockSession;
import static de.chat2u.cucumber.selenium.TestServer.user1;
import static org.hamcrest.CoreMatchers.is;

/**
 * Chat2U:
 * * PACKAGE_NAME:
 * * * Created by KAABERT on 24.11.2016.
 */
public class FrontEndSteps {

    @Before("@startSzenario")
    public void beforeScenario() {

        TestServer.initialize();
        TestServer.start();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }

    @Wenn("^\"([^\"]*)\" sich mit dem Passwort \"([^\"]*)\" registriert und einlogggt$")
    public void sichMitDemPasswortRegistriertUndEinlogggt(String username, String password) throws Throwable {
        TestServer.client.put(username,registerUser(new AuthenticationUser(username, password)));
    }

    @Dann("^ist die Anmeldeaufforderung für \"([^\"]*)\" verschwunden$")
    public void istDieAnmeldeaufforderungVerschwunden(String client) throws Throwable {
        WebDriverWait wait = new WebDriverWait(TestServer.client.get(client), 2);
        wait.until(ExpectedConditions.invisibilityOfElementLocated((By.id("popupbox"))));
        Assert.assertThat(TestServer.client.get(client).findElement(By.id("popupbox")).getAttribute("style"), is("visibility: hidden; position: fixed;"));
    }

    @Und("^er erscheint in der Liste der Benutzer, welche online sind$")
    public void erErscheintInDerListeDerBenutzerWelcheOnlineSind() throws Throwable {
        TestServer.client.firstEntry().getValue().findElement(By.id("userlist")).findElement(By.id("user_" + user1.getUsername()));
    }

    @Gegebenseien("^die angemeldeten Benutzer$")
    public void dieAngemeldetenBenutzer(List<String> loggedInUsers) throws Throwable {
        for (String username : loggedInUsers) {
            try {
                ChatServer.register(username, "geheim");
                ChatServer.login(username, "geheim", getMockSession());
            } catch (Exception ignore){

            }
        }
    }

    @Wenn("^\"([^\"]*)\" nach \"([^\"]*)\" sucht$")
    public void nachSucht(String webdriver, String search) throws Throwable {
        TestServer.client.get(webdriver).findElement(By.id("search")).clear();
        TestServer.client.get(webdriver).findElement(By.id("search")).sendKeys(search);
    }

    @Dann("^werden bei \"([^\"]*)\" die Benutzer angezeigt:$")
    public void werdenBeiDieBenutzerAngezeigt(String webdriver, List<String> onlineUsers) throws Throwable {
        for(User user : ChatServer.getOnlineUsers()) {
            Assert.assertThat(TestServer.client.get(webdriver).findElement(By.id("user_" + user.getUsername())).getAttribute("style"), CoreMatchers.is((onlineUsers.contains(user.getUsername()))?"display: block;":"display: none;"));
        }
    }

    @After("@endSzenario")
    public void AfterFeature() {
        Collection<WebDriver> c = TestServer.client.values();
        c.forEach(WebDriver::close);
    }
}
