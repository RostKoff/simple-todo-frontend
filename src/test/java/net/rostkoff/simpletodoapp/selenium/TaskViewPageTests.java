package net.rostkoff.simpletodoapp.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.selenium.pages.MainPage;
import net.rostkoff.simpletodoapp.selenium.pages.TaskViewPage;
import net.rostkoff.simpletodoapp.services.TaskService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.format.DateTimeFormatter;

public class TaskViewPageTests {
    private WebDriver driver;
    private TaskViewPage taskViewPage;
    private TaskService taskService;
    private TaskDto taskDto;

    @BeforeEach
    public void init() {
        driver = new ChromeDriver();
        taskService = new TaskService();

        taskDto = SeleniumHelper.createTaskDto();

        var taskId = taskService.addTask(taskDto);
        taskDto.setId(taskId);
        
        taskViewPage = new TaskViewPage(driver);
        taskViewPage.open(taskId);
    }

    @AfterEach
    public void close() {
        driver.quit();
        if(taskDto.getId() != null)
            taskService.deleteTask(taskDto.getId());
    }

    @Test
    public void openTest() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        assertEquals(TaskViewPage.URL + taskDto.getId(), driver.getCurrentUrl());
        assertEquals(taskDto.getTitle(), taskViewPage.getTitle());
        assertEquals(taskDto.getDescription(), taskViewPage.getDescription());
        assertEquals(taskDto.getStartDate().format(formatter), taskViewPage.getStartDate());
        assertEquals(taskDto.getEndDate().format(formatter), taskViewPage.getEndDate());
        assertEquals(taskDto.isAllDay() ? "Yes" : "No", taskViewPage.getAllDay());
    }

    @Test
    public void deleteTaskButtonTest() {
        var mainPage = taskViewPage.clickDeleteButton();
        var allTasks = mainPage.getTasks();
        var targetTask = allTasks.stream().filter(f -> f.getAttribute("href").equals("/tasks/" + taskDto.getId())).findFirst();

        assertTrue(driver.getCurrentUrl().startsWith(MainPage.URL));
        assertTrue(targetTask.isEmpty());
        taskDto.setId(null);
    }

    @Test
    public void editTaskButtonTest() {
        var editTaskPage = taskViewPage.clickEditButton();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        assertEquals(taskDto.getTitle(), editTaskPage.getTitle());
        assertEquals(taskDto.getDescription(), editTaskPage.getDescription());
        assertEquals(taskDto.getStartDate().format(formatter), editTaskPage.getStartDate());
        assertEquals(taskDto.getEndDate().format(formatter), editTaskPage.getEndDate());
        assertEquals(taskDto.isAllDay(), editTaskPage.getAllDayRadioButtons().get("allDayTrue").isSelected());
    }

}
