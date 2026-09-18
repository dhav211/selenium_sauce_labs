import POM.LoginPageObjectModel;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests {
    private WebDriver driver;
    private LoginPageObjectModel loginPageObjectModel;

    @BeforeEach
    void setup() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        loginPageObjectModel = new LoginPageObjectModel(driver);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void successfullyLoginStandardUser() {
        String inventoryUrl = "https://www.saucedemo.com/inventory.html";

        loginPageObjectModel.loginUser("standard_user", "secret_sauce");
        loginPageObjectModel.waitForInventoryPageToLoad();

        assertThat(driver.getCurrentUrl()).isEqualTo(inventoryUrl);
    }

    @Test
    void successfullyLoginStandardUserAfterIncorrectTry() {
        String inventoryUrl = "https://www.saucedemo.com/inventory.html";

        loginPageObjectModel.loginUser("standard_user", "secret_sauces");
        String errorMessage = loginPageObjectModel.getErrorMessage();
        loginPageObjectModel.closeErrorMessage();
        loginPageObjectModel.clearUsernameAndPasswordFields();
        loginPageObjectModel.loginUser("standard_user", "secret_sauce");
        loginPageObjectModel.waitForInventoryPageToLoad();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
        assertThat(driver.getCurrentUrl()).isEqualTo(inventoryUrl);
    }

    @Test
    void successfullyLoginStandardUserAfterLockedOutUser() {
        String inventoryUrl = "https://www.saucedemo.com/inventory.html";

        loginPageObjectModel.loginUser("locked_out_user", "secret_sauces");
        String errorMessage = loginPageObjectModel.getErrorMessage();
        loginPageObjectModel.closeErrorMessage();
        loginPageObjectModel.clearUsernameAndPasswordFields();
        loginPageObjectModel.loginUser("standard_user", "secret_sauce");
        loginPageObjectModel.waitForInventoryPageToLoad();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
        assertThat(driver.getCurrentUrl()).isEqualTo(inventoryUrl);
    }

    @Test
    void successfullyLoginWithEnterKeyAfterPassword() {
        String inventoryUrl = "https://www.saucedemo.com/inventory.html";

        loginPageObjectModel.loginUserWithEnterKey("standard_user", "secret_sauce");
        loginPageObjectModel.waitForInventoryPageToLoad();

        assertThat(driver.getCurrentUrl()).isEqualTo(inventoryUrl);
    }

    @Test
    void successfullyLoginStandardUserWithDoubleClick() {
        String inventoryUrl = "https://www.saucedemo.com/inventory.html";

        loginPageObjectModel.loginUserWithLoginButtonDoubleClicked("standard_user", "secret_sauce");
        loginPageObjectModel.waitForInventoryPageToLoad();

        assertThat(driver.getCurrentUrl()).isEqualTo(inventoryUrl);
    }

    @Test
    void unsuccessfullyLoginLockedOutUser() {
        loginPageObjectModel.loginUser("locked_out_user", "secret_sauce");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.LOCKED_OUT_ERROR);
    }

    @Test
    void wrongUsernameAndPasswordOnLogin() {
        loginPageObjectModel.loginUser("unstandard_user", "well_known_sauce");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
    }

    @Test
    void missingUsernameAndPassword() {
        loginPageObjectModel.loginUser("", "");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.MISSING_USERNAME);
    }

    @Test
    void missingPassword() {
        loginPageObjectModel.loginUser("standard_user", "");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.MISSING_PASSWORD);
    }

    @Test
    void failureWithLongUsername() {
        String longUserName = "a".repeat(2000);
        loginPageObjectModel.loginUser(longUserName, "secret_sauce");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
    }

    @Test
    void failureWithLongPassword() {
        String longPassword = "a".repeat(2000);
        loginPageObjectModel.loginUser("standard_user", longPassword);
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
    }

    @Test
    void failureWithWhitespaceForUsername() {
        loginPageObjectModel.loginUser(" ", "secret_sauce");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
    }

    @Test
    void failureWithCapitalizedUsername() {
        loginPageObjectModel.loginUser("Standard_User", "secret_sauce");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
    }

    @Test
    void failureWithTrailingSpaceInUsername() {
        loginPageObjectModel.loginUser(" standard_user", "secret_sauce");
        String errorMessage = loginPageObjectModel.getErrorMessage();

        assertThat(errorMessage).isEqualTo(LoginPageObjectModel.NONMATCHING_USERNAME_AND_PASSWORD);
    }

    @Test
    void logoTextIsCorrect() {
        String logoText = loginPageObjectModel.getLogoText();
        assertThat(logoText).isEqualTo("Swag Labs");
    }
}
