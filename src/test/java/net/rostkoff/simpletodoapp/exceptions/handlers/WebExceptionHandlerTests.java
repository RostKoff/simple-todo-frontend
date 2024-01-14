package net.rostkoff.simpletodoapp.exceptions.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.rostkoff.simpletodoapp.TestHelper;
import net.rostkoff.simpletodoapp.exceptions.ApiUnavailable;
import net.rostkoff.simpletodoapp.exceptions.ErrorResponse;

public class WebExceptionHandlerTests {
    private WebExceptionHandler webExceptionHandler;

    @BeforeEach
    public void setUp() {
        webExceptionHandler = new WebExceptionHandler();
    }

    @Test
    public void handleHttpClientErrorException() throws Exception {
        var httpStatus = HttpStatus.BAD_REQUEST;
        var errorResponse = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase());
        var objectMapper = new ObjectMapper();
        Map<String, Object> responseBody;
        var jsonBytes = TestHelper.convertErrorResponseToBytes(errorResponse);

        var response = webExceptionHandler.handleHttpClientErrorException(
            new HttpClientErrorException(httpStatus, "", jsonBytes, StandardCharsets.UTF_8),
            null
        );

        // Convert the response body to a Map
        responseBody = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});

        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals(httpStatus, response.getStatusCode());
        assertTrue(responseBody.containsKey("status"));
        assertTrue(responseBody.containsKey("message"));
        assertEquals(errorResponse.getStatus(), responseBody.get("status"));
        assertEquals(errorResponse.getMessage(), responseBody.get("message"));
    }

    @Test
    public void handleApiUnavailable() {
        var message = "API is unavailable";
        var response = webExceptionHandler.handleApiUnavailable(new ApiUnavailable(message), null);
        var expectedResponseBody = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), message);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }


}
