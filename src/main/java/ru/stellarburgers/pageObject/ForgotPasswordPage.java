package ru.stellarburgers.pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ForgotPasswordPage {

    // PageObject для страницы восстановления пароля
    private WebDriver driver;

    // Email
    private By emailField = By.xpath("(//input[@type='text'])[1]");
    // Кнопка Восстановить
    private By restoreButton = By.xpath(".//button[contains(text(), 'Восстановить')]");
    // Ссылка Войти
    private By loginLink = By.xpath(".//a[contains(text(), 'Войти')]");
    // Заголовок
    private By heading = By.xpath(".//h2[contains(text(), 'Восстановление')]");

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
    }

    // Восстановление пароля
    @Step
    public void restorePassword(String email) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(restoreButton));
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(restoreButton).click();
    }

    // Проверка отображения кнопки Восстановить
    @Step
    public boolean restoreButtonIsDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(restoreButton));
        try {
            return driver.findElement(restoreButton).isDisplayed();
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

}

