package com.example.backend.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assumptions;

import static org.assertj.core.api.Assertions.assertThat;

/*
 Simple E2E: sign up → login → add/edit/delete transaction → delete account → login fails.
 Needs frontend at http://localhost:5173 and backend at http://localhost:8081.
*/
public class E2E_WholeApp_SignUp_LogIn_AddTransaction_EditTransaction_DeleteTransaction_DeleteAccount_FailToLogInAgain {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String FRONTEND_URL = "http://localhost:5173";
    private static final int WAIT_TIMEOUT_SECONDS = 25;

    @BeforeAll
    static void setupWebDriver() {
        // setup ChromeDriver
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        // Skip E2E in CI to avoid failing builds when services aren't running
        boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));
        Assumptions.assumeFalse(isCI, "Skipping E2E in CI environment");

        // start Chrome (headless if HEADLESS=true)
        boolean headless = isCI || "true".equalsIgnoreCase(System.getenv("HEADLESS"));
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-gpu", "--no-sandbox", "--window-size=1920,1080");
        options.setAcceptInsecureCerts(true);
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
        driver.manage().window().maximize();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void userJourney_signup_login_create_delete_transaction_delete_account() {
        // unique creds for test
        String uniqueUsername = "e2e_user_" + System.currentTimeMillis();
        String password = "TestPass123!";

        // register
        driver.get(FRONTEND_URL + "/auth");

        // open Register tab if needed
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(text(), 'Login')] | //button[contains(text(), 'Register')]")
        ));
        clickIfPresent(By.xpath("//button[contains(text(), 'Register')]"));

        // fill form
        WebElement usernameInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("username"))
        );
        WebElement passwordInput = driver.findElement(By.name("password"));
        WebElement confirmInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("confirm"))
        );
        WebElement createAccountButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("form.form button.primary"))
        );

        usernameInput.sendKeys(uniqueUsername);
        passwordInput.sendKeys(password);
        confirmInput.sendKeys(password);
        createAccountButton.click();

        // wait for success
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(text(), 'Account created')]")
        ));

        // login
        clickIfPresent(By.xpath("//button[contains(text(), 'Login')]"));

        WebElement loginUsernameInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("username"))
        );
        WebElement loginPasswordInput = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("form.form button.primary"));

        loginUsernameInput.clear();
        loginUsernameInput.sendKeys(uniqueUsername);
        loginPasswordInput.clear();
        loginPasswordInput.sendKeys(password);
        loginButton.click();

        // wait for localStorage auth
        wait.until(driver1 -> {
            try {
                Object u = ((JavascriptExecutor) driver1).executeScript("return window.localStorage.getItem('username')");
                Object uid = ((JavascriptExecutor) driver1).executeScript("return window.localStorage.getItem('userId')");
                return u != null && uid != null;
            } catch (Exception e) {
                return false;
            }
        });

        // go to add transaction
        driver.get(FRONTEND_URL + "/add-transaction");

        // wait for form
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//h2[contains(@class,'card-title') and contains(text(),'Add Transaction')]")
        ));

        // fill transaction form
        WebElement expenseRadio = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("input[name='transaction-type'][value='expense']")
        ));
        if (!expenseRadio.isSelected()) expenseRadio.click();

        // amount
        WebElement amountInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("amount")));
        amountInput.clear();
        amountInput.sendKeys("99.99");
        assertThat(amountInput.getDomProperty("value")).isEqualTo("99.99");

        // category
        WebElement categoryButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("category")));
        categoryButton.click();
        WebElement firstCategory = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//ul[@id='category-panel']//button[contains(@class,'menu-item') and not(@aria-disabled='true')][1]")
        ));
        firstCategory.click();
        assertThat(categoryButton.getText()).doesNotContain("Select a category");

        // description
        WebElement descriptionTextarea = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("description")));
        descriptionTextarea.sendKeys("E2E Test Purchase");
        assertThat(descriptionTextarea.getDomProperty("value")).isEqualTo("E2E Test Purchase");

        // submit
        WebElement addTransactionButton = driver.findElement(By.cssSelector("button.btn-primary"));
        addTransactionButton.click();

        // optional toast: no-op if not present
        List<WebElement> toasts = driver.findElements(By.cssSelector(".toast"));
        if (!toasts.isEmpty()) {
            assertThat(toasts.get(0).isDisplayed()).isTrue();
        }

        // go to transactions
        driver.get(FRONTEND_URL + "/transactions");
        wait.until(ExpectedConditions.urlContains("/transactions"));

        // check list
        WebElement transactionItem = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//ul[contains(@class,'tx-list')]//li[contains(@class,'tx-item')]//div[contains(@class,'title') and normalize-space()='E2E Test Purchase']")
            )
        );
        assertThat(transactionItem.isDisplayed()).isTrue();

        // edit transaction
        WebElement editButton = driver.findElement(
            By.xpath("//li[contains(@class,'tx-item')]//div[contains(@class,'title') and normalize-space()='E2E Test Purchase']/ancestor::li//button[@aria-label='Edit transaction']")
        );
        editButton.click();

        // wait for edit modal
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[normalize-space()='Edit Transaction']")));

        // change description
        WebElement editDescriptionTextarea = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("edit-desc"))
        );
        editDescriptionTextarea.clear();
        editDescriptionTextarea.sendKeys("E2E Test Purchase - EDITED");

        // save
        WebElement saveButton = driver.findElement(By.xpath("//button[contains(.,'Update Transaction')]"));
        saveButton.click();

        // wait for toast
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[contains(@class,'toast') and contains(.,'updated')]")
        ));

        // back to transactions if needed
        if (!driver.getCurrentUrl().contains("/transactions")) {
            driver.get(FRONTEND_URL + "/transactions");
        }

        // delete transaction
        WebElement deleteButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//li[contains(@class,'tx-item')]//div[contains(@class,'title') and normalize-space()='E2E Test Purchase - EDITED']/ancestor::li//button[@aria-label='Delete transaction']")
            )
        );
        deleteButton.click();

        // confirm delete
        WebElement confirmDeleteButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'modal-actions')]//button[contains(@class,'btn-delete') and normalize-space()='Delete']"))
        );
        confirmDeleteButton.click();

        // wait until gone
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//ul[contains(@class,'tx-list')]//div[contains(@class,'title') and normalize-space()='E2E Test Purchase - EDITED']")
        ));

        // delete account
        WebElement usernameButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("button.user-pill"))
        );
        usernameButton.click();

        // open delete dialog
        WebElement deleteAccountButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-delete-account-proceed"))
        );
        deleteAccountButton.click();

        // enter password
        WebElement deletePasswordInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector(".delete-account-modal input[type='password']"))
        );
        deletePasswordInput.sendKeys(password);

        // confirm
        WebElement confirmAccountDeleteButton = driver.findElement(
            By.cssSelector(".delete-account-modal button.btn-delete-account")
        );
        confirmAccountDeleteButton.click();

        // should land on /auth
        wait.until(ExpectedConditions.urlContains("/auth"));
        assertThat(driver.getCurrentUrl()).contains("auth");

        // try to login again (should fail)
        clickIfPresent(By.xpath("//button[contains(text(), 'Login')]"));

        // attempt login with deleted creds
        WebElement retryUsernameInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("username"))
        );
        WebElement retryPasswordInput = driver.findElement(By.name("password"));
        WebElement retryLoginButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.cssSelector("form.form button.primary"))
        );

        retryUsernameInput.clear();
        retryUsernameInput.sendKeys(uniqueUsername);
        retryPasswordInput.clear();
        retryPasswordInput.sendKeys(password);
        retryLoginButton.click();

        // stay on /auth, no nav
        wait.until(ExpectedConditions.urlContains("/auth"));
        assertThat(driver.findElements(By.cssSelector("nav.app-nav, nav[aria-label='Main']")).isEmpty()).isTrue();

        // optional error presence
        List<WebElement> errors = driver.findElements(
            By.xpath("//div[contains(@class,'alert') or contains(@class,'error')][contains(.,'Invalid') or contains(.,'not found') or contains(.,'does not exist') or contains(.,'Failed')]")
        );
        if (!errors.isEmpty()) {
            assertThat(errors.get(0).isDisplayed()).isTrue();
        }

        // ensure no auth flag
        Object authFlag = ((JavascriptExecutor) driver).executeScript("return window.localStorage.getItem('auth')");
        assertThat(authFlag).isNull();
    }

    // helpers to avoid empty catch blocks and flakiness
    private void clickIfPresent(By locator) {
        List<WebElement> els = driver.findElements(locator);
        if (!els.isEmpty()) {
            WebElement el = els.get(0);
            if (el.isDisplayed() && el.isEnabled()) {
                el.click();
            }
        }
    }
}
