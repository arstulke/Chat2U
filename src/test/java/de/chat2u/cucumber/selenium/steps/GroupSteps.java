package de.chat2u.cucumber.selenium.steps;

import com.google.common.base.Function;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Und;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static de.chat2u.cucumber.selenium.SeleniumHelper.client;
import static de.chat2u.cucumber.selenium.SeleniumHelper.fillInput;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Chat2U:
 * * de.chat2u.cucumber.selenium.steps:
 * * * Created by KAABERT on 13.12.2016.
 */
public class GroupSteps {
    @Und("^\"([^\"]*)\" eine Gruppe mit den Namen \"([^\"]*)\" erstellt und die Teilnehmer einlädt:$")
    public void eineGruppeMitDenNamenErstelltUndDieTeilnehmerEinlädt(String username, String groupName, List<String> groupUsers) {
        WebDriver webDriver = client.get(username);

        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("$(\"#addGroup\").trigger(\"click\")");

        fillInput(webDriver, "groupName", groupName);
        groupUsers.forEach(groupUser -> checkUserByName(webDriver, groupUser));

        webDriver.findElement(By.id("createGroup")).click();
    }

    private void checkUserByName(WebDriver webDriver, String groupUser) {
        List<WebElement> checkableUsers = webDriver.findElements(By.xpath("//*[@id=\"groupUsers\"]/li/input"));
        checkableUsers.forEach(webElement -> {
            String value = webElement.getAttribute("value");
            if(value.equals(groupUser) && !webElement.isSelected())
                webElement.click();
        });
    }

    @Dann("^verschwindet das Fenster bei \"([^\"]*)\" zum Erstellen einer Gruppe nicht$")
    public void verschwindetDasFensterBeiZumErstellenEinerGruppeNicht(String username) throws Throwable {
        assertThat(client.get(username).findElement(By.id("popup")).getCssValue("visibility"), is("visible"));
    }

    @Dann("^verschwindet das Fenster bei \"([^\"]*)\" zum Erstellen einer Gruppe$")
    public void verschwindetDasFensterBeiZumErstellenEinerGruppe(String username) throws Throwable {
        WebDriverWait wait = new WebDriverWait(client.get(username), 10);
        wait.until(new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                WebElement element = client.get(username).findElement(By.id("popup"));
                return element.getCssValue("visibility").equals("hidden");
            }
        });
    }

    @Und("^es erscheint bei \"([^\"]*)\" die Nachricht \"([^\"]*)\"$")
    public void esErscheintBeiDieNachricht(String username, String msg) throws Throwable {
        WebDriver webDriver = client.get(username);
        assertThat(webDriver.findElement(By.id("createGroupBoxAlert")).getText(), is(msg));
        client.get(username).findElement(By.id("cancelCreateGroup")).click();
    }
}
