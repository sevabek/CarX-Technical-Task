package ru.carX.TechnicalTask.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.CarX.TechnicalTask.DTO.NewUsersForCountryDTO;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.repositories.UserSyncDataRepository;
import ru.CarX.TechnicalTask.services.UserSyncDataService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserSyncDataServiceTest {

    @Mock
    private UserSyncDataRepository repository;

    @InjectMocks
    private UserSyncDataService userSyncDataService;

    @Test
    void updateUserData_ShouldUpdateExistingData() {
        UUID uuid = UUID.randomUUID();
        UserSyncData existingUser = new UserSyncData(uuid, 50, "RU", LocalDateTime.now());
        UserSyncData updatedUser = new UserSyncData(uuid, 200, "US", LocalDateTime.now());
        Mockito.when(repository.findByUuid(uuid)).thenReturn(Optional.of(existingUser));

        userSyncDataService.updateUserData(updatedUser);

        Mockito.verify(repository, Mockito.times(1)).save(existingUser);
        Assertions.assertEquals(200, existingUser.getMoney());
        Assertions.assertEquals("US", existingUser.getCountry());
    }

    @Test
    void findUserDataByUuid_ShouldReturnData() {
        UUID uuid = UUID.randomUUID();
        UserSyncData user = new UserSyncData(uuid, 300, "FR", LocalDateTime.now());
        Mockito.when(repository.findByUuid(uuid)).thenReturn(Optional.of(user));

        UserSyncData result = userSyncDataService.findUserDataByUuid(uuid);

        Mockito.verify(repository, Mockito.times(1)).findByUuid(uuid);
        Assertions.assertEquals(user, result);
    }

    @Test
    void getUserTopByMoney_ShouldReturnTopUsers() {
        int topUsersAmount = 3;
        List<UserSyncData> topUsers = new ArrayList<>();
        topUsers.add(new UserSyncData(UUID.randomUUID(), 1000, "RU", LocalDateTime.now()));
        topUsers.add(new UserSyncData(UUID.randomUUID(), 800, "US", LocalDateTime.now()));
        topUsers.add(new UserSyncData(UUID.randomUUID(), 600, "JP", LocalDateTime.now()));
        Mockito.when(repository.getUserTopByMoney(topUsersAmount)).thenReturn(Optional.of(topUsers));

        List<UserSyncData> result = userSyncDataService.getUserTopByMoney(topUsersAmount);

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(1000, result.get(0).getMoney());
        Mockito.verify(repository, Mockito.times(1)).getUserTopByMoney(topUsersAmount);
    }

    @Test
    void getNewUsersCount_ShouldReturnNewUsersCountPerCountry() {
        LocalDateTime fromDate = LocalDateTime.now().minusDays(7);
        LocalDateTime toDate = LocalDateTime.now();
        List<NewUsersForCountryDTO> newUsers = new ArrayList<>();
        newUsers.add(new NewUsersForCountryDTO("RU", 10L));
        newUsers.add(new NewUsersForCountryDTO("US", 5L));
        Mockito.when(repository.getNewUsersForCountries(fromDate, toDate)).thenReturn(Optional.of(newUsers));

        List<NewUsersForCountryDTO> result = userSyncDataService.getNewUsersCount(fromDate, toDate);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("RU", result.get(0).getCountry());
        Assertions.assertEquals(10L, result.get(0).getUsersCount());
        Mockito.verify(repository, Mockito.times(1)).getNewUsersForCountries(fromDate, toDate);
    }
}
