package ru.CarX.TechnicalTask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.CarX.TechnicalTask.models.UserActivityData;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.services.UserActivityDataService;
import ru.CarX.TechnicalTask.services.UserSyncDataService;
import ru.CarX.TechnicalTask.util.UserDataErrorResponse;
import ru.CarX.TechnicalTask.util.UserSyncDataNotFoundException;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameDataController {

    private final UserActivityDataService userActivityDataService;

    private final UserSyncDataService userSyncDataService;

    @Autowired
    public GameDataController(UserActivityDataService userActivityDataService,
                              UserSyncDataService userSyncDataService) {
        this.userActivityDataService = userActivityDataService;
        this.userSyncDataService = userSyncDataService;
    }

    // Метод для приема данных синхронизации от пользователя
    @PostMapping("/sync")
    public ResponseEntity<Map<String, String>> receiveSyncData(@RequestBody @Valid UserSyncData userSyncData,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult
                    .getAllErrors()
                    .stream()
                    .map((x) -> x.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMsg);
        }
        userSyncDataService.updateUserData(userSyncData);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Data received");
        return ResponseEntity.ok(response);
    }

    // Метод для отправки данных пользователю
    @GetMapping("/sync/{uuid}")
    public ResponseEntity<UserSyncData> getUserData(@PathVariable String uuid) {
        UserSyncData userSyncData = userSyncDataService.findUserDataByUuid(uuid);
        return ResponseEntity.ok(userSyncData);
    }

    // Метод для приема игровой статистики от пользователя
    @PostMapping("/stats")
    public ResponseEntity<Map<String, String>> receiveActivity(@RequestBody @Valid UserActivityData userActivityData,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult
                    .getAllErrors()
                    .stream()
                    .map((x) -> x.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMsg);
        }
        userActivityDataService.saveActivityData(userActivityData);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Activity recorded");
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler
    private ResponseEntity<UserDataErrorResponse> userDataExceptionHandler(UserSyncDataNotFoundException exception) {
        UserDataErrorResponse errorResponse = new UserDataErrorResponse(
                "UserSyncData with that uuid was not found",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserDataErrorResponse> userValidationExceptionHandler(ValidationException exception) {
        UserDataErrorResponse errorResponse = new UserDataErrorResponse(
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

