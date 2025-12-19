import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import ru.stellarburgers.pageObject.LoginPage;
import ru.stellarburgers.pageObject.MainPage;
import ru.stellarburgers.pageObject.ForgotPasswordPage;
import ru.stellarburgers.api.ApiClient;

import static org.junit.Assert.*;

@Feature("Аутентификация")
@Story("Вход в систему")
public class LoginTest extends BaseTest {
    
    private LoginPage loginPage;
    private MainPage mainPage;
    private ForgotPasswordPage forgotPasswordPage;
    private ApiClient apiClient;
    private String testEmail;
    private String testPassword;
    private String testName;
    private String userToken;

    @Before
    public void setUp() {
        super.setUp();
        
        mainPage = new MainPage(driver);
        loginPage = new LoginPage(driver);
        forgotPasswordPage = new ForgotPasswordPage(driver);
        apiClient = new ApiClient();
        
        // Генерируем уникальные данные теста
        testEmail = "test" + System.currentTimeMillis() + "@example.com";
        testPassword = "password123";
        testName = "Test User";
        
        // Создаем тестового пользователя через API
        try {
            userToken = apiClient.registerUser(testEmail, testPassword, testName);
        } catch (Exception e) {
            System.out.println("Не удалось создать пользователя через API: " + e.getMessage());
        }
    }
    
    @After
    public void tearDown() {
        // Удаляем тестового пользователя через API
        try {
            if (userToken != null && !userToken.isEmpty()) {
                apiClient.deleteUser(userToken);
            }
        } catch (Exception e) {
            System.out.println("Не удалось удалить пользователя: " + e.getMessage());
        }
        
        super.tearDown();
    }
    
    @Test
    @Description("Вход по кнопке «Войти в аккаунт» на главной")
    public void testLoginFromMainPageButton() {
        driver.navigate().to("https://stellarburgers.education-services.ru/");
        mainPage.loginButtonClick();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/login"));
        loginPage.login(testEmail, testPassword);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должна быть видна кнопка Оформить заказ", 
                mainPage.createOrderButtonIsDisplayed());
    }
    
    @Test
    @Description("Вход через кнопку «Личный кабинет»")
    public void testLoginFromPersonalAccountButton() {
        driver.navigate().to("https://stellarburgers.education-services.ru/");
        mainPage.profileButtonClick();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/login"));
        loginPage.login(testEmail, testPassword);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должна быть видна кнопка Оформить заказ", 
                mainPage.createOrderButtonIsDisplayed());
    }
    
    @Test
    @Description("Вход через кнопку в форме регистрации")
    public void testLoginLinkFromRegisterForm() {
        driver.navigate().to("https://stellarburgers.education-services.ru/register");
        driver.findElement(org.openqa.selenium.By.xpath(".//a[contains(text(), 'Войти')]")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/login"));
        loginPage.login(testEmail, testPassword);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должна быть видна кнопка Оформить заказ", 
                mainPage.createOrderButtonIsDisplayed());
    }
    
    @Test
    @Description("Вход через кнопку в форме восстановления пароля")
    public void testLoginLinkFromForgotPasswordForm() {
        driver.navigate().to("https://stellarburgers.education-services.ru/forgot-password");
        forgotPasswordPage.clickLoginLink();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/login"));
        loginPage.login(testEmail, testPassword);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должна быть видна кнопка Оформить заказ", 
                mainPage.createOrderButtonIsDisplayed());
    }
}
