package net.rostkoff.simpletodoapp.services;

import net.rostkoff.simpletodoapp.contract.TaskDto;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TaskService {
    RestClient restClient;
    private final String API_URL = "http://localhost:8081";

    public TaskService() {
        restClient = RestClient.create();
    }

    public Long addTask(TaskDto taskDto) {
        return restClient.post()
                .uri(API_URL + "/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDto)
                .retrieve()
                .toEntity(Long.class)
                .getBody();
    }

    public TaskDto getTask(Long id) {
        return restClient.get()
                .uri(API_URL + "/tasks/" + id)
                .retrieve()
                .toEntity(TaskDto.class)
                .getBody();
    }

    public void deleteTask(Long id) {
        restClient.delete()
                .uri(API_URL + "/tasks/" + id)
                .retrieve()
                .toBodilessEntity();
    }

    public void updateTask(TaskDto taskDto) {
        restClient.put()
                .uri(API_URL + "/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDto)
                .retrieve()
                .toBodilessEntity();
    }

    public Map<String, String> getFormattedDates(TaskDto taskDto) {
        var map = new HashMap<String, String>();
        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var closeDate = taskDto.getCloseDate();
        map.put("startDate", taskDto.getStartDate().format(dateFormatter));
        map.put("endDate", taskDto.getEndDate().format(dateFormatter));
        if (closeDate != null)
            map.put("closeDate", closeDate.format(dateFormatter));
        return map;
    }
}
