package ru.CarX.TechnicalTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.CarX.TechnicalTask.models.UserSyncData;

import java.util.Optional;

@Repository
public interface UserSyncDataRepository extends JpaRepository<UserSyncData, Long> {
    Optional<UserSyncData> findByUuid(String uuid);
}
