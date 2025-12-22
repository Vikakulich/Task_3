package ru.stellarburgers.pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {

    // PageObject для страницы входа
    private WebDriver driver;

    // Email
    private By emailField = By.xpath("(//input[@type='text'])[1]");
    // Пароль
    private By passwordField = By.xpath("(//input[@type='password'])[1]");
    // Кнопка Войти
    private By loginButton = By.xpath(".//button[contains(text(), 'Войти')]");
    // Ссылка Зарегистрироваться
    private By registerLink = By.xpath(".//a[contains(text(), 'Зарегистрироваться')]");
    // Ссылка Восстановить пароль
    private By forgotPasswordLink = By.xpath(".//a[contains(text(), 'Восстановить пароль')]");
    // Заголовок
    private By heading = By.xpath(".//h2[contains(text(), 'Вход')]");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Вход в аккаунт
    @Step
    public void login(String email, String password) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    // Проверка отображения кнопки Войти
    @Step
    public boolean loginButtonIsDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        try {
            return driver.findElement(loginButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Клик на ссылку Зарегистрироваться
    @Step
    public void clickRegisterLink() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(registerLink));
        driver.findElement(registerLink).click();
    }

    // Клик на ссылку Восстановить пароль
    @Step
    public void clickForgotPasswordLink() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(forgotPasswordLink));
        driver.findElement(forgotPasswordLink).click();
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

}

