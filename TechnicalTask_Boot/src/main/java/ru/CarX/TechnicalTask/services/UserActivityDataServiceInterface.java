package ru.CarX.TechnicalTask.services;

import ru.CarX.TechnicalTask.DTO.UserActivityDTO;
import ru.CarX.TechnicalTask.models.UserActivityData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserActivityDataServiceInterface {

    void saveActivityData(UserActivityData activityData);

    List<UserActivityDTO> getUserActivityForPeriod(UUID uuid, LocalDateTime fromDate, LocalDateTime toDate);
}

