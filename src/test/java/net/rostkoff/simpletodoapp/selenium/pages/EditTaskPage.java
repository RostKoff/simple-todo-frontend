package net.rostkoff.simpletodoapp.selenium.pages;

import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

public class EditTaskPage {
    public static String URL = "http://localhost:8080/tasks/edit/";

    private WebDriver driver;

    @FindBy(tagName="h1")
    private WebElement header;
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
        allDayRadioButtons = new HashMap<>();
        PageFactory.initElements(driver, this);
    }

    public void open(Long id) {
        driver.get(URL + id);
    }

    public String getTitleInputValue() {
        return titleInput.getAttribute("value");
    }
    public String getDescriptionInputValue() {
        return descriptionInput.getAttribute("value");
    }
    public String getStartDateInputValue() {
        return startDateInput.getAttribute("value");
    }
    public String getEndDateInputValue() {
        return endDateInput.getAttribute("value");
    }
    public String getCloseDateInputValue() {
        return closeDateInput.getAttribute("value");
    }
    public Map<String, WebElement> getAllDayRadioButtons() {
        if (allDayRadioButtons.isEmpty())
            allDayRadioButtons = driver.findElements(By.name("allDay")).stream()
                    .collect(Collectors.toMap(e -> e.getAttribute("id"), e -> e));
        return allDayRadioButtons;
    }
    public String getHeader() {
        return header.getText();
    }

    public void setTitleInput(CharSequence... title) {
        titleInput.clear();
        titleInput.sendKeys(title);
    }
    public void setDescriptionInput(CharSequence... description) {
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
    }
    public void setStartDateInput(CharSequence... startDate) {
        startDateInput.clear();
        startDateInput.sendKeys(startDate);
    }
    public void setEndDateInput(CharSequence... endDate) {
        endDateInput.clear();
        endDateInput.sendKeys(endDate);
    }
    public void setCloseDateInput(CharSequence... closeDate) {
        closeDateInput.clear();
        closeDateInput.sendKeys(closeDate);
    }
    public TaskViewPage clickSubmitButton() {
        submitButton.click();
        return new TaskViewPage(driver);
    }
}
