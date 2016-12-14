package de.chat2u.cucumber.selenium;

import com.google.common.base.Function;
import de.chat2u.model.AuthenticationUser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created SeleniumTest in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
public class SeleniumHelper {
    public static Map<String, WebDriver> client = new ConcurrentHashMap<>();

    public static WebDriver loginUser(AuthenticationUser user, WebDriver driver) {
        fillInput(driver, "user", user.getUsername());
        fillInput(driver, "password", user.getPassword());
        clickButton(driver, "login");
        return driver;
    }

    public static WebDriver registerUser(AuthenticationUser user) {
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost/");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        clickButton(driver, "loginPanelHead");

        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("register"))));

        fillInput(driver, "registerUsername", user.getUsername());
        fillInput(driver, "registerPassword", user.getPassword());
        fillInput(driver, "registerSecPassword", user.getPassword());
        clickButton(driver, "register");

        wait = new WebDriverWait(driver, 10);
        wait.until(new Function<WebDriver, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable WebDriver input) {
                return input.findElement(By.id("popup")).getCssValue("visibility").equals("hidden");
            }
        });

        return driver;
    }


    public static void fillInput(WebDriver driver, String id, CharSequence value) {
        WebElement input = driver.findElement(By.id(id));
        input.clear();
        input.sendKeys(value);
    }

    private static void clickButton(WebDriver driver, String id) {
        WebElement btn = driver.findElement(By.id(id));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", btn);
    }

    public static List<String> getUserList(String webdriver) {
        List<String> userList = new ArrayList<>();
        List<WebElement> webElementList = client.get(webdriver).findElements(By.xpath("//*[@id=\"ul_userList\"]/li"));
        webElementList.forEach(webElement -> userList.add(webElement.getAttribute("username")));

        return userList;
    }

    public static String getChatID(WebElement webElement) {
        String onclick = webElement.getAttribute("onclick");
        String before = "tabManager.openTab(event.currentTarget, '";
        return onclick.substring(onclick.indexOf(before) + before.length(), onclick.indexOf("')"));
    }

    public static void sendMessage(String webdriver, String message) {
        client.get(webdriver).findElement(By.id("chatMessage")).clear();
        client.get(webdriver).findElement(By.id("chatMessage")).sendKeys(message);
        client.get(webdriver).findElement(By.id("chatSendMessage")).click();
    }
}
