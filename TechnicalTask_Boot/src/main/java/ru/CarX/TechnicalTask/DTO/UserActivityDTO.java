package ru.CarX.TechnicalTask.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserActivityDTO {

    private int activity;

    private LocalDateTime timestamp;
}
