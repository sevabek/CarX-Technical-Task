package ru.carX.TechnicalTask.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.CarX.TechnicalTask.DTO.UserActivityDTO;
import ru.CarX.TechnicalTask.models.UserActivityData;
import ru.CarX.TechnicalTask.repositories.UserActivityDataRepository;
import ru.CarX.TechnicalTask.services.UserActivityDataService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserActivityDataServiceTest {

    @Mock
    private UserActivityDataRepository repository;

    @InjectMocks
    private UserActivityDataService userActivityDataService;

    @Test
    void saveActivityData_ShouldCallSaveMethod() {
        UserActivityData activityData = new UserActivityData(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), LocalDateTime.now(), 123);

        userActivityDataService.saveActivityData(activityData);

        Mockito.verify(repository, Mockito.times(1)).save(activityData);
    }

    @Test
    void getUserActivityForPeriod_ShouldReturnData() {
        UUID uuid = UUID.randomUUID();
        LocalDateTime from = LocalDateTime.now().minusDays(7);
        LocalDateTime to = LocalDateTime.now();
        List<UserActivityDTO> activities = new ArrayList<>();
        activities.add(new UserActivityDTO(123, LocalDateTime.now()));
        Mockito.when(repository.getUserActivityForPeriod(uuid, from, to)).thenReturn(Optional.of(activities));

        List<UserActivityDTO> result = userActivityDataService.getUserActivityForPeriod(uuid, from, to);

        Assertions.assertEquals(activities, result);
    }
}

