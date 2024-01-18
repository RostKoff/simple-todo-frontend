package net.rostkoff.simpletodoapp.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class TaskViewPage {

    public static String URL = "http://localhost:8080/tasks/";

    private WebDriver driver;

    @FindBy(id="id")
    private WebElement idField;
    @FindBy(id="title")
    private WebElement titleField;
    @FindBy(id="description")
    private WebElement descriptionField;
    @FindBy(id="start-date")
    private WebElement startDateField;
    @FindBy(id="end-date")
    private WebElement endDateField;
    @FindBy(id="close-date")
    private WebElement closeDateField;
    @FindBy(id="all-day")
    private WebElement allDayField;
    @FindBy(id="delete-btn")
    private WebElement deleteButton;
    @FindBy(id="edit-btn")
    private WebElement editButton;

    public TaskViewPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open(Long id) {
        driver.get(URL + id);
    }
    public String getTitle() {
        return titleField.getText();
    }
    public String getDescription() {
        return descriptionField.getText();
    }
    public String getStartDate() {
        return startDateField.getText();
    }
    public String getEndDate() {
        return endDateField.getText();
    }
    public String getAllDay() {
        return allDayField.getText();
    }
    public String getCloseDate() {
        return closeDateField.getText();
    }
    public String getId() {
        return idField.getAttribute("data-value");
    }

    public MainPage clickDeleteButton() {
        deleteButton.click();
        return new MainPage(driver);
    }
    public EditTaskPage clickEditButton() {
        editButton.click();
        return new EditTaskPage(driver);
    }
    
}
