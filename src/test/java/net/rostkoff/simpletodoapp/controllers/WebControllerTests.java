package net.rostkoff.simpletodoapp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.HttpClientErrorException;

import net.rostkoff.simpletodoapp.TestHelper;
import net.rostkoff.simpletodoapp.contract.TaskDto;
import net.rostkoff.simpletodoapp.exceptions.ApiUnavailable;
import net.rostkoff.simpletodoapp.exceptions.ErrorResponse;
import net.rostkoff.simpletodoapp.exceptions.handlers.WebExceptionHandler;
import net.rostkoff.simpletodoapp.formatters.TaskDtoFormatter;
import net.rostkoff.simpletodoapp.services.TaskService;

public class WebControllerTests {
    @Mock
    private TaskService taskService;

    @Mock
    TaskDtoFormatter taskDtoFormatter;

    @InjectMocks
    private WebController webController;

    private AutoCloseable ac;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        ac = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new WebExceptionHandler(), webController).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        ac.close();
    }

    @Test
    public void getMainViewReturnsIndexView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("module", "calendar"))
                .andExpect(view().name("index"));
    }

    @Test
    public void getAddPageViewReturnsAddTaskView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", Matchers.any(TaskDto.class)))
                .andExpect(model().attribute("module", "addTask"))
                .andExpect(view().name("addTask"));
    }

    @Test
    public void addPageRedirectsToTaskView() throws Exception {
        var taskDto = new TaskDto();
        MvcResult mvcResult;
        ModelMap modelMap;
        
        taskDto.setId(1L);

        when(taskService.addTask(taskDto)).thenReturn(taskDto.getId());

        mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/tasks/add")
                .flashAttr("taskDto", taskDto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tasks/" + taskDto.getId()))
                .andReturn();
        
        modelMap = mvcResult.getModelAndView().getModelMap();
        
        assertTrue(modelMap.containsKey("message"));
        assertEquals("Task Created", modelMap.get("message"));
    }

    @Test
    public void addPageReturnsAnErrorResponseWhenServiceThrowsHttpClientErrorException() throws Exception {
        var taskDto = new TaskDto();
        var httpStatus = HttpStatus.BAD_REQUEST;

        // Convert ErrorResponse to JSON bytes in order to pass it to HttpClientErrorException
        var errorResponse = new ErrorResponse(httpStatus.value(), "Message");
        byte[] jsonBytes = TestHelper.convertErrorResponseToBytes(errorResponse);

        when(taskService.addTask(any(TaskDto.class))).thenThrow(new HttpClientErrorException(httpStatus, "", jsonBytes, StandardCharsets.UTF_8));

        mockMvc.perform(
            MockMvcRequestBuilders.post("/tasks/add")
            .flashAttr("taskDto", taskDto)
        )
        .andExpect(status().is(httpStatus.value()))
        .andExpect(jsonPath("$.status").value(httpStatus.value()))
        .andExpect(jsonPath("$.message").value("Message"));   
    }

    @Test
    public void addPageReturnsServiceUnavailableWhenServiceThrowsApiUnavailable() throws Exception {
        var taskDto = new TaskDto();

        when(taskService.addTask(any(TaskDto.class))).thenThrow(new ApiUnavailable());

        mockMvc.perform(
            MockMvcRequestBuilders.post("/tasks/add")
            .flashAttr("taskDto", taskDto)
        )
        .andExpect(status().isServiceUnavailable())
        .andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))        
        .andExpect(jsonPath("$.message").value("API is unavailable"));   
    }

    @Test
    public void getTaskPageViewReturnsTaskView() throws Exception {
        var taskDto = new TaskDto();
        Map<String, String> map = new HashMap<>();
        
        taskDto.setId(1L);
        map.put("startDate", "");

        when(taskService.getTask(taskDto.getId())).thenReturn(taskDto);
        when(taskDtoFormatter.getFormattedDates(taskDto)).thenReturn(map);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + taskDto.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", taskDto))
                .andExpect(model().attribute("dates", map))
                .andExpect(view().name("taskView"));
    }

    @Test
    public void getTaskPageViewReturnsBadRequestWhenIdIsInvalid() throws Exception {
        var id = "invalid";

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getTaskPageViewReturnsAnErrorResponseWhenServiceThrowsHttpClientErrorException() throws Exception {
        var id = 1L;
        var httpStatus = HttpStatus.NOT_FOUND;

        var errorResponse = new ErrorResponse(httpStatus.value(), "Message");
        byte[] jsonBytes = TestHelper.convertErrorResponseToBytes(errorResponse);

        when(taskService.getTask(id)).thenThrow(new HttpClientErrorException(httpStatus, "", jsonBytes, StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.value()))
                .andExpect(jsonPath("$.message").value("Message"));
    }

    @Test
    public void getTaskPageViewReturnsServiceUnavailableWhenServiceThrowsApiUnavailable() throws Exception {
        var id = 1L;

        when(taskService.getTask(id)).thenThrow(new ApiUnavailable());

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
                .andExpect(jsonPath("$.message").value("API is unavailable"));
    }

    @Test
    public void deleteTaskRedirectsToMainViewWhenTaskIsDeleted() throws Exception {
        var id = 1L;
        var argumentCaptor = ArgumentCaptor.forClass(Long.class);
        ModelMap modelMap;

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andReturn();

        modelMap = mvcResult.getModelAndView().getModelMap();

        verify(taskService, times(1)).deleteTask(argumentCaptor.capture());
        assertEquals(id, argumentCaptor.getValue());
        assertTrue(modelMap.containsKey("message"));
        assertEquals("Task Deleted", modelMap.get("message"));
    }

    @Test
    public void deleteTaskReturnsBadRequestWhenIdIsInvalid() throws Exception {
        var id = "invalid";

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteTaskReturnsAnErrorResponseWhenServiceThrowsHttpClientErrorException() throws Exception {
        var id = 1L;
        var httpStatus = HttpStatus.NOT_FOUND;
        
        var ErrorResponse = new ErrorResponse(httpStatus.value(), "Message");
        var jsonBytes = TestHelper.convertErrorResponseToBytes(ErrorResponse);

        
        doThrow(new HttpClientErrorException(httpStatus, "", jsonBytes, StandardCharsets.UTF_8))
            .when(taskService)
            .deleteTask(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/" + id))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.value()))
                .andExpect(jsonPath("$.message").value("Message"));
    }

    @Test
    public void deleteTaskReturnsServiceUnavailableWhenServiceThrowsApiUnavailable() throws Exception {
        var id = 1L;

        doThrow(new ApiUnavailable())
            .when(taskService)
            .deleteTask(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/" + id))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
                .andExpect(jsonPath("$.message").value("API is unavailable"));
    }

    @Test
    public void getEditPageViewReturnsEditTaskView() throws Exception {
        var taskDto = new TaskDto();
        Map<String, String> map = new HashMap<>();
        
        taskDto.setId(1L);
        map.put("startDate", "");

        when(taskService.getTask(taskDto.getId())).thenReturn(taskDto);
        when(taskDtoFormatter.getFormattedDates(taskDto)).thenReturn(map);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/edit/" + taskDto.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("task", taskDto))
                .andExpect(model().attribute("dates", map))
                .andExpect(view().name("editTask"));
    }

    @Test
    public void getEditPageViewReturnsBadRequestWhenIdIsInvalid() throws Exception {
        var id = "invalid";

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/edit/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getEditPageViewReturnsAnErrorResponseWhenServiceThrowsHttpClientErrorException() throws Exception {
        var id = 1L;
        var httpStatus = HttpStatus.NOT_FOUND;

        var ErrorResponse = new ErrorResponse(httpStatus.value(), "Message");
        var jsonBytes = TestHelper.convertErrorResponseToBytes(ErrorResponse);

        
        when(taskService.getTask(id)).thenThrow(new HttpClientErrorException(httpStatus, "", jsonBytes, StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/edit/" + id))
                .andExpect(status().is(httpStatus.value()))
                .andExpect(jsonPath("$.status").value(httpStatus.value()))
                .andExpect(jsonPath("$.message").value("Message"));
    }

    @Test
    public void getEditPageViewReturnsServiceUnavailableWhenServiceThrowsApiUnavailable() throws Exception {
        var id = 1L;

        when(taskService.getTask(id)).thenThrow(new ApiUnavailable());

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/edit/" + id))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
                .andExpect(jsonPath("$.message").value("API is unavailable"));
    }

    @Test
    public void updateTaskRedirectsToTaskViewWhenTaskIsUpdated() throws Exception {
        var taskDto = new TaskDto();
        MvcResult mvcResult;
        ModelMap modelMap;
        var argumentCaptor = ArgumentCaptor.forClass(TaskDto.class);

        taskDto.setId(1L);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/tasks/edit")
            .flashAttr("taskDto", taskDto)
        )
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/tasks/" + taskDto.getId()))
        .andReturn();

        modelMap = mvcResult.getModelAndView().getModelMap();
        
        verify(taskService, times(1)).updateTask(argumentCaptor.capture());
        assertEquals(taskDto, argumentCaptor.getValue());
        assertTrue(modelMap.containsKey("message"));
        assertEquals("Task Updated", modelMap.get("message"));
    }

    @Test
    public void updateTaskReturnsAnErrorResponseWhenServiceThrowsHttpClientErrorException() throws Exception {
        var taskDto = new TaskDto();
        var httpStatus = HttpStatus.BAD_REQUEST;

        var errorResponse = new ErrorResponse(httpStatus.value(), "Message");
        byte[] jsonBytes = TestHelper.convertErrorResponseToBytes(errorResponse);

        doThrow(new HttpClientErrorException(httpStatus, "", jsonBytes, StandardCharsets.UTF_8))
            .when(taskService)
            .updateTask(taskDto);
        
        mockMvc.perform(
            MockMvcRequestBuilders.put("/tasks/edit")
            .flashAttr("taskDto", taskDto)
        )
        .andExpect(status().is(httpStatus.value()))
        .andExpect(jsonPath("$.status").value(httpStatus.value()))
        .andExpect(jsonPath("$.message").value("Message"));
    }
    
    @Test
    public void updateTaskReturnsServiceUnavailableWhenServiceThrowsApiUnavailable() throws Exception {
        var taskDto = new TaskDto();

        taskDto.setId(1L);

        doThrow(new ApiUnavailable())
            .when(taskService)
            .updateTask(taskDto);
        
        mockMvc.perform(
            MockMvcRequestBuilders.put("/tasks/edit")
            .flashAttr("taskDto", taskDto)
        )
        .andExpect(status().isServiceUnavailable())
        .andExpect(jsonPath("$.status").value(HttpStatus.SERVICE_UNAVAILABLE.value()))
        .andExpect(jsonPath("$.message").value("API is unavailable"));
    }

}
