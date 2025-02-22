package ru.CarX.TechnicalTask.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.CarX.TechnicalTask.DTO.UserActivityDTO;
import ru.CarX.TechnicalTask.models.UserActivityData;
import ru.CarX.TechnicalTask.repositories.UserActivityDataRepository;
import ru.CarX.TechnicalTask.util.UserActivityDataNotFoundException;
import ru.CarX.TechnicalTask.controllers.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис для работы с данными активности пользователей.
 * <p>
 * Используется в контроллерах:
 * <ul>
 *     <li>{@link AnalyticalDataController} - для получения аналитических данных пользователей за определенный период.</li>
 *     <li>{@link GameDataController} - для сохранения игровой активности пользователя.</li>
 * </ul>
 * </p>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserActivityDataService implements UserActivityDataServiceInterface {

    private final UserActivityDataRepository repository;

    /**
     * Сохраняет данные активности пользователя.
     * <p>
     * Используется в {@link GameDataController#receiveActivity(UserActivityData, BindingResult)}
     * для записи игровой статистики пользователя.
     * </p>
     *
     * @param activityData Данные активности пользователя.
     */
    @Override
    @Transactional
    public void saveActivityData(UserActivityData activityData) {
        repository.save(activityData);
    }

    /**
     * Получает список активности пользователя за указанный период.
     * <p>
     * Используется в {@link AnalyticalDataController#getActivityByPeriod(LocalDateTime, LocalDateTime, UUID)}
     * для получения активности пользователя в аналитических целях.
     * </p>
     *
     * @param uuid     UUID пользователя.
     * @param fromDate Начальная дата периода.
     * @param toDate   Конечная дата периода.
     * @return Список активности пользователя за указанный период, отсортированный по дате.
     * @throws UserActivityDataNotFoundException если данные активности не найдены.
     */
    @Override
    public List<UserActivityDTO> getUserActivityForPeriod(UUID uuid, LocalDateTime fromDate, LocalDateTime toDate) {
        Optional<List<UserActivityDTO>> userActivitiesList = repository.getUserActivityForPeriod(uuid, fromDate, toDate);
        return userActivitiesList
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new UserActivityDataNotFoundException("UserActivityData with that uuid was not found"));
    }
}

