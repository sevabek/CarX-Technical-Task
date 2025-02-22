package ru.CarX.TechnicalTask.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.CarX.TechnicalTask.DTO.UserSyncDataDTO;
import ru.CarX.TechnicalTask.models.UserActivityData;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.services.UserActivityDataService;
import ru.CarX.TechnicalTask.services.UserSyncDataService;
import ru.CarX.TechnicalTask.util.BindingResultErrorHandler;

import javax.validation.Valid;
import java.util.*;

/**
 * REST контроллер для обработки данных игры.
 * Предоставляет три эндпоинта для синхронизации данных пользователя, получения данных пользователя и приема игровой статистики.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GameDataController {

    private final UserActivityDataService userActivityDataService;
    private final UserSyncDataService userSyncDataService;
    private final BindingResultErrorHandler bindingResultErrorHandler;
    private final ModelMapper modelMapper;

    /**
     * Принимает данные синхронизации от пользователя.
     *
     * @param userSyncData Данные синхронизации пользователя в формате JSON.
     * @param bindingResult Результат валидации данных.
     * @return Подтверждение получения данных.
     */
    @PostMapping("/sync")
    public ResponseEntity<Map<String, String>> receiveSyncData(@RequestBody @Valid UserSyncData userSyncData,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResultErrorHandler.handleError(bindingResult);
        }
        userSyncDataService.updateUserData(userSyncData);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Data received");
        return ResponseEntity.ok(response);
    }

    /**
     * Отправляет данные пользователя.
     *
     * @param uuid UUID пользователя.
     * @return Данные пользователя в формате JSON.
     */
    @GetMapping("/sync/{uuid}")
    public ResponseEntity<UserSyncDataDTO> getUserData(@PathVariable("uuid") UUID uuid) {
        UserSyncData userSyncData = userSyncDataService.findUserDataByUuid(uuid);
        UserSyncDataDTO userSyncDataDTO = convertToUserSyncDataDTO(userSyncData);
        return ResponseEntity.ok(userSyncDataDTO);
    }

    /**
     * Принимает игровую статистику от пользователя.
     *
     * @param userActivityData Данные активности пользователя в формате JSON.
     * @param bindingResult Результат валидации данных.
     * @return Подтверждение получения данных.
     */
    @PostMapping("/stats")
    public ResponseEntity<Map<String, String>> receiveActivity(@RequestBody @Valid UserActivityData userActivityData,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResultErrorHandler.handleError(bindingResult);
        }
        userActivityDataService.saveActivityData(userActivityData);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Activity recorded");
        return ResponseEntity.ok(response);
    }

    private UserSyncDataDTO convertToUserSyncDataDTO(UserSyncData userSyncData) {
        return modelMapper.map(userSyncData, UserSyncDataDTO.class);
    }
}

