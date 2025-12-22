import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.Before;
import org.junit.Test;
import ru.stellarburgers.pageObject.MainPage;

import static org.junit.Assert.*;

@Feature("Главная страница")
@Story("Навигация по конструктору")
public class MainPageTest extends BaseTest {
    
    private MainPage mainPage;
    
    @Before
    public void setUp() {
        super.setUp();
        driver.navigate().to("https://stellarburgers.education-services.ru/");
        mainPage = new MainPage(driver);
    }
    
    @Test
    @Description("Проверка отображения кнопки личного кабинета")
    public void testProfileButtonDisplayed() {
        assertTrue("Кнопка личного кабинета должна отображаться", mainPage.profileButtonIsDisplayed());
    }
    
    @Test
    @Description("Проверка перехода к разделу Булки")
    public void testNavigateToBunsSection() {
        mainPage.sectionBunClick();
        assertEquals("Должен быть выбран раздел Булки", "Булки", mainPage.returnSelectedSection("Булки"));
    }
    
    @Test
    @Description("Проверка перехода к разделу Соусы")
    public void testNavigateToSaucesSection() {
        mainPage.sectionSauceClick();
        assertEquals("Должен быть выбран раздел Соусы", "Соусы", mainPage.returnSelectedSection("Соусы"));
    }
    
    @Test
    @Description("Проверка перехода к разделу Начинки")
    public void testNavigateToToppingsSection() {
        mainPage.sectionToppingClick();
        assertEquals("Должен быть выбран раздел Начинки", "Начинки", mainPage.returnSelectedSection("Начинки"));
    }
}
