package ru.carX.TechnicalTask;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.CarX.TechnicalTask.TechnicalTaskBootApplication;
import ru.CarX.TechnicalTask.controllers.GameDataController;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.services.UserActivityDataService;
import ru.CarX.TechnicalTask.services.UserSyncDataService;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameDataController.class)
@ContextConfiguration(classes = TechnicalTaskBootApplication.class)
public class GameDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserActivityDataService userActivityDataService;

    @MockBean
    private UserSyncDataService userSyncDataService;

    @Test
    void receiveSyncData_ShouldReturnOk() throws Exception {
        String userSyncDataJson =
                        "{\n" +
                        "  \"uuid\": \"1e5\",\n" +
                        "  \"money\": 100,\n" +
                        "  \"country\": \"RU\",\n" +
                        "  \"createdAt\": \"2023-12-04T21:24:00\"\n" +
                        "}";

        mockMvc.perform(post("/api/sync")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userSyncDataJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Data received"));

        Mockito.verify(userSyncDataService, Mockito.times(1)).updateUserData(Mockito.any());
    }

    @Test
    void getUserData_ShouldReturnUserSyncData() throws Exception {
        UserSyncData userSyncData = new UserSyncData("1e5", 500, "RU", LocalDateTime.now(), null);
        Mockito.when(userSyncDataService.findUserDataByUuid("1e5")).thenReturn(userSyncData);

        mockMvc.perform(get("/api/sync/1e5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value("1e5"))
                .andExpect(jsonPath("$.money").value(500));

        Mockito.verify(userSyncDataService, Mockito.times(1)).findUserDataByUuid("1e5");
    }

    @Test
    void receiveActivity_ShouldReturnOk() throws Exception {
        String userActivityDataJson =
                        "{\n" +
                        "  \"activity\": 777,\n" +
                        "  \"timestamp\": \"2023-10-10T15:30:00\",\n" +
                        "  \"userSyncData\": {\n" +
                        "    \"uuid\": \"1e5\"\n" +
                        "  }\n" +
                        "}";

        mockMvc.perform(post("/api/stats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userActivityDataJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Activity recorded"));

        Mockito.verify(userActivityDataService, Mockito.times(1)).saveActivityData(Mockito.any());
    }
}
