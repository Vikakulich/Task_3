package ru.stellarburgers.pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegisterPage {

    // PageObject для страницы регистрации
    private WebDriver driver;

    // Имя
    private By nameField = By.xpath("(//input[@type='text'])[1]");
    // Email
    private By emailField = By.xpath("(//input[@type='text'])[2]");
    // Пароль
    private By passwordField = By.xpath("(//input[@type='password'])[1]");
    // Кнопка Зарегистрироваться
    private By registerButton = By.xpath(".//button[contains(text(), 'Зарегистрироваться')]");
    // Ссылка Войти
    private By loginLink = By.xpath(".//a[contains(text(), 'Войти')]");
    // Заголовок
    private By heading = By.xpath(".//h2[contains(text(), 'Регистрация')]");
    // Сообщение об ошибке пароля
    private By passwordErrorMessage = By.xpath(".//p[contains(text(), 'Некорректный пароль')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    // Регистрация пользователя
    @Step
    public void register(String name, String email, String password) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(registerButton));
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(registerButton).click();
    }

    // Проверка отображения кнопки Зарегистрироваться
    @Step
    public boolean registerButtonIsDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(registerButton));
        try {
            return driver.findElement(registerButton).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Клик на ссылку Войти
    @Step
    public void clickLoginLink() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(loginLink));
        driver.findElement(loginLink).click();
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

    // Проверка отображения ошибки пароля
    @Step
    public boolean passwordErrorMessageIsDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(passwordErrorMessage));
            return driver.findElement(passwordErrorMessage).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}

