package net.rostkoff.simpletodoapp.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import net.rostkoff.simpletodoapp.selenium.pages.AddTaskPage;
import net.rostkoff.simpletodoapp.selenium.pages.TaskViewPage;
import net.rostkoff.simpletodoapp.services.TaskService;

public class AddTaskPageTests {
    private WebDriver driver;
    private AddTaskPage addTaskPage;

    @BeforeEach
    public void init() {
        driver = new ChromeDriver();
        addTaskPage = new AddTaskPage(driver);
        addTaskPage.open();
    }

    @AfterEach
    public void close() {
        driver.quit();
    }

    @Test
    public void openTest() {
        assertEquals(AddTaskPage.URL, driver.getCurrentUrl());
        assertEquals("Add Task", addTaskPage.getHeader());
    }

    @Test
    public void addTaskButtonTest() {
        var title = "Test task";
        var description = "Test description";
        
        // Character sequence representing date and time.
        var charDate = "01012024";
        var charTime = "0000";

        var expectedDate = "2024-01-01 00:00";
        var taskService = new TaskService();
        TaskViewPage taskViewPage;
        
        addTaskPage.setTitleInput(title);
        addTaskPage.setDescriptionInput(description);
        addTaskPage.setStartDateInput(charDate, Keys.TAB, charTime);
        addTaskPage.setEndDateInput(charDate, Keys.TAB, charTime);
        addTaskPage.getAllDayRadioButtons().get("allDayTrue").click();

        taskViewPage = addTaskPage.clickSubmitButton();
        
        try {
            assertTrue(driver.getCurrentUrl().startsWith(TaskViewPage.URL));
            assertEquals(title, taskViewPage.getTitle());
            assertEquals(description, taskViewPage.getDescription());
            assertEquals(expectedDate, taskViewPage.getStartDate());
            assertEquals(expectedDate, taskViewPage.getEndDate());
            assertEquals("Yes", taskViewPage.getAllDay());
        } finally {
            taskService.deleteTask(Long.parseLong(taskViewPage.getId()));
        }
    }
}
