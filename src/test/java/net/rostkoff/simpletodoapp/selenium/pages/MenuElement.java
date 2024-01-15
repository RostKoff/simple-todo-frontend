package net.rostkoff.simpletodoapp.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MenuElement {
    @FindBy(id="menu-calendar")
    private WebElement calendarLink;
    @FindBy(id="menu-add-task")
    private WebElement addTaskLink;

    public void clickCalendarLink() {
        calendarLink.click();
    }

    public void clickAddTaskLink() {
        addTaskLink.click();
    }

}
