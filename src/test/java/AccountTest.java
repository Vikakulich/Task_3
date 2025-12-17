import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.stellarburgers.pageObject.MainPage;
import ru.stellarburgers.pageObject.ProfilePage;
import ru.stellarburgers.pageObject.LoginPage;
import ru.stellarburgers.api.ApiClient;

import static org.junit.Assert.*;

@Feature("Личный кабинет")
@Story("Управление аккаунтом")
public class AccountTest {
    
    private WebDriver driver;
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
        // Браузер: chrome (по умолчанию) или yandex
        String browserName = System.getProperty("browser", "chrome");
        
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        
        if (browserName.equalsIgnoreCase("yandex")) {
            // Путь к Яндекс.Браузеру на macOS
            options.setBinary("/Applications/Yandex.app/Contents/MacOS/Yandex");
        }
        
        driver = new ChromeDriver(options);
        
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
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
        
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Description("Переход в личный кабинет по клику на кнопку")
    public void testNavigateToPersonalAccount() {
        // Логинимся через UI
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        
        // Ждем перехода на главную после логина
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        
        // Переходим в личный кабинет
        mainPage.profileButtonClick();
        
        // После клика должны быть на странице личного кабинета
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/account"));
        assertTrue("URL должен содержать /account", driver.getCurrentUrl().contains("/account"));
    }
    
    @Test
    @Description("Переход из личного кабинета в конструктор по кнопке Конструктор")
    public void testNavigateToConstructorFromAccount() {
        // Логинимся и переходим в личный кабинет
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        mainPage.profileButtonClick();
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/account"));
        
        // Ждем загрузки страницы личного кабинета
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlMatches(".*/account.*"));
        
        // Кликаем на Конструктор
        profilePage.clickConstructorButton();
        
        // Должны вернуться на главную страницу
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должны быть на главной странице", 
            driver.getCurrentUrl().equals("https://stellarburgers.education-services.ru/"));
    }
    
    @Test
    @Description("Переход из личного кабинета на главную по логотипу")
    public void testNavigateToMainByLogoFromAccount() {
        // Логинимся и переходим в личный кабинет
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        mainPage.profileButtonClick();
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/account"));
        
        // Ждем загрузки страницы личного кабинета и навигации
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlMatches(".*/account.*"));
        // Ждем появления навигации
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//nav")));
        
        // Кликаем на логотип
        profilePage.clickLogo();
        
        // Должны вернуться на главную страницу
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должны быть на главной странице", 
            driver.getCurrentUrl().equals("https://stellarburgers.education-services.ru/"));
    }
    
    @Test
    @Description("Выход из аккаунта по кнопке Выйти")
    public void testLogoutFromAccount() {
        // Логинимся и переходим в личный кабинет
        mainPage.loginButtonClick();
        loginPage.login(testEmail, testPassword);
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("stellarburgers.education-services.ru/"));
        mainPage.profileButtonClick();
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/account"));
        
        // Ждем загрузки страницы личного кабинета и навигации
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(15))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlMatches(".*/account.*"));
        // Ждем появления навигации
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(
                    org.openqa.selenium.By.xpath("//nav")));
        
        // Выходим из аккаунта
        profilePage.logout();
        
        // После выхода должны быть на странице входа
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/login"));
        assertTrue("Должны быть на странице входа", 
            driver.getCurrentUrl().contains("/login"));
    }
}




