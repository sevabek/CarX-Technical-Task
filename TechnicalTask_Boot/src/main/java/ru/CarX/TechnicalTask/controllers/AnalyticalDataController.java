package ru.CarX.TechnicalTask.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.CarX.TechnicalTask.DTO.NewUsersForCountryDTO;
import ru.CarX.TechnicalTask.DTO.UserActivityDTO;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.services.UserActivityDataService;
import ru.CarX.TechnicalTask.services.UserSyncDataService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST контроллер для получения аналитических данных.
 * Предоставляет API для получения информации о пользователях, отсортированных по количеству денег,
 * подсчета новых пользователей по странам за определенный период и получения активности пользователя за период.
 */
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AnalyticalDataController {

    private final UserActivityDataService userActivityDataService;
    private final UserSyncDataService userSyncDataService;

    /**
     * Возвращает список из Х пользователей с самым большим значением "money" для каждой страны.
     *
     * @param usersAmount Количество пользователей для возврата для каждой страны (X).
     * @return ResponseEntity с картой, где ключ - страна, а значение - список пользователей, отсортированных по количеству денег.
     */
    @GetMapping("/moneyTopPerCountry")
    public ResponseEntity<Map<String, List<UserSyncData>>> getUsersMoneyTop(@RequestParam("usersAmount") int usersAmount) {
        List<UserSyncData> topUsersByMoney = userSyncDataService.getUserTopByMoney(usersAmount);

        Map<String, List<UserSyncData>> groupedByCountry = topUsersByMoney.stream()
                .collect(Collectors.groupingBy(UserSyncData::getCountry));
        return ResponseEntity.ok(groupedByCountry);
    }

    /**
     * Возвращает количество новых пользователей для каждой страны за указанный период.
     *
     * @param fromDate Начальная дата периода (X).
     * @param toDate   Конечная дата периода (X).
     * @return ResponseEntity с картой, где ключ - страна, а значение - количество новых пользователей.
     */
    @GetMapping("/newUsers")
    public ResponseEntity<Map<String, Long>>
    getNewUsersCount(@RequestParam("fromTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
                     @RequestParam("toTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        List<NewUsersForCountryDTO> newUsersForCountriesList = userSyncDataService.getNewUsersCount(fromDate, toDate);

        Map<String, Long> newUsersForCountriesMap = newUsersForCountriesList.stream().collect(Collectors.toMap(
                NewUsersForCountryDTO::getCountry,
                NewUsersForCountryDTO::getUsersCount
        ));
        return ResponseEntity.ok(newUsersForCountriesMap);
    }

    /**
     * Возвращает отсортированный по дате список значений показателя "activity" для указанного пользователя за указанный период.
     *
     * @param fromDate Начальная дата периода (Y).
     * @param toDate   Конечная дата периода (Y).
     * @param uuid     UUID пользователя (X).
     * @return ResponseEntity со списком UserActivityDTO, отсортированным по дате.
     */
    @GetMapping("/userActivity/{uuid}")
    public ResponseEntity<List<UserActivityDTO>>
    getActivityByPeriod(@RequestParam("fromTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
                        @RequestParam("toTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
                        @PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(userActivityDataService.getUserActivityForPeriod(uuid, fromDate, toDate));
    }
}
