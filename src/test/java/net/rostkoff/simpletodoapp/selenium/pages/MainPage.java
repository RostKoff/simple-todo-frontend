package net.rostkoff.simpletodoapp.selenium.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MainPage {
    
    public static String URL = "http://localhost:8080/";
    
    private WebDriver driver;

    @FindBy(id="fc-dom-1")
    private WebElement date;

    @FindBy(className="fc-listWeek-button")
    private WebElement listButton;
    @FindBy(className="fc-TimeGridWeek-button")
    private WebElement weekButton;
    @FindBy(className="fc-dayGridMonth-button")
    private WebElement monthButton;

    @FindBy(className="fc-today-button")
    private WebElement todayButton;
    @FindBy(className="fc-prev-button")
    private WebElement prevButton;
    @FindBy(className="fc-next-button")
    private WebElement nextButton;

    @FindBy(className="fc-event")
    private List<WebElement> tasks;


    public MainPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get(URL);
    }

    public String getDate() {
        return date.getText();
    }

    public void clickListButton() {
        listButton.click();
        refillTasks();
    }

    public void clickWeekButton() {
        weekButton.click();
        refillTasks();
    }

    public void clickMonthButton() {
        monthButton.click();
        refillTasks();
    }

    public void clickTodayButton() {
        todayButton.click();
        refillTasks();
    }

    public void clickPrevButton() {
        prevButton.click();
        refillTasks();
    }

    public void clickNextButton() {
        nextButton.click();
        refillTasks();
    }

    public List<WebElement> getTasks() {
        return tasks;
    }

    private void refillTasks() {
        tasks.clear();
        tasks = driver.findElements(By.id("fc-event"));
    }

}
