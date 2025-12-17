package ru.stellarburgers.pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProfilePage {

    // PageObject для страницы профиля/личного кабинета
    private WebDriver driver;

    // Кнопка Выйти
    private By logoutButton = By.xpath("//button[text()='Выход']");
    // Кнопка Конструктор
    private By constructorButton = By.xpath("//p[text()='Конструктор']/parent::a");
    // Логотип (находится в header, но вне nav)
    private By logo = By.cssSelector("header a[href='/'] img, a[href='/'] > svg, a[href='/'] img");
    // Заголовок Личный кабинет
    private By heading = By.xpath(".//h1");
    // Ссылка Профиль
    private By profileLink = By.xpath(".//a[contains(text(), 'Профиль')]");
    // Ссылка История заказов
    private By ordersLink = By.xpath(".//a[contains(text(), 'История')]");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
    }

    // Выход из аккаунта
    @Step
    public void logout() {
        // Ждем загрузки страницы профиля
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlContains("/account"));
        
        // Пробуем разные локаторы для кнопки выхода
        By[] logoutLocators = {
            By.xpath("//button[text()='Выход']"),
            By.xpath("//button[contains(text(), 'Выход')]"),
            By.xpath("//button[contains(text(), 'Выйти')]"),
            By.cssSelector("button.Account_button__14Yp3"),
            By.xpath("//nav[@class]//button")
        };
        
        WebElement element = null;
        for (By locator : logoutLocators) {
            try {
                element = new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(locator));
                if (element != null) break;
            } catch (Exception ignored) {}
        }
        
        if (element == null) {
            throw new org.openqa.selenium.NoSuchElementException("Кнопка выхода не найдена");
        }
        
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Клик на Конструктор
    @Step
    public void clickConstructorButton() {
        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(constructorButton));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Клик на логотип
    @Step
    public void clickLogo() {
        // Ждем загрузки страницы
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlContains("/account"));
        
        // Логотип находится в header, но ВНЕ nav - это отдельная ссылка с href="/"
        // Пробуем разные локаторы
        By[] logoLocators = {
            By.cssSelector("header a[href='/'] img"),
            By.xpath("//header//a[@href='/']/img"),
            By.xpath("//a[@href='/']/img"),
            By.xpath("//header//a[@href='/']"),
            By.cssSelector("a[href='/'] > img")
        };
        
        WebElement element = null;
        for (By locator : logoLocators) {
            try {
                element = new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(locator));
                if (element != null) break;
            } catch (Exception ignored) {}
        }
        
        if (element == null) {
            throw new org.openqa.selenium.NoSuchElementException("Логотип не найден");
        }
        
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // Проверка отображения заголовка
    @Step
    public boolean headingIsDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(heading));
        try {
            return driver.findElement(heading).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Проверка отображения кнопки Выйти
    @Step
    public boolean logoutButtonIsDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
        try {
            return driver.findElement(logoutButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Проверка отображения ссылки Профиль
    @Step
    public boolean profileLinkIsDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(profileLink));
            return driver.findElement(profileLink).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Проверка отображения ссылки История заказов
    @Step
    public boolean ordersLinkIsDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(ordersLink));
            return driver.findElement(ordersLink).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}

