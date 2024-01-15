package net.rostkoff.simpletodoapp.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import net.rostkoff.simpletodoapp.selenium.pages.AddTaskPage;

public class AddTaskPageTests {
    private WebDriver driver;
    private AddTaskPage addTaskPage;

    @BeforeEach
    public void init() {
        driver = new ChromeDriver();
        driver.get("http://google.com");
        // addTaskPage = new AddTaskPage(driver);
        // addTaskPage.open();
    }

    

    @Test
    public void openTest() {
        // assertEquals(driver.getCurrentUrl(), addTaskPage.URL);
    }
}
