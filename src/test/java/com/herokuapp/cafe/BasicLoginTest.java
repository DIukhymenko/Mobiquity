package com.herokuapp.cafe;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

public class BasicLoginTest {
    WebDriver driver;
    By loginField = By.xpath("//input[@ng-model='user.name']");
    By passwordField = By.xpath("//input[@ng-model='user.password']");
    By loginButton = By.xpath("//button[text()='Login']");
    By helloText = By.id("greetings");
    By logoutButton = By.xpath("//p[@ng-click='logout()']");
    By createEmployeeButton = By.id("bAdd");
    By updateEmployeeButton = By.xpath("//button[text()='Update']");
    By editEmployeeButton = By.id("bEdit");
    By deleteEmployeeButton = By.id("bDelete");
    By cancelCreationButton = By.xpath("//*[@id=\"sub-nav\"]/li/a");
    By firstNameField = By.xpath("//input[@ng-model='selectedEmployee.firstName']");
    By lastNameField = By.xpath("//input[@ng-model='selectedEmployee.lastName']");
    By startDateField = By.xpath("//input[@ng-model='selectedEmployee.startDate']");
    By emailField = By.xpath("//input[@ng-model='selectedEmployee.email']");
    By addEmployeeButton = By.xpath("//button[@ng-show='isCreateForm']");
    String userName = "Luke";
    String userPassword = "Skywalker";
    String firstName = "John";
    String lastName = "Snow";
    String startDate = "2017-01-01";
    String email = "avengers@gmail.com";
    String firstNameUpd = "Tony";
    String lastNameUpd = "Stark";
    String startDateUpd = "2018-02-02";
    String emailUpd = "new_email@gmail.com";

    @BeforeMethod
    public void initMethod() {
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://cafetownsend-angular-rails.herokuapp.com/login");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        loginAs(userName, userPassword);
    }


    public void loginAs(String login, String password) {
        driver.findElement(loginField).sendKeys(login);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        Assert.assertEquals("Hello " + userName + "",driver.findElement(helloText).getText().trim());
        Assert.assertEquals(true, driver.findElement(logoutButton).isEnabled());
    }

    public void fillEmployeeData(String name, String lastName, String startDate, String email){
        driver.findElement(firstNameField).clear();
        driver.findElement(lastNameField).clear();
        driver.findElement(startDateField).clear();
        driver.findElement(emailField).clear();
        driver.findElement(firstNameField).sendKeys(name);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(startDateField).sendKeys(startDate);
        driver.findElement(emailField).sendKeys(email);
    }

    public WebElement searchUser(String name, String lastName) throws InterruptedException {
        WebElement element = driver.findElement(By.xpath("//li[contains(text(), '"+name+" "+lastName+"')]"));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
        return element;
    }

    @Test (priority = 2 )
    public void createEmployee() throws InterruptedException {
        driver.findElement(createEmployeeButton).click();
        fillEmployeeData(firstName, lastName, startDate, email);
        driver.findElement(addEmployeeButton).click();
        Assert.assertEquals("http://cafetownsend-angular-rails.herokuapp.com/employees/new", driver.getCurrentUrl());
        (searchUser(firstName, lastName)).click();
        driver.findElement(editEmployeeButton).click();
        Assert.assertEquals(firstName, driver.findElement(firstNameField).getAttribute("value"));
        Assert.assertEquals(lastName, driver.findElement(lastNameField).getAttribute("value"));
        Assert.assertEquals(startDate, driver.findElement(startDateField).getAttribute("value"));
        Assert.assertEquals(email, driver.findElement(emailField).getAttribute("value"));
    }

    @Test (priority = 3 )
    public void editByDoubleClick() throws InterruptedException {
        driver.findElement(createEmployeeButton).click();
        fillEmployeeData(firstName, lastName, startDate, email);
        driver.findElement(addEmployeeButton).click();
        WebElement user = searchUser(firstName, lastName);
        Actions action = new Actions(driver);
        action.doubleClick(user).build().perform();
        fillEmployeeData(firstNameUpd, lastNameUpd, startDateUpd, emailUpd);
        driver.findElement(updateEmployeeButton).click();
        Thread.sleep(4000);
        (searchUser(firstNameUpd, lastNameUpd)).click();
        driver.findElement(editEmployeeButton).click();
        Assert.assertEquals(firstNameUpd, driver.findElement(firstNameField).getAttribute("value"));
        Assert.assertEquals(lastNameUpd, driver.findElement(lastNameField).getAttribute("value"));
        Assert.assertEquals(startDateUpd, driver.findElement(startDateField).getAttribute("value"));
        Assert.assertEquals(emailUpd, driver.findElement(emailField).getAttribute("value"));
    }

    @Test (priority = 4 )
    public void editByButtonClick() throws InterruptedException {
        driver.findElement(createEmployeeButton).click();
        fillEmployeeData(firstName, lastName, startDate, email);
        driver.findElement(addEmployeeButton).click();
        (searchUser(firstName, lastName)).click();
        Assert.assertEquals(true, driver.findElement(editEmployeeButton).isEnabled());
        driver.findElement(editEmployeeButton).click();
        fillEmployeeData(firstNameUpd, lastNameUpd, startDateUpd, emailUpd);
        driver.findElement(updateEmployeeButton).click();
        Thread.sleep(4000);
        (searchUser(firstNameUpd, lastNameUpd)).click();
        driver.findElement(editEmployeeButton).click();
        Assert.assertEquals(firstNameUpd, driver.findElement(firstNameField).getAttribute("value"));
        Assert.assertEquals(lastNameUpd, driver.findElement(lastNameField).getAttribute("value"));
        Assert.assertEquals(startDateUpd, driver.findElement(startDateField).getAttribute("value"));
        Assert.assertEquals(emailUpd, driver.findElement(emailField).getAttribute("value"));

    }

    @Test (priority = 5 )
    public void deleteEmployee() throws InterruptedException {
        WebElement user = searchUser(firstName, lastName);
        Actions action = new Actions(driver);
        action.doubleClick(user).build().perform();
        driver.findElement(deleteEmployeeButton).click();
        Assert.assertEquals("Are you sure you want to delete " + firstName +" "+ lastName+ "?", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        //Assert.assertEquals("http://cafetownsend-angular-rails.herokuapp.com/employees", driver.getCurrentUrl());
    }

    @Test (priority = 6 )
    public void logoutEmployee() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertEquals("http://cafetownsend-angular-rails.herokuapp.com/employees", driver.getCurrentUrl());
        Assert.assertEquals(true, driver.findElement(createEmployeeButton).isDisplayed());
        driver.findElement(logoutButton).click();
        Assert.assertEquals("http://cafetownsend-angular-rails.herokuapp.com/login", driver.getCurrentUrl());
    }

    @AfterMethod
    public void tearDownSession() {
        driver.quit();
    }
}
