package de.luisdeltoro.calorie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception indicates that the requested element has not been found in the database
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Element does not exists")
public class ObjectNotFoundException extends RuntimeException {

    /**
     * Builds a new instance
     * @param message the error message
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
