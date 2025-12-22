import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import ru.stellarburgers.pageObject.MainPage;
import ru.stellarburgers.pageObject.ProfilePage;
import ru.stellarburgers.pageObject.LoginPage;
import ru.stellarburgers.api.ApiClient;

import static org.junit.Assert.*;

@Feature("Личный кабинет")
@Story("Управление аккаунтом")
public class AccountTest extends BaseTest {
    
    private MainPage mainPage;
    private ProfilePage profilePage;
    private LoginPage loginPage;
    private ApiClient apiClient;
    private String testEmail;
    private String testPassword;
    private String testName;
    private String userToken;
    
    @Before
    public void setUp() {
        super.setUp();
        driver.navigate().to("https://stellarburgers.education-services.ru/");
        
        mainPage = new MainPage(driver);
        profilePage = new ProfilePage(driver);
        loginPage = new LoginPage(driver);
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
    @Description("Переход в личный кабинет по клику на кнопку")
    public void testNavigateToPersonalAccount() {
        // Логинимся через UI
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        
        // Ждем перехода на главную после логина
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        
        // Переходим в личный кабинет
        mainPage.profileButtonClick();
        
        // После клика должны быть на странице личного кабинета
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/account"));
        assertTrue("URL должен содержать /account", driver.getCurrentUrl().contains("/account"));
    }
    
    @Test
    @Description("Переход из личного кабинета в конструктор по кнопке Конструктор")
    public void testNavigateToConstructorFromAccount() {
        // Логинимся и переходим в личный кабинет
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        mainPage.profileButtonClick();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/account"));
        
        // Ждем загрузки страницы личного кабинета
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlMatches(".*/account.*"));
        
        // Кликаем на Конструктор
        profilePage.clickConstructorButton();
        
        // Должны вернуться на главную страницу
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должны быть на главной странице", 
            driver.getCurrentUrl().equals("https://stellarburgers.education-services.ru/"));
    }
    
    @Test
    @Description("Переход из личного кабинета на главную по логотипу")
    public void testNavigateToMainByLogoFromAccount() {
        // Логинимся и переходим в личный кабинет
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        mainPage.profileButtonClick();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/account"));
        
        // Ждем загрузки страницы личного кабинета и навигации
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlMatches(".*/account.*"));
        // Ждем появления навигации
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//nav")));
        
        // Кликаем на логотип
        profilePage.clickLogo();
        
        // Должны вернуться на главную страницу
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должны быть на главной странице", 
            driver.getCurrentUrl().equals("https://stellarburgers.education-services.ru/"));
    }
    
    @Test
    @Description("Выход из аккаунта по кнопке Выйти")
    public void testLogoutFromAccount() {
        // Логинимся и переходим в личный кабинет
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        mainPage.profileButtonClick();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/account"));
        
        // Ждем загрузки страницы личного кабинета и навигации
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlMatches(".*/account.*"));
        // Ждем появления навигации
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//nav")));
        
        // Выходим из аккаунта
        profilePage.logout();
        
        // После выхода должны быть на странице входа
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlContains("/login"));
        assertTrue("Должны быть на странице входа", 
            driver.getCurrentUrl().contains("/login"));
    }
}




