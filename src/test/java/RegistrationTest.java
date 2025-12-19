import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import ru.stellarburgers.pageObject.RegisterPage;
import ru.stellarburgers.pageObject.MainPage;
import ru.stellarburgers.api.ApiClient;

import static org.junit.Assert.*;

@Feature("Аутентификация")
@Story("Регистрация")
public class RegistrationTest extends BaseTest {
    
    private RegisterPage registerPage;
    private MainPage mainPage;
    private ApiClient apiClient;

    @Before
    public void setUp() {
        super.setUp();
        driver.navigate().to("https://stellarburgers.education-services.ru/register");
        registerPage = new RegisterPage(driver);
        mainPage = new MainPage(driver);
        apiClient = new ApiClient();
    }
    
    @Test
    @Description("Успешная регистрация с корректными данными")
    public void testSuccessfulRegistration() {
        String email = "newuser" + System.currentTimeMillis() + "@example.com";
        String name = "Test User";
        String password = "password123";
        
        registerPage.register(name, email, password);
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.urlToBe("https://stellarburgers.education-services.ru/"));
        assertTrue("Должна быть видна кнопка Оформить заказ", 
                mainPage.createOrderButtonIsDisplayed());
        
        // Удаляем пользователя через API после теста
        try {
            String token = apiClient.loginUser(email, password);
            if (token != null && !token.isEmpty()) {
                apiClient.deleteUser(token);
            }
        } catch (Exception e) {
            System.out.println("Не удалось удалить пользователя: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Ошибка для некорректного пароля (менее 6 символов)")
    public void testRegistrationWithShortPassword() {
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String name = "Test User";
        String shortPassword = "pass";  // Менее 6 символов
        
        registerPage.register(name, email, shortPassword);
        assertTrue("Должно отображаться сообщение об ошибке пароля", 
                registerPage.passwordErrorMessageIsDisplayed());
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
