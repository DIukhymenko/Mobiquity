package com.herokuapp.cafe;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class LoginPage {
    private final By loginField = By.xpath("//input[@ng-model='user.name']");
    private final By passwordField = By.xpath("//input[@ng-model='user.password']");
    private final By loginButton = By.xpath("//button[text()='Login']");
    private final By helloText = By.id("greetings");
    private final By logoutButton = By.xpath("//p[@ng-click='logout()']");
    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openLoginPage() {
        driver.get("http://cafetownsend-angular-rails.herokuapp.com/login");
    }

    public void loginUser(String userName, String userPassword){
        driver.findElement(loginField).sendKeys(userName);
        driver.findElement(passwordField).sendKeys(userPassword);
        driver.findElement(loginButton).click();
        Assert.assertEquals("Hello " + userName + "", driver.findElement(helloText).getText().trim());
        Assert.assertEquals(true, driver.findElement(logoutButton).isEnabled());
    }
}
