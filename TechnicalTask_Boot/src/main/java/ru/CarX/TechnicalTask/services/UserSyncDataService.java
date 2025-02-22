package ru.CarX.TechnicalTask.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.CarX.TechnicalTask.DTO.NewUsersForCountryDTO;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.repositories.UserSyncDataRepository;
import ru.CarX.TechnicalTask.util.UserSyncDataNotFoundException;
import ru.CarX.TechnicalTask.controllers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с данными синхронизации пользователей.
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
public class UserSyncDataService implements UserSyncDataServiceInterface {

    private final UserSyncDataRepository repository;

    /**
     * Обновляет данные пользователя или создает новую запись, если пользователя нет в базе.
     * <p>
     * Используется в {@link GameDataController#receiveSyncData(UserSyncData, BindingResult)}
     * для приема данных синхронизации от пользователя.
     * </p>
     *
     * @param userSyncData Данные пользователя для обновления или сохранения.
     */
    @Override
    @Transactional
    public void updateUserData(UserSyncData userSyncData) {
        Optional<UserSyncData> existingUserSyncData = repository.findByUuid(userSyncData.getUuid());
        if (existingUserSyncData.isPresent()) {
            UserSyncData userSyncDataToUpdate = existingUserSyncData.get();
            userSyncDataToUpdate.setMoney(userSyncData.getMoney());
            userSyncDataToUpdate.setCountry(userSyncData.getCountry());
            repository.save(userSyncDataToUpdate);
        } else {
            repository.save(userSyncData);
        }
    }

    /**
     * Находит данные пользователя по UUID.
     * <p>
     * Используется в {@link GameDataController#getUserData(UUID)} для получения данных пользователя.
     * </p>
     *
     * @param uuid UUID пользователя.
     * @return Данные пользователя.
     * @throws UserSyncDataNotFoundException если данные пользователя не найдены.
     */
    @Override
    public UserSyncData findUserDataByUuid(UUID uuid) {
        Optional<UserSyncData> userSyncData = repository.findByUuid(uuid);
        return userSyncData
                .orElseThrow(() -> new UserSyncDataNotFoundException("UserSyncData with that uuid was not found"));
    }

    /**
     * Получает топ пользователей с наибольшим балансом.
     * <p>
     * Используется в {@link AnalyticalDataController#getUsersMoneyTop(int)} для
     * получения списка пользователей с самым большим количеством денег.
     * </p>
     *
     * @param topUsersAmount Количество пользователей в топе.
     * @return Список пользователей, отсортированный по количеству денег.
     * @throws UserSyncDataNotFoundException если данные не найдены.
     */
    @Override
    public List<UserSyncData> getUserTopByMoney(int topUsersAmount) {
        Optional<List<UserSyncData>> topUsersList = repository.getUserTopByMoney(topUsersAmount);
        return topUsersList
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new UserSyncDataNotFoundException("UserSyncData with that uuid was not found"));
    }

    /**
     * Подсчитывает количество новых пользователей за указанный период.
     * <p>
     * Используется в {@link AnalyticalDataController#getNewUsersCount(LocalDateTime, LocalDateTime)}
     * для получения количества новых пользователей по странам.
     * </p>
     *
     * @param fromDate Начальная дата периода.
     * @param toDate   Конечная дата периода.
     * @return Список объектов {@link NewUsersForCountryDTO}, содержащих информацию
     * о количестве новых пользователей в каждой стране.
     * @throws UserSyncDataNotFoundException если данные не найдены.
     */
    @Override
    public List<NewUsersForCountryDTO> getNewUsersCount(LocalDateTime fromDate, LocalDateTime toDate) {
        Optional<List<NewUsersForCountryDTO>> usersCountList = repository.getNewUsersForCountries(fromDate, toDate);
        return usersCountList
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new UserSyncDataNotFoundException("UserSyncData with that uuid was not found"));
    }
}
