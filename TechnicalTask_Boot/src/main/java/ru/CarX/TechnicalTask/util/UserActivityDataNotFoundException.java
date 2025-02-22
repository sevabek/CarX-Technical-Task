package ru.CarX.TechnicalTask.util;

public class UserActivityDataNotFoundException extends RuntimeException {

    public UserActivityDataNotFoundException(String message) {
        super(message);
    }

    public UserActivityDataNotFoundException() {}

    public UserActivityDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserActivityDataNotFoundException(Throwable cause) {
        super(cause);
    }
}
