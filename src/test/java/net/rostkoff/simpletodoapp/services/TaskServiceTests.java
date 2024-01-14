package net.rostkoff.simpletodoapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.exceptions.ApiUnavailable;

public class TaskServiceTests {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;
    @InjectMocks
    private TaskService taskService;
    private AutoCloseable ac;

    @BeforeEach
    public void init() {
        ac = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        ac.close();
    }

    @Test
    public void addTaskReturnsIdWhenTaskIsAdded() {
        TaskDto taskDto = new TaskDto();
        Long expected = 1L,
        actual;

        when(restClient.post()
                        .uri(anyString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(taskDto)
                        .retrieve()
                        .toEntity(Long.class)
                        .getBody()
        )
        .thenReturn(expected);

        actual = taskService.addTask(taskDto);

        assertEquals(expected, actual);
    }

    @Test
    public void addTaskThrowsExceptionWhenApiIsUnavailable() {
        TaskDto taskDto = new TaskDto();

        when(restClient.post()
                        .uri(anyString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(taskDto)
                        .retrieve()
        ).thenThrow(new ResourceAccessException(""));

        assertThrows(ApiUnavailable.class, () -> taskService.addTask(taskDto));
    }

    @Test
    public void getTaskReturnsTaskDtoWhenTaskIsFound() {
        TaskDto expected = new TaskDto(),
        actual;

        expected.setId(1L);

        when(restClient.get()
                        .uri(anyString())
                        .retrieve()
                        .toEntity(TaskDto.class)
                        .getBody()
        )
        .thenReturn(expected);

        actual = taskService.getTask(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void getTaskThrowsExceptionWhenApiIsUnavailable() {
        when(restClient.get()
                        .uri(anyString())
                        .retrieve()
        ).thenThrow(new ResourceAccessException(""));

        assertThrows(ApiUnavailable.class, () -> taskService.getTask(1L));
    }

    @Test
    public void deleteTaskReturnsVoidWhenTaskIsDeleted() {
        taskService.deleteTask(1L);
        
        verify(restClient.delete()
            .uri(anyString())
            .retrieve()
            , 
            times(1)
        )
        .toBodilessEntity();
    }

    @Test
    public void deleteTaskThrowsExceptionWhenApiIsUnavailable() {
        when(restClient.delete()
                        .uri(anyString())
                        .retrieve()
        ).thenThrow(new ResourceAccessException(""));

        assertThrows(ApiUnavailable.class, () -> taskService.deleteTask(1L));
    }

    @Test
    public void updateTaskReturnsVoidWhenTaskIsUpdated() {
        TaskDto taskDto = new TaskDto();

        taskDto.setId(1L);

        taskService.updateTask(taskDto);
        
        verify(restClient.put()
            .uri(anyString())
            .contentType(MediaType.APPLICATION_JSON)
            .body(taskDto)
            .retrieve()
            , 
            times(1)
        )
        .toBodilessEntity();
    }

    @Test
    public void updateTaskThrowsExceptionWhenApiIsUnavailable() {
        TaskDto taskDto = new TaskDto();

        when(restClient.put()
                        .uri(anyString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(taskDto)
                        .retrieve()
        ).thenThrow(new ResourceAccessException(""));

        assertThrows(ApiUnavailable.class, () -> taskService.updateTask(taskDto));
    }

}
