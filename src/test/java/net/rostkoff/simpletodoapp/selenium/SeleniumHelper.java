package net.rostkoff.simpletodoapp.selenium;

import net.rostkoff.simpletodoapp.contract.TaskDto;

import java.time.LocalDateTime;

public class SeleniumHelper {
    public static TaskDto createTaskDto() {
        var date = LocalDateTime.now();
        TaskDto taskDto = new TaskDto();

        taskDto.setTitle("Big task");
        taskDto.setDescription("Test description");
        taskDto.setStartDate(date);
        taskDto.setEndDate(date);
        taskDto.setAllDay(true);

        return taskDto;
    }
}
