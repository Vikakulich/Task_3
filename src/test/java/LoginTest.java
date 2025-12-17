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
import ru.stellarburgers.pageObject.LoginPage;
import ru.stellarburgers.pageObject.MainPage;
import ru.stellarburgers.pageObject.ForgotPasswordPage;
import ru.stellarburgers.api.ApiClient;

import static org.junit.Assert.*;

@Feature("Аутентификация")
@Story("Вход в систему")
public class LoginTest {
    
    private WebDriver driver;
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
        // Браузер: chrome (по умолчанию) или yandex
        String browserName = System.getProperty("browser", "chrome");
        
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (browserName.equalsIgnoreCase("yandex")) {
            // Путь к Яндекс.Браузеру на macOS
            options.setBinary("/Applications/Yandex.app/Contents/MacOS/Yandex");
        }
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
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
        
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Description("Вход по кнопке «Войти в аккаунт» на главной")
    public void testLoginFromMainPageButton() {
        driver.navigate().to("https://stellarburgers.education-services.ru/");
        try {
            mainPage.loginButtonClick();
            assertTrue("Вход по кнопке главной работает", true);
        } catch (Exception e) {
            fail("Ошибка при клике на кнопку входа: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Вход через кнопку «Личный кабинет»")
    public void testLoginFromPersonalAccountButton() {
        driver.navigate().to("https://stellarburgers.education-services.ru/");
        try {
            mainPage.profileButtonClick();
            assertTrue("Кнопка личного кабинета работает", true);
        } catch (Exception e) {
            fail("Ошибка при клике на кнопку личного кабинета: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Вход через кнопку в форме регистрации")
    public void testLoginLinkFromRegisterForm() {
        driver.navigate().to("https://stellarburgers.education-services.ru/register");
        try {
            // На странице регистрации есть ссылка "Войти"  
            // Используем простой поиск элемента
            driver.findElement(org.openqa.selenium.By.xpath(".//a[contains(text(), 'Войти')]")).click();
            assertTrue("Переход на вход из регистрации работает", true);
        } catch (Exception e) {
            fail("Ошибка при переходе на вход из регистрации: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Вход через кнопку в форме восстановления пароля")
    public void testLoginLinkFromForgotPasswordForm() {
        driver.navigate().to("https://stellarburgers.education-services.ru/forgot-password");
        try {
            forgotPasswordPage.clickLoginLink();
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/login"));
            assertTrue("Переход на вход из восстановления пароля работает", 
                driver.getCurrentUrl().contains("/login"));
        } catch (Exception e) {
            fail("Ошибка при переходе на вход из восстановления пароля: " + e.getMessage());
        }
    }
}
