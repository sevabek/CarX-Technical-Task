package ru.CarX.TechnicalTask.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDataErrorResponse {

    private String message;

    private Throwable cause;

    private LocalDateTime timestamp;

    public UserDataErrorResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
