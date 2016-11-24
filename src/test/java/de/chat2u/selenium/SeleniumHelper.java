package de.chat2u.selenium;

import de.chat2u.model.AuthenticationUser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created SeleniumTest in de.chat2u.junit
 * by ARSTULKE on 23.11.2016.
 */
class SeleniumHelper {
    WebDriver loginUser(AuthenticationUser user) {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost/");

        fillInput(driver, "user", user.getUsername());
        fillInput(driver, "password", user.getPassword());
        clickButton(driver, "login");
        return driver;
    }

    void fillInput(WebDriver driver, String id, CharSequence value) {
        WebElement input = driver.findElement(By.id(id));
        input.sendKeys(value);
    }

    private void clickButton(WebDriver driver, String id) {
        WebElement btn = driver.findElement(By.id(id));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", btn);
    }
}
