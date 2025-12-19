import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseTest {

    protected WebDriver driver;

    @Before
    public void setUp() {
        String browserName = System.getProperty("browser", "chrome");
        
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", 
                           "--disable-dev-shm-usage", 
                           "--window-size=1920,1080", 
                           "--disable-gpu", 
                           "--remote-allow-origins=*",
                           "--disable-blink-features=AutomationControlled",
                           "--disable-extensions",
                           "--start-maximized");
        
        if (browserName.equalsIgnoreCase("yandex")) {
            options.setBinary("/Applications/Yandex.app/Contents/MacOS/Yandex");
        }
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
