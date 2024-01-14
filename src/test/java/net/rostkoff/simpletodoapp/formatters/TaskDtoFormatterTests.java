package net.rostkoff.simpletodoapp.formatters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.rostkoff.simpletodoapp.contract.TaskDto;

public class TaskDtoFormatterTests {
    private TaskDtoFormatter taskDtoFormatter;

    @BeforeEach
    public void init() {
        taskDtoFormatter = new TaskDtoFormatter();
    }

    @Test
    public void getFormattedDatesReturnsMapWithFormattedDates() {
        var taskDto = new TaskDto();
        var date = LocalDateTime.of(2021, 1, 1, 0, 0);
        var expectedDate = "2021-01-01 00:00";
        Map<String, String> map;

        taskDto.setStartDate(date);
        taskDto.setEndDate(date);
        taskDto.setCloseDate(date);

        map = taskDtoFormatter.getFormattedDates(taskDto);

        assertEquals(3, map.size());
        assertTrue(map.containsKey("startDate"));
        assertTrue(map.containsKey("endDate"));
        assertTrue(map.containsKey("closeDate"));
        assertEquals(expectedDate, map.get("startDate"));
        assertEquals(expectedDate, map.get("endDate"));
        assertEquals(expectedDate, map.get("closeDate"));
    }

    @Test
    public void getFormattedDatesReturnsMapWithFormattedDatesWithoutCloseDate() {
        var taskDto = new TaskDto();
        var date = LocalDateTime.of(2021, 1, 1, 0, 0);
        var expectedDate = "2021-01-01 00:00";
        Map<String, String> map;

        taskDto.setStartDate(date);
        taskDto.setEndDate(date);

        map = taskDtoFormatter.getFormattedDates(taskDto);

        assertEquals(2, map.size());
        assertTrue(map.containsKey("startDate"));
        assertTrue(map.containsKey("endDate"));
        assertFalse(map.containsKey("closeDate"));
        assertEquals(expectedDate, map.get("startDate"));
        assertEquals(expectedDate, map.get("endDate"));
    }

}
