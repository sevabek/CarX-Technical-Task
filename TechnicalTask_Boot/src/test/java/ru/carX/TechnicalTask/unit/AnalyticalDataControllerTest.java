package ru.carX.TechnicalTask.unit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.CarX.TechnicalTask.DTO.NewUsersForCountryDTO;
import ru.CarX.TechnicalTask.DTO.UserActivityDTO;
import ru.CarX.TechnicalTask.TechnicalTaskBootApplication;
import ru.CarX.TechnicalTask.controllers.AnalyticalDataController;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.services.UserActivityDataService;
import ru.CarX.TechnicalTask.services.UserSyncDataService;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticalDataController.class)
@ContextConfiguration(classes = TechnicalTaskBootApplication.class)
public class AnalyticalDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserActivityDataService userActivityDataService;

    @MockBean
    private UserSyncDataService userSyncDataService;

    @Test
    void getUsersMoneyTop_ShouldReturnMap() throws Exception {
        List<UserSyncData> userSyncDataList = new ArrayList<>();
        userSyncDataList.add(new UserSyncData(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), 500, "RU", LocalDateTime.now()));
        userSyncDataList.add(new UserSyncData(UUID.fromString("123e4567-e89b-12d3-a456-556642440001"), 2500, "RU", LocalDateTime.now().minusDays(2)));
        userSyncDataList.add(new UserSyncData(UUID.fromString("123e4567-e89b-12d3-a456-556642440002"), 1500, "US", LocalDateTime.now().minusDays(5)));

        Mockito.when(userSyncDataService.getUserTopByMoney(10)).thenReturn(userSyncDataList);


        mockMvc.perform(get("/analytics/moneyTopPerCountry?usersAmount=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.RU[0].uuid").value("123e4567-e89b-12d3-a456-556642440000"))
                .andExpect(jsonPath("$.RU[0].money").value(500))
                .andExpect(jsonPath("$.RU[1].uuid").value("123e4567-e89b-12d3-a456-556642440001"))
                .andExpect(jsonPath("$.RU[1].money").value(2500))
                .andExpect(jsonPath("$.US[0].uuid").value("123e4567-e89b-12d3-a456-556642440002"))
                .andExpect(jsonPath("$.US[0].money").value(1500));

        Mockito.verify(userSyncDataService, Mockito.times(1)).getUserTopByMoney(10);
    }

    @Test
    void getNewUsersCount_ShouldReturnList() throws Exception {
        LocalDateTime toTime = LocalDateTime.of(2025, 1, 22, 20, 20);
        LocalDateTime fromTime = toTime.minusDays(3);
        List<NewUsersForCountryDTO> newUsersForCountriesList = new ArrayList<>();
        newUsersForCountriesList.add(new NewUsersForCountryDTO("RU", 23));
        newUsersForCountriesList.add(new NewUsersForCountryDTO("US", 62));

        Mockito.when(userSyncDataService.getNewUsersCount(fromTime, toTime)).thenReturn(newUsersForCountriesList);

        mockMvc.perform(get("/analytics/newUsers?fromTime=" + fromTime + "&toTime=" + toTime))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.RU").value(23))
                .andExpect(jsonPath("$.US").value(62));

        Mockito.verify(userSyncDataService, Mockito.times(1)).getNewUsersCount(fromTime, toTime);
    }

    @Test
    void getActivityByPeriod_ShouldReturnList() throws Exception {
        LocalDateTime toTime = LocalDateTime.of(2025, 1, 22, 20, 20);
        LocalDateTime fromTime = toTime.minusDays(1);
        List<UserActivityDTO> userActivitiesList = new ArrayList<>();
        userActivitiesList.add(new UserActivityDTO(23, LocalDateTime.of(2025, 1, 21, 20, 20)));
        userActivitiesList.add(new UserActivityDTO(65, LocalDateTime.of(2025, 1, 21, 20, 30)));
        userActivitiesList.add(new UserActivityDTO(12, LocalDateTime.of(2025, 1, 21, 20, 40)));

        Mockito.when(userActivityDataService.getUserActivityForPeriod(
                UUID.fromString("123e4567-e89b-12d3-a456-556642440000"), fromTime, toTime))
                .thenReturn(userActivitiesList);


        mockMvc.perform(get("/analytics/userActivity/123e4567-e89b-12d3-a456-556642440000?fromTime=" +
                        fromTime + "&toTime=" + toTime))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].activity").value(23))
                .andExpect(jsonPath("$.[0].timestamp").value("2025-01-21T20:20:00"))
                .andExpect(jsonPath("$.[2].activity").value(12))
                .andExpect(jsonPath("$.[2].timestamp").value("2025-01-21T20:40:00"));

        Mockito.verify(userActivityDataService, Mockito.times(1))
                .getUserActivityForPeriod(UUID.fromString("123e4567-e89b-12d3-a456-556642440000"),
                        fromTime, toTime);
    }
}
