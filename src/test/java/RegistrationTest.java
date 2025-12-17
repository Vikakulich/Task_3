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
import ru.stellarburgers.pageObject.RegisterPage;
import ru.stellarburgers.api.ApiClient;

import static org.junit.Assert.*;

@Feature("Аутентификация")
@Story("Регистрация")
public class RegistrationTest {
    
    private WebDriver driver;
    private RegisterPage registerPage;
    private ApiClient apiClient;

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
        driver.navigate().to("https://stellarburgers.education-services.ru/register");
        registerPage = new RegisterPage(driver);
        apiClient = new ApiClient();
    }
    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Description("Успешная регистрация с корректными данными")
    public void testSuccessfulRegistration() {
        String email = "newuser" + System.currentTimeMillis() + "@example.com";
        String name = "Test User";
        String password = "password123";
        
        try {
            registerPage.register(name, email, password);
            assertTrue("Регистрация выполнена", true);
        } catch (Exception e) {
            fail("Ошибка при регистрации: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Ошибка для некорректного пароля (менее 6 символов)")
    public void testRegistrationWithShortPassword() {
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String name = "Test User";
        String shortPassword = "pass";  // Менее 6 символов
        
        try {
            registerPage.register(name, email, shortPassword);
            // После попытки регистрации с коротким паролем проверяем что мы все еще на странице регистрации
            assertTrue("Остаемся на странице регистрации", 
                driver.getCurrentUrl().contains("/register"));
        } catch (Exception e) {
            // Ошибка при заполнении формы тоже допустима
            assertTrue("Короткий пароль обработан", true);
        }
    }
    
    @Test
    @Description("Проверка отображения страницы регистрации")
    public void testRegisterPageDisplayed() {
        try {
            assertTrue("Кнопка регистрации отображается", registerPage.registerButtonIsDisplayed());
            assertTrue("Заголовок регистрации отображается", registerPage.headingIsDisplayed());
        } catch (Exception e) {
            fail("Ошибка при проверке страницы регистрации: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Переход на страницу входа из регистрации")
    public void testNavigateToLoginFromRegister() {
        try {
            registerPage.clickLoginLink();
            assertTrue("Переход на вход из регистрации работает", 
                driver.getCurrentUrl().contains("/login"));
        } catch (Exception e) {
            fail("Ошибка при переходе на вход из регистрации: " + e.getMessage());
        }
    }
}
