package ru.CarX.TechnicalTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.CarX.TechnicalTask.DTO.NewUsersForCountryDTO;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.services.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с данными синхронизации пользователей.
 * <p>
 * Используется в {@link UserSyncDataService} для выполнения операций чтения и записи в базу данных.
 * </p>
 */
@Repository
public interface UserSyncDataRepository extends JpaRepository<UserSyncData, UUID> {

    /**
     * Находит данные пользователя по UUID.
     * <p>
     * Используется в {@link UserSyncDataService#findUserDataByUuid(UUID)}
     * для получения данных конкретного пользователя.
     * </p>
     *
     * @param uuid UUID пользователя.
     * @return {@link Optional}, содержащий данные пользователя, если они найдены.
     */
    Optional<UserSyncData> findByUuid(UUID uuid);

    /**
     * Получает топ пользователей с наибольшим балансом по каждой стране.
     * <p>
     * Используется в {@link UserSyncDataService#getUserTopByMoney(int)}
     * для формирования списка пользователей с самым большим количеством денег.
     * </p>
     *
     * @param usersAmount Количество пользователей в топе для каждой страны.
     * @return {@link Optional} со списком пользователей, отсортированных по количеству денег.
     */
    @Query(nativeQuery = true, value= "SELECT * \n" +
            "FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY country ORDER BY money DESC) AS rank FROM user_sync_data) AS ranked \n" +
            "WHERE rank <= :usersAmount")
    Optional<List<UserSyncData>> getUserTopByMoney(@Param("usersAmount") int usersAmount);

    /**
     * Подсчитывает количество новых пользователей за указанный период, сгруппированных по странам.
     * <p>
     * Используется в {@link UserSyncDataService#getNewUsersCount(LocalDateTime, LocalDateTime)}
     * для получения статистики новых пользователей по странам.
     * </p>
     *
     * @param fromDate Начальная дата периода.
     * @param toDate   Конечная дата периода.
     * @return {@link Optional} со списком {@link NewUsersForCountryDTO}, содержащим информацию
     * о количестве новых пользователей в каждой стране.
     */
    @Query(value = "SELECT new ru.CarX.TechnicalTask.DTO.NewUsersForCountryDTO(u.country, COUNT(u)) \n" +
            "FROM UserSyncData u \n" +
            "WHERE u.createdAt BETWEEN :startDate AND :endDate \n" +
            "GROUP BY u.country")
    Optional<List<NewUsersForCountryDTO>> getNewUsersForCountries(@Param("startDate") LocalDateTime fromDate,
                                                                  @Param("endDate") LocalDateTime toDate);
}
