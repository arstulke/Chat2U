package de.chat2u.cucumber.selenium.steps;

import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import de.chat2u.cucumber.selenium.SeleniumHelper;
import de.chat2u.model.AuthenticationUser;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static de.chat2u.cucumber.selenium.SeleniumHelper.client;
import static de.chat2u.cucumber.selenium.SeleniumHelper.registerUser;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created SearchUserSteps in de.chat2u.cucumber.selenium.steps
 * by ARSTULKE on 08.12.2016.
 */
public class SearchUserSteps {

    private List<String> userList;
    @Wenn("^\"([^\"]*)\" sich mit dem Passwort \"([^\"]*)\" registriert und einloggt$")
    public void sichMitDemPasswortRegistriertUndEinlogggt(String username, String password) throws Throwable {
        client.put(username, registerUser(new AuthenticationUser(username, password)));
    }

    @Dann("^ist die Anmeldeaufforderung für \"([^\"]*)\" verschwunden$")
    public void istDieAnmeldeaufforderungVerschwunden(String clientName) throws Throwable {
        WebDriverWait wait = new WebDriverWait(client.get(clientName), 2);
        wait.until(ExpectedConditions.invisibilityOfElementLocated((By.id("loginBox"))));
        Assert.assertThat(client.get(clientName).findElement(By.id("loginBox")).getAttribute("style"), is("visibility: hidden; display: none;"));
    }

    @Und("^\"([^\"]*)\" erscheint in der Liste der Benutzer, welche online sind$")
    public void erErscheintInDerListeDerBenutzerWelcheOnlineSind(String username) throws Throwable {
        client.get(username).findElement(By.id("ul_userList")).findElement(By.id("user_" + username.hashCode()));
    }





    @Wenn("^\"([^\"]*)\" nach \"([^\"]*)\" sucht$")
    public void nachSucht(String webdriver, String search) throws Throwable {
        userList = SeleniumHelper.getUserList(webdriver);
        client.get(webdriver).findElement(By.id("searchUser")).clear();
        client.get(webdriver).findElement(By.id("searchUser")).sendKeys(search);
    }

    @Dann("^werden bei \"([^\"]*)\" die Benutzer angezeigt:$")
    public void werdenBeiDieBenutzerAngezeigt(String webDriver, List<String> onlineUsers) throws Throwable {
        for (String username : userList) {
            Assert.assertThat(client.get(webDriver).findElement(By.id("user_" + username)).getAttribute("style"), CoreMatchers.is((onlineUsers.contains(username)) ? "display: block;" : "display: none;"));
        }
    }
}
