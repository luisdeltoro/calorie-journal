package de.luisdeltoro.calorie.ws;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

/**
 * Controller Advice class to centralize the conversion of java exceptions to HTTP Status codes
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maps {@link NoSuchElementException} to 404 Not Found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void handleNoSuchElementException() {
        // do nothing
    }
}