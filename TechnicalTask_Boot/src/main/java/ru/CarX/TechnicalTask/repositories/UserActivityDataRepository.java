package ru.CarX.TechnicalTask.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.CarX.TechnicalTask.DTO.UserActivityDTO;
import ru.CarX.TechnicalTask.models.UserActivityData;
import ru.CarX.TechnicalTask.services.UserActivityDataService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с данными синхронизации пользователей.
 * <p>
 * Используется в {@link UserActivityDataService}
 * для выполнения операций чтения и записи в базу данных.
 * </p>
 */
@Repository
public interface UserActivityDataRepository extends CassandraRepository<UserActivityData, UUID> {

    /**
     * Получает все записи активности пользователя за определенный промежуток времени, отсортированных по дате.
     * <p>
     * Используется в {@link UserActivityDataService#getUserActivityForPeriod(UUID, LocalDateTime, LocalDateTime)}
     * для получения данных конкретного пользователя.
     * </p>
     *
     * @param uuid      UUID пользователя.
     * @param fromDate  Начальная дата периода.
     * @param toDate    Конечная дата периода.
     * @return {@link Optional} со списком {@link UserActivityDTO}, содержащим информацию
     * о всех записях активности пользователя за период.
     */
    @Query("SELECT activity, timestamp FROM user_activity_data \n" +
            "WHERE uuid = :uuid AND timestamp >= :startDate \n" +
            "AND timestamp <= :endDate \n" +
            "ORDER BY timestamp")
    Optional<List<UserActivityDTO>> getUserActivityForPeriod(@Param("uuid") UUID uuid,
                                                             @Param("startDate") LocalDateTime fromDate,
                                                             @Param("endDate") LocalDateTime toDate);
}
