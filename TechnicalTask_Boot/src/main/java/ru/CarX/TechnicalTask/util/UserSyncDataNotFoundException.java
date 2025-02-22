package ru.CarX.TechnicalTask.util;

public class UserSyncDataNotFoundException extends RuntimeException {

    public UserSyncDataNotFoundException(String message) {
        super(message);
    }

    public UserSyncDataNotFoundException() {}

    public UserSyncDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserSyncDataNotFoundException(Throwable cause) {
        super(cause);
    }
}
