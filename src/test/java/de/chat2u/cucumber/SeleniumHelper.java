package de.chat2u.cucumber;

import de.chat2u.model.AuthenticationUser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created SeleniumTest in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
public class SeleniumHelper {
    public static WebDriver loginUser(AuthenticationUser user) {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost/");

        fillInput(driver, "user", user.getUsername());
        fillInput(driver, "password", user.getPassword());
        clickButton(driver, "login");
        return driver;
    }

    public static WebDriver registerUser(AuthenticationUser user) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost/");
        clickButton(driver,"header_logintext");
        while (!isAccessable(driver, By.id("user_register")) && !isAccessable(driver, By.id("password_register")) && !isAccessable(driver, By.id("password2_register")))
        {

        }
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
