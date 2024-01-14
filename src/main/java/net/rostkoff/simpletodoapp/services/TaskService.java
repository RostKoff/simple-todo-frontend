package net.rostkoff.simpletodoapp.services;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.exceptions.ApiUnavailable;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;



@Service
public class TaskService {
    RestClient restClient;
    private final String API_URL = "http://localhost:8081";

    public TaskService() {
        restClient = RestClient.create();
    }

    public Long addTask(TaskDto taskDto) {
        try {
            return restClient.post()
                            .uri(API_URL + "/tasks/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(taskDto)
                            .retrieve()
                            .toEntity(Long.class)
                            .getBody();
        } catch (ResourceAccessException ex) {
            throw new ApiUnavailable();
        }
    }

    public TaskDto getTask(Long id) {
        try {
            return restClient.get()
                    .uri(API_URL + "/tasks/" + id)
                    .retrieve()
                    .toEntity(TaskDto.class)
                    .getBody();
        } catch (ResourceAccessException ex) {
            throw new ApiUnavailable();
        }
    }

    public void deleteTask(Long id) {
        try {
            restClient.delete()
                    .uri(API_URL + "/tasks/" + id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (ResourceAccessException ex) {
            throw new ApiUnavailable();
        }
    }

    public void updateTask(TaskDto taskDto) {
        try {
            restClient.put()
                    .uri(API_URL + "/tasks/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(taskDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (ResourceAccessException ex) {
            throw new ApiUnavailable();
        }
    }
}
