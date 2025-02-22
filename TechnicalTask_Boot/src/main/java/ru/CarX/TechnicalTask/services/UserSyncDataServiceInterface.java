package ru.CarX.TechnicalTask.services;

import ru.CarX.TechnicalTask.DTO.NewUsersForCountryDTO;
import ru.CarX.TechnicalTask.models.UserSyncData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface UserSyncDataServiceInterface {

    void updateUserData(UserSyncData userSyncData);

    UserSyncData findUserDataByUuid(UUID uuid);

    List<UserSyncData> getUserTopByMoney(int topUsersAmount);

    List<NewUsersForCountryDTO> getNewUsersCount(LocalDateTime fromDate, LocalDateTime toDate);
}
