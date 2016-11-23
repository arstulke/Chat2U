package de.chat2u.junit.selenium;

import de.chat2u.Server;
import de.chat2u.TestServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static de.chat2u.TestServer.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created SearchUser in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
public class SearchUser extends SeleniumTest {

    private WebDriver carsten;
    private WebDriver marianne;
    private WebDriver thorsten;

    private WebElement userlist_user_carsten;
    private WebElement userlist_user_marianne;
    private WebElement userlist_user_thorsten;

    @Before
    public void initialize() {
        TestServer.initialize();
        Server.start();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
    }

    @Test
    public void searchUserInBar() throws InterruptedException {
        //given
        carsten = loginUser(user1);
        marianne = loginUser(user2);
        thorsten = loginUser(user3);


        WebElement userlist = carsten.findElement(By.id("userlist"));
        userlist_user_carsten = userlist.findElement(By.id("user_" + user1.getUsername()));
        userlist_user_marianne = userlist.findElement(By.id("user_" + user2.getUsername()));
        userlist_user_thorsten = userlist.findElement(By.id("user_" + user3.getUsername()));
        //when/then
        assertDisplay(true, true, true);
        fillInput(carsten, "search", "T");
        assertDisplay(false, false, true);
        fillInput(carsten, "search", Keys.BACK_SPACE);
        assertDisplay(true, true, true);
        fillInput(carsten, "search", "P");
        assertDisplay(false, false, false);
    }

    private void assertDisplay(boolean... display) {
        Assert.assertThat(userlist_user_carsten.getAttribute("style"), is("display: " + (display[0] ? "block" : "none") + ";"));
        Assert.assertThat(userlist_user_marianne.getAttribute("style"), is("display: " + (display[1] ? "block" : "none") + ";"));
        Assert.assertThat(userlist_user_thorsten.getAttribute("style"), is("display: " + (display[2] ? "block" : "none") + ";"));
    }

    @After
    public void close() {
        carsten.quit();
        marianne.quit();
        thorsten.quit();
    }
}
