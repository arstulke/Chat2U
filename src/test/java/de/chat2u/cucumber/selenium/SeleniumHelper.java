package de.chat2u.cucumber.selenium;

import de.chat2u.model.AuthenticationUser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * Created SeleniumTest in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
public class SeleniumHelper {
    public static WebDriver loginUser(AuthenticationUser user, WebDriver driver) {

        fillInput(driver, "user", user.getUsername());
        fillInput(driver, "password", user.getPassword());
        clickButton(driver, "login");
        return driver;
    }

    public static WebDriver registerUser(AuthenticationUser user) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost/");
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        clickButton(driver,"header_logintext");

        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("register"))));

        fillInput(driver, "user_register", user.getUsername());
        fillInput(driver, "password_register", user.getPassword());
        fillInput(driver, "password2_register", user.getPassword());
        clickButton(driver, "register");

        return driver;
    }

    private static boolean isAccessable(WebDriver driver, By password_register) {
        try{
            driver.findElement(password_register).sendKeys(Keys.BACK_SPACE);
        }catch (Exception e){
            return false;
        }
        return true;
    }


    public static void fillInput(WebDriver driver, String id, CharSequence value) {
        WebElement input = driver.findElement(By.id(id));
        input.sendKeys(value);
    }

    private static void clickButton(WebDriver driver, String id) {
        WebElement btn = driver.findElement(By.id(id));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", btn);
    }
}
