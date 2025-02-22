package ru.CarX.TechnicalTask.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class UserDataExceptionHandler {

    @ExceptionHandler({UserSyncDataNotFoundException.class, UserActivityDataNotFoundException.class})
    @ResponseBody
    public ResponseEntity<UserDataErrorResponse> handleUserDataException(RuntimeException exception) {
        UserDataErrorResponse errorResponse = new UserDataErrorResponse(
                exception.getMessage(),
                exception.getCause(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseBody
    private ResponseEntity<UserDataErrorResponse> userValidationExceptionHandler(ValidationException exception) {
        UserDataErrorResponse errorResponse = new UserDataErrorResponse(
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
