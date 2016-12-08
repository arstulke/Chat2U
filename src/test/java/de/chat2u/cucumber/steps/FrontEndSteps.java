package de.chat2u.cucumber.steps;

import com.google.common.base.Function;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.de.*;
import de.chat2u.ChatServer;
import de.chat2u.Server;
import de.chat2u.cucumber.selenium.TestServer;
import de.chat2u.model.AuthenticationUser;
import de.chat2u.model.User;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import static de.chat2u.cucumber.selenium.SeleniumHelper.registerUser;
import static de.chat2u.cucumber.selenium.TestServer.client;
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
        ChatServer.initialize(TestServer.authenticationService);
        Server.start();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }

    @Wenn("^\"([^\"]*)\" sich mit dem Passwort \"([^\"]*)\" registriert und einlogggt$")
    public void sichMitDemPasswortRegistriertUndEinlogggt(String username, String password) throws Throwable {
        client.put(username, registerUser(new AuthenticationUser(username, password)));
    }

    @Dann("^ist die Anmeldeaufforderung für \"([^\"]*)\" verschwunden$")
    public void istDieAnmeldeaufforderungVerschwunden(String clientName) throws Throwable {
        WebDriverWait wait = new WebDriverWait(client.get(clientName), 2);
        wait.until(ExpectedConditions.invisibilityOfElementLocated((By.id("loginBox"))));
        Assert.assertThat(client.get(clientName).findElement(By.id("loginBox")).getAttribute("style"), is("visibility: hidden; cursor: auto;"));
    }

    @Und("^er erscheint in der Liste der Benutzer, welche online sind$")
    public void erErscheintInDerListeDerBenutzerWelcheOnlineSind() throws Throwable {
        client.firstEntry().getValue().findElement(By.id("ul_userList")).findElement(By.id("user_" + user1.getUsername()));
    }

    @Gegebenseien("^die angemeldeten Benutzer$")
    public void dieAngemeldetenBenutzer(List<String> loggedInUsers) throws Throwable {
        for (String username : loggedInUsers) {
            ChatServer.register(username, "secret");
            ChatServer.login(username, "secret", getMockSession());
        }
    }

    @Wenn("^\"([^\"]*)\" nach \"([^\"]*)\" sucht$")
    public void nachSucht(String webdriver, String search) throws Throwable {
        client.get(webdriver).findElement(By.id("searchUser")).clear();
        client.get(webdriver).findElement(By.id("searchUser")).sendKeys(search);
    }

    @Dann("^werden bei \"([^\"]*)\" die Benutzer angezeigt:$")
    public void werdenBeiDieBenutzerAngezeigt(String webDriver, List<String> onlineUsers) throws Throwable {
        for (User user : ChatServer.getOnlineUsers()) {
            Assert.assertThat(client.get(webDriver).findElement(By.id("user_" + user.getUsername())).getAttribute("style"), CoreMatchers.is((onlineUsers.contains(user.getUsername())) ? "display: block;" : "display: none;"));
        }
    }

    @Wenn("^\"([^\"]*)\" die Nachricht an alle \"([^\"]*)\" sendet$")
    public void dieNachrichtAnAlleSendet(String webdriver, String msg) throws Throwable {
        client.get(webdriver).findElement(By.id("chatMessage")).clear();
        client.get(webdriver).findElement(By.id("chatMessage")).sendKeys(msg);
        client.get(webdriver).findElement(By.id("chatSendMessage")).click();

        WebDriverWait wait = new WebDriverWait(client.get(webdriver), 10);
        wait.until(new Function<WebDriver, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver webDriver) {
                return client.get(webdriver).findElement(By.id("chatContainer")).getText().contains(msg);
            }
        });
    }

    @Dann("^wird bei \"([^\"]*)\" die Nachricht \"([^\"]*)\" erscheinen$")
    public void wirdBeiDieNachrichtErscheinen(String webdriver, String message) throws Throwable {
        Assert.assertThat(client.get(webdriver).findElement(By.id("chatContainer")).getText(), Matchers.containsString(message));
    }

    @After("@CleanSelenium")
    public void afterFeature() {
        Collection<WebDriver> c = client.values();
        c.forEach(WebDriver::close);
    }
}
