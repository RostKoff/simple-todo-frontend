package net.rostkoff.simpletodoapp.exceptions.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.rostkoff.simpletodoapp.exceptions.ApiUnavailable;
import net.rostkoff.simpletodoapp.exceptions.ErrorResponse;

@ControllerAdvice
public class WebExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(ex.getResponseBodyAsString(), headers, ex.getStatusCode());
    }

    @ExceptionHandler(ApiUnavailable.class)
    public ResponseEntity<ErrorResponse> handleApiUnavailable(ApiUnavailable ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), ex.getMessage());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
