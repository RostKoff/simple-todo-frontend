package net.rostkoff.simpletodoapp.selenium.pages;

import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddTaskPage {

    public final String URL = "http://localhost:8080/tasks/add";

    private WebDriver driver;
    
    private MenuElement menu;
    @FindBy(tagName = "h1")
    private WebElement header;
    @FindBy(id = "title")
    private WebElement titleInput;
    @FindBy(id = "description")
    private WebElement descriptionInput;
    @FindBy(id = "start-date")
    private WebElement startDateInput;
    @FindBy(id = "end-date")
    private WebElement endDateInput;
    @FindBy(className = "btn")
    private WebElement submitButton;

    // Stores all radio buttons from allDay radio group. Key - id of radio button, value - radio button element. 
    private Map<String, WebElement> allDayRadioButtons;


    public AddTaskPage(WebDriver driver) {
        this.driver = driver;
        
        var radioButtonsList = driver.findElements(By.name("allDay"));
        if(radioButtonsList != null)
            allDayRadioButtons = radioButtonsList.stream().collect(Collectors.toMap(e -> e.getAttribute("id"), e -> e));
        
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(URL);
    }

    public String getHeader() {
        return header.getText();
    }

    public void setTitleInput(String title) {
        titleInput.sendKeys(title);
    }

    public void setDescriptionInput(String description) {
        descriptionInput.sendKeys(description);
    }

    public void setStartDateInput(String startDate) {
        startDateInput.sendKeys(startDate);
    }

    public void setEndDateInput(String endDate) {
        endDateInput.sendKeys(endDate);
    }

    public void setAllDay(boolean allDay) {
        if(allDay)
            allDayRadioButtons.get("allDayTrue").click();
        else
            allDayRadioButtons.get("allDayFalse").click();
    }

    public TaskViewPage clickSubmitButton() {
        submitButton.click();
        return new TaskViewPage(driver);
    }
}
