package ru.CarX.TechnicalTask.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class UserSyncDataDTO {

    private UUID uuid;

    private int money;

    private String country;

    private LocalDateTime createdAt;
}
