package net.rostkoff.simpletodoapp.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.selenium.pages.EditTaskPage;
import net.rostkoff.simpletodoapp.selenium.pages.TaskViewPage;
import net.rostkoff.simpletodoapp.services.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditTaskPageTests {
    private WebDriver driver;
    private EditTaskPage editTaskPage;
    private TaskService taskService;
    private TaskDto taskDto;

    public EditTaskPageTests() {
        driver = new ChromeDriver();
        editTaskPage = new EditTaskPage(driver);
    }

    @BeforeEach
    public void init() {
        taskService = new TaskService();
        taskDto = SeleniumHelper.createTaskDto();

        var taskId = taskService.addTask(taskDto);
        taskDto.setId(taskId);

        editTaskPage.open(taskId);
    }

    @AfterEach
    public void close() {
        driver.quit();
        if(taskDto.getId() != null)
            taskService.deleteTask(taskDto.getId());
    }

    @Test
    public void openTest() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");    
        var closeDate = taskDto.getCloseDate() == null ? "" : taskDto.getCloseDate().format(formatter);

        assertEquals(EditTaskPage.URL + taskDto.getId(), driver.getCurrentUrl());
        assertEquals("Edit Task", editTaskPage.getHeader());

        assertEquals(taskDto.getTitle(), editTaskPage.getTitleInputValue());
        assertEquals(taskDto.getDescription(), editTaskPage.getDescriptionInputValue());
        assertEquals(taskDto.getStartDate().format(formatter), editTaskPage.getStartDateInputValue());
        assertEquals(taskDto.getEndDate().format(formatter), editTaskPage.getEndDateInputValue());
        assertEquals(closeDate, editTaskPage.getCloseDateInputValue());
        assertEquals(taskDto.isAllDay(), editTaskPage.getAllDayRadioButtons().get("allDayTrue").isSelected());
    }

    @Test
    public void editTaskTest() {
        var editedStringField = "Edited";

        // Character sequence representing date and time.
        var editedCharDate = "01012021";
        var editedCharTime = "0000";

        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var expectedEditedDateString = "2021-01-01 00:00";
        var expectedEditedDate = LocalDateTime.parse(expectedEditedDateString, formatter);      

        TaskViewPage taskViewPage;

        taskDto.setTitle(editedStringField);
        taskDto.setDescription(editedStringField);
        taskDto.setStartDate(expectedEditedDate);
        taskDto.setEndDate(expectedEditedDate);
        taskDto.setCloseDate(expectedEditedDate);
        taskDto.setAllDay(false);

        editTaskPage.setTitleInput(editedStringField);
        editTaskPage.setDescriptionInput(editedStringField);
        editTaskPage.setStartDateInput(editedCharDate, Keys.TAB, editedCharTime);
        editTaskPage.setEndDateInput(editedCharDate, Keys.TAB, editedCharTime);
        editTaskPage.setCloseDateInput(editedCharDate, Keys.TAB, editedCharTime);
        editTaskPage.getAllDayRadioButtons().get("allDayFalse").click();

        taskViewPage = editTaskPage.clickSubmitButton();
        
        assertTrue(driver.getCurrentUrl().startsWith(TaskViewPage.URL + taskDto.getId()));
        assertEquals(taskDto.getTitle(), taskViewPage.getTitle());
        assertEquals(taskDto.getDescription(), taskViewPage.getDescription());
        assertEquals(expectedEditedDateString, taskViewPage.getStartDate());
        assertEquals(expectedEditedDateString, taskViewPage.getEndDate());
        assertEquals(expectedEditedDateString, taskViewPage.getCloseDate());
        assertEquals("No", taskViewPage.getAllDay());
    }
}
