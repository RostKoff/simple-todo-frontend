package net.rostkoff.simpletodoapp.exceptions;

public class ApiUnavailable extends RuntimeException{
    public ApiUnavailable(String message) {
        super(message);
    }
    
    public ApiUnavailable() {
        super("API is unavailable");
    }
}
