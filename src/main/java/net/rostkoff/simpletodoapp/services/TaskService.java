package net.rostkoff.simpletodoapp.services;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TaskService {
    RestClient restClient;
    private final String API_URL = "http://localhost:8081";

    public TaskService() {
        restClient = RestClient.create();
    }

    public void addTask(TaskDto taskDto) {
        restClient.post()
                .uri(API_URL + "/tasks/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(taskDto)
                .retrieve()
                .toBodilessEntity();
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
}
