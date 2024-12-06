package ru.CarX.TechnicalTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.CarX.TechnicalTask.models.UserActivityData;

@Repository
public interface UserActivityDataRepository extends JpaRepository<UserActivityData, Long> {}
