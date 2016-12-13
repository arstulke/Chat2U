package de.chat2u.cucumber.selenium.steps;

import com.google.common.base.Function;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.de.Dann;
import cucumber.api.java.de.Und;
import cucumber.api.java.de.Wenn;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import static de.chat2u.cucumber.selenium.SeleniumHelper.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * Chat2U:
 * * PACKAGE_NAME:
 * * * Created by KAABERT on 24.11.2016.
 */
public class SendMessageSteps {

    private String currentChatID;

    @Wenn("^\"([^\"]*)\" die Nachricht \"([^\"]*)\" an alle sendet$")
    public void dieNachrichtAnAlleSendet(String webdriver, String msg) throws Throwable {
        sendMessage(webdriver, msg);

        currentChatID = "global";
        WebDriverWait wait = new WebDriverWait(client.get(webdriver), 10);
        wait.until(new Function<WebDriver, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver webDriver) {
                return client.get(webdriver).findElement(By.id("global")).getText().contains(msg);
            }
        });
    }

    @Dann("^wird bei \"([^\"]*)\" die Nachricht \"([^\"]*)\" erscheinen$")
    public void wirdBeiDieNachrichtErscheinen(String webdriver, String message) throws Throwable {
        JavascriptExecutor js = (JavascriptExecutor) client.get(webdriver);
        js.executeScript("tabManager.openTab(null, '" + currentChatID + "')");
        Assert.assertThat(client.get(webdriver).findElement(By.id(currentChatID)).getText(), Matchers.containsString(message));
    }

    @Before("@WaitForServer")
    public void waitForServer() throws InterruptedException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL("http://localhost/").openStream()));
            br.readLine();
        } catch (IOException e) {
            Thread.sleep(100);
            waitForServer();
        }
    }

    @After("@CleanSeleniumAfterThat")
    public void afterFeature() {
        client.forEach((name, driver) -> {
            driver.close();
            client.remove(name);
        });
    }

    @Und("^\"([^\"]*)\" die Nachricht \"([^\"]*)\" an \"([^\"]*)\" sendet$")
    public void dieNachrichtAnSendet(String username, String message, String toUser) throws Throwable {
        final WebDriver webDriver = client.get(username);
        webDriver.findElement(By.id("user_" + toUser)).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 10);

        Alert alert = webDriver.switchTo().alert();
        String chatName = "Test123";
        alert.sendKeys(chatName);
        alert.accept();

        wait.until((Function<? super WebDriver, Boolean>) webDriver1 ->
                webDriver1.findElements(By.xpath("//*[@id=\"tabContainer\"]/li/a")).size() >= 2);

        assertCreatedChat(webDriver, chatName);


        List<WebElement> children = webDriver.findElements(By.xpath("//*[@id=\"tabContainer\"]/li/a"));
        children.get(1).click();
        String chatID = getChatID(children.get(1));
        currentChatID = chatID;
        sendMessage(username, message);

        wait.until(new Function<WebDriver, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver webDriver) {
                return webDriver.findElement(By.id(chatID)).getText().contains(message);
            }
        });
    }

    private void assertCreatedChat(WebDriver webDriver, String name) {
        List<WebElement> links = webDriver.findElements(By.xpath("//*[@id=\"tabContainer\"]/li/a"));
        boolean contained = false;
        for(WebElement element : links){
            contained = contained || element.getText().contains(name);
        }
        if(!contained)
            Assert.fail("The tabContainer does not contains a tabLink with the give name.");
    }
}
