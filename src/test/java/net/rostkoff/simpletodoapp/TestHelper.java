package net.rostkoff.simpletodoapp;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.rostkoff.simpletodoapp.exceptions.ErrorResponse;

public class TestHelper {
    // Converts ErrorResponse to JSON bytes in order to pass it to HttpClientErrorException
    public static byte[] convertErrorResponseToBytes(ErrorResponse errorResponse) throws Exception {
        var objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(errorResponse).getBytes();
    }
}
