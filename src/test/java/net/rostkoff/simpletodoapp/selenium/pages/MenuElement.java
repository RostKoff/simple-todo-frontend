package net.rostkoff.simpletodoapp.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MenuElement {
    private WebDriver driver;

    @FindBy(id="menu-calendar")
    private WebElement calendarLink;
    @FindBy(id="menu-add-task")
    private WebElement addTaskLink;

    public MenuElement(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public MainPage clickCalendarLink() {
        calendarLink.click();
        return new MainPage(driver);
    }

    public AddTaskPage clickAddTaskLink() {
        addTaskLink.click();
        return new AddTaskPage(driver);
    }

}
