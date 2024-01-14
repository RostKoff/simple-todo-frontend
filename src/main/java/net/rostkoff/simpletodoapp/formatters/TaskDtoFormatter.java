package net.rostkoff.simpletodoapp.formatters;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.rostkoff.simpletodoapp.contract.TaskDto;

@Component
public class TaskDtoFormatter implements IFormatDates<TaskDto> {

    @Override
    public Map<String, String> getFormattedDates(TaskDto taskDto) {
        var map = new HashMap<String, String>();
        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var closeDate = taskDto.getCloseDate();
        
        map.put("startDate", taskDto.getStartDate().format(dateFormatter));
        map.put("endDate", taskDto.getEndDate().format(dateFormatter));
        if (closeDate != null) {
            map.put("closeDate", closeDate.format(dateFormatter));
        }
        return map;
    }
    
}
