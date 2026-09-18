package POM;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;

public class LoginPageObjectModel {
    final private WebDriver driver;
    final private WebDriverWait wait;

    private final By logoTextBy = By.className("login_logo");
    private final By loginFieldBy = By.id("user-name");
    private final By passwordFieldBy = By.id("password");
    private final By loginButtonBy = By.id("login-button");
    private final By errorMessageBy = By.cssSelector("[data-test='error']");
    private final By closeErrorMessageBy = By.cssSelector("[data-test='error-button']");

    public static final String LOCKED_OUT_ERROR = "Epic sadface: Sorry, this user has been locked out.";
    public static final String NONMATCHING_USERNAME_AND_PASSWORD = "Epic sadface: Username and password do not match any user in this service";
    public static final String MISSING_USERNAME = "Epic sadface: Username is required";
    public static final String MISSING_PASSWORD = "Epic sadface: Password is required";

    public LoginPageObjectModel(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
    }

    public void loginUser(String username, String password) {
        driver.findElement(loginFieldBy).sendKeys(username);
        driver.findElement(passwordFieldBy).sendKeys(password);
        driver.findElement(loginButtonBy).click();
    }

    public void loginUserWithEnterKey(String username, String password) {
        driver.findElement(loginFieldBy).sendKeys(username);
        driver.findElement(passwordFieldBy).sendKeys(password + Keys.ENTER);
    }

    public void loginUserWithLoginButtonDoubleClicked(String username, String password) {
        driver.findElement(loginFieldBy).sendKeys(username);
        driver.findElement(passwordFieldBy).sendKeys(password);
        new Actions(driver)
                .doubleClick(driver.findElement(loginButtonBy))
                .perform();
    }

    public void clearUsernameAndPasswordFields() {
        driver.findElement(loginFieldBy).clear();
        driver.findElement(passwordFieldBy).clear();
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageBy));
        WebElement errorElement = driver.findElement(errorMessageBy);
        return errorElement.getText();
    }

    public void closeErrorMessage() {
        WebElement errorMessage = driver.findElement(errorMessageBy);
        driver.findElement(closeErrorMessageBy).click();
        wait.until(ExpectedConditions.invisibilityOf(errorMessage));
    }

    public void waitForInventoryPageToLoad() {
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/inventory.html"));
    }

    public String getLogoText() {
        return driver.findElement(logoTextBy).getText();
    }
}
