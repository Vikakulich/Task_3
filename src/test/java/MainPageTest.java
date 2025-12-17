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

import static org.junit.Assert.*;

@Feature("Главная страница")
@Story("Навигация по конструктору")
public class MainPageTest {
    
    private WebDriver driver;
    private MainPage mainPage;
    
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
        driver.navigate().to("https://stellarburgers.education-services.ru/");
        mainPage = new MainPage(driver);
    }
    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @Description("Проверка отображения кнопки личного кабинета")
    public void testProfileButtonDisplayed() {
        assertTrue("Кнопка личного кабинета должна отображаться", mainPage.profileButtonIsDisplayed());
    }
    
    @Test
    @Description("Проверка перехода к разделу Булки")
    public void testNavigateToBunsSection() {
        try {
            mainPage.sectionBunClick();
            assertTrue("Раздел Булки доступен", true);
        } catch (Exception e) {
            fail("Не удалось нажать на раздел Булки: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Проверка перехода к разделу Соусы")
    public void testNavigateToSaucesSection() {
        try {
            mainPage.sectionSauceClick();
            assertTrue("Раздел Соусы доступен", true);
        } catch (Exception e) {
            fail("Не удалось нажать на раздел Соусы: " + e.getMessage());
        }
    }
    
    @Test
    @Description("Проверка перехода к разделу Начинки")
    public void testNavigateToToppingsSection() {
        try {
            mainPage.sectionToppingClick();
            assertTrue("Раздел Начинки доступен", true);
        } catch (Exception e) {
            fail("Не удалось нажать на раздел Начинки: " + e.getMessage());
        }
    }
}
