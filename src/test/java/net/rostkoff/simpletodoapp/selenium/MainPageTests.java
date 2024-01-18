package net.rostkoff.simpletodoapp.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import net.rostkoff.simpletodoapp.selenium.pages.MainPage;
import net.rostkoff.simpletodoapp.selenium.pages.TaskViewPage;
import net.rostkoff.simpletodoapp.services.TaskService;

import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class MainPageTests {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void init() {
        driver = new ChromeDriver();
        mainPage = new MainPage(driver);
        mainPage.open();
    }

    @AfterEach
    public void close() {
        driver.quit();
    }

    @Test
    public void openTest() {
        assertEquals(MainPage.URL, driver.getCurrentUrl());        
    }

    @Test
    public void openTaskTest() {
        var taskDto = SeleniumHelper.createTaskDto();
        var taskService = new TaskService();
        List<WebElement> tasks;
        Optional<WebElement> expectedCalendarTask;
        TaskViewPage taskViewPage;
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try {
            var taskId = taskService.addTask(taskDto);
            taskDto.setId(taskId);

            driver.navigate().refresh();

            tasks = mainPage.getTasks();
            expectedCalendarTask = tasks.stream()
                .filter(t -> t.getAttribute("href").endsWith("/tasks/" + taskId))
                .findFirst();

            assertTrue(expectedCalendarTask.isPresent());

            expectedCalendarTask.get().click();
            taskViewPage = new TaskViewPage(driver);

            assertTrue(driver.getCurrentUrl().startsWith(TaskViewPage.URL));
            assertEquals(taskDto.getTitle(), taskViewPage.getTitle());
            assertEquals(taskDto.getDescription(), taskViewPage.getDescription());
            assertEquals(taskDto.getStartDate().format(formatter), taskViewPage.getStartDate());
            assertEquals(taskDto.getEndDate().format(formatter), taskViewPage.getEndDate());
            assertEquals(taskDto.isAllDay() ? "Yes" : "No", taskViewPage.getAllDay());
        } finally {
            taskService.deleteTask(taskDto.getId());
        }
    }

    @Test
    public void previousButtonTest() {
        var xPath = "//*[contains(@class, 'fc-day') and @data-date]";
        LocalDate dateBeforeClick, dateAfterClick;
        
        dateBeforeClick = getDateFromXpathElement(xPath);
        
        mainPage.clickPrevButton();
        
        dateAfterClick = getDateFromXpathElement(xPath);

        assertTrue(dateAfterClick.isBefore(dateBeforeClick));
    }

    @Test
    public void nextButtonTest() {
        var xPath = "//*[contains(@class, 'fc-day') and @data-date]";
        LocalDate dateBeforeClick, 
        dateAfterClick;
        
        dateBeforeClick = getDateFromXpathElement(xPath);
        
        mainPage.clickNextButton();

        dateAfterClick = getDateFromXpathElement(xPath);

        assertTrue(dateAfterClick.isAfter(dateBeforeClick));
    }

    @Test 
    public void todayButtonTest() {
        var xPath = "//*[contains(@class, 'fc-day') and @data-date]";

        LocalDate dateBeforeClick,
        dateAfterNextButton,
        dateAfterTodayButton;

        dateBeforeClick = getDateFromXpathElement(xPath);

        mainPage.clickNextButton();
        dateAfterNextButton = getDateFromXpathElement(xPath);

        mainPage.clickTodayButton();
        dateAfterTodayButton = getDateFromXpathElement(xPath);
        
        assertNotEquals(dateBeforeClick, dateAfterNextButton);
        assertEquals(dateBeforeClick, dateAfterTodayButton);
    }

    @Test
    public void clickWeekButtonTest() {
        var className = "fc-timeGridWeek-view";

        mainPage.clickWeekButton();

        assertTrue(driver.findElement(By.className(className)).isDisplayed());
    }

    @Test
    public void clickListButtonTest() {
        var className = "fc-listWeek-view";

        mainPage.clickListButton();

        assertTrue(driver.findElement(By.className(className)).isDisplayed());
    }

    @Test void clickMonthButtonTest() {
        var className = "fc-dayGridMonth-view";

        mainPage.clickMonthButton();

        assertTrue(driver.findElement(By.className(className)).isDisplayed());
    }

    private LocalDate getDateFromXpathElement(String xPath) {
        var element = driver.findElement(By.xpath(xPath));
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(element.getAttribute("data-date"), formatter);
    }


}
