package net.rostkoff.simpletodoapp.selenium.pages;

import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditTaskPage {
    private static String URL = "http://localhost:8080/tasks/edit/";

    private WebDriver driver;

    @FindBy(id="title")
    private WebElement titleInput;
    @FindBy(id="description")
    private WebElement descriptionInput;
    @FindBy(id="start-date")
    private WebElement startDateInput;
    @FindBy(id="end-date")
    private WebElement endDateInput;
    @FindBy(id="close-date")
    private WebElement closeDateInput;

    // Stores all radio buttons from allDay radio group. Key - id of radio button, value - radio button element. 
    private Map<String, WebElement> allDayRadioButtons;

    @FindBy(className = "btn")
    private WebElement submitButton;

    public EditTaskPage(WebDriver driver) {
        this.driver = driver;

        var radioButtonsList = driver.findElements(By.name("allDay"));
        if(radioButtonsList != null)
            allDayRadioButtons = radioButtonsList.stream().collect(Collectors.toMap(e -> e.getAttribute("id"), e -> e));

        PageFactory.initElements(driver, this);
    }

    public void open(String id) {
        driver.get(URL + id);
    }

    public void getTitle() {
        titleInput.getText();
    }
    public void getDescription() {
        descriptionInput.getText();
    }
    public void getStartDate() {
        startDateInput.getText();
    }
    public void getEndDate() {
        endDateInput.getText();
    }
    public void getCloseDate() {
        closeDateInput.getText();
    }
    public boolean isAllDay() {
        return allDayRadioButtons.get("allDayTrue").isSelected();
    }

    public void setTitleField(String title) {
        titleInput.sendKeys(title);
    }
    
    public void setDescriptionField(String description) {
        descriptionInput.sendKeys(description);
    }
    public void setStartDateField(String startDate) {
        startDateInput.sendKeys(startDate);
    }
    public void setEndDateField(String endDate) {
        endDateInput.sendKeys(endDate);
    }
    public void setCloseDateField(String closeDate) {
        closeDateInput.sendKeys(closeDate);
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
