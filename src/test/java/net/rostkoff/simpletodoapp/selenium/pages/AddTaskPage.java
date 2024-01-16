package net.rostkoff.simpletodoapp.selenium.pages;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

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
    @FindBy(id = "id")
    private WebElement taskId;
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
        menu = new MenuElement(driver);
        allDayRadioButtons = new HashMap<>();
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(URL);
    }

    public String getHeader() {
        return header.getText();
    }

    public Map<String, WebElement> getAllDayRadioButtons() {
        if (allDayRadioButtons.isEmpty())
            allDayRadioButtons = driver.findElements(By.name("allDay")).stream()
                    .collect(Collectors.toMap(e -> e.getAttribute("id"), e -> e));
        return allDayRadioButtons;
    }

    public MenuElement getMenu() {
        return menu;
    }

    public void setTitleInput(CharSequence... title) {
        titleInput.sendKeys(title);
    }

    public void setDescriptionInput(CharSequence... description) {
        descriptionInput.sendKeys(description);
    }

    public void setStartDateInput(CharSequence... keysToSend) {
        startDateInput.sendKeys(keysToSend);
    }

    public void setEndDateInput(CharSequence... keysToSend) {
        endDateInput.sendKeys(keysToSend);
    }

    public TaskViewPage clickSubmitButton() {
        submitButton.click();
        return new TaskViewPage(driver);
    }
}
