package ru.stellarburgers.pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

public class MainPage {

    // PageObject для главной страницы
    private WebDriver driver;

    // Кнопка Войти в аккаунт
    private By loginButton = By.xpath(".//button[contains(text(), 'Войти в аккаунт')]");
    // Кнопка Личный кабинет
    private By profileButton = By.xpath(".//a[contains(@href, '/account')]");
    // Кнопка Оформить заказ
    private By createOrderButton = By.xpath(".//button[contains(text(), 'Оформить заказ')]");
    // раздел Булки
    private By sectionBun = By.xpath(".//span[contains(text(), 'Булки')]");
    // раздел Соусы
    private By sectionSauce = By.xpath(".//span[contains(text(), 'Соусы')]");
    // раздел Начинки
    private By sectionTopping = By.xpath(".//span[contains(text(), 'Начинки')]");
    // Логотип
    private By logo = By.xpath(".//nav//a/img");
    // Конструктор ссылка в навигации
    private By constructorLink = By.xpath(".//nav//a[contains(@href, '/')]");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    // Нажатие на кнопку Войти в аккаунт
    @Step
    public void loginButtonClick() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        driver.findElement(loginButton).click();
    }

    // Нажатие на кнопку Личный кабинет
    @Step
    public void profileButtonClick() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(profileButton));
        driver.findElement(profileButton).click();
    }

    // Нажатие на раздел Булки
    @Step
    public void sectionBunClick() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(sectionBun));
        WebElement element = driver.findElement(sectionBun);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Нажатие на раздел Соусы
    @Step
    public void sectionSauceClick() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(sectionSauce));
        WebElement element = driver.findElement(sectionSauce);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Нажатие на раздел Начинки
    @Step
    public void sectionToppingClick() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(sectionTopping));
        WebElement element = driver.findElement(sectionTopping);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Клик на логотип
    @Step
    public void logoClick() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(logo));
        driver.findElement(logo).click();
    }

    // Клик на Конструктор в навигации
    @Step
    public void constructorLinkClick() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(constructorLink));
        driver.findElement(constructorLink).click();
    }

    // Проверка отображения кнопки перехода в Личный кабинет
    @Step
    public boolean profileButtonIsDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(profileButton));
        try {
            return driver.findElement(profileButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Проверка отображения кнопки Оформить заказ
    @Step
    public boolean createOrderButtonIsDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(createOrderButton));
        try {
            return driver.findElement(createOrderButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Возвращает выбранный элемент в конструкторе
    @Step
    public String returnSelectedSection(String sectionName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.textToBe(By.xpath(".//div[contains(@class, 'current')]/span"), sectionName));
        return driver.findElement(By.xpath(".//div[contains(@class, 'current')]/span")).getText();
    }

}

