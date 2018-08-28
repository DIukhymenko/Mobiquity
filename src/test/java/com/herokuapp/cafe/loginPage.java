package com.herokuapp.cafe;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class loginPage {
    By loginField = By.xpath("//input[@ng-model='user.name']");
    By passwordField = By.xpath("//input[@ng-model='user.password']");
    By loginButton = By.xpath("//button[text()='Login']");
    By helloText = By.id("greetings");
    By logoutButton = By.xpath("//p[@ng-click='logout()']");
    String userName;
    String userPassword;

    public loginPage(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    private WebDriver driver;
    public void openLoginPage() {
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://cafetownsend-angular-rails.herokuapp.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        driver.findElement(loginField).sendKeys(userName);
        driver.findElement(passwordField).sendKeys(userPassword);
        driver.findElement(loginButton).click();
        Assert.assertEquals("Hello " + userName + "", driver.findElement(helloText).getText().trim());
        Assert.assertEquals(true, driver.findElement(logoutButton).isEnabled());

    }
    }
