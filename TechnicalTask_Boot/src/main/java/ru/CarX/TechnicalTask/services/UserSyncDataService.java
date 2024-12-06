package ru.CarX.TechnicalTask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.repositories.UserSyncDataRepository;
import ru.CarX.TechnicalTask.util.UserSyncDataNotFoundException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserSyncDataService {
    private final UserSyncDataRepository repository;

    @Autowired
    public UserSyncDataService(UserSyncDataRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void updateUserData(UserSyncData userSyncData) {
        UserSyncData existingUserSyncData = repository.findByUuid(userSyncData.getUuid()).orElse(null);
        if (existingUserSyncData != null) {
            existingUserSyncData.setMoney(userSyncData.getMoney());
            repository.save(existingUserSyncData);
        } else {
            repository.save(userSyncData);
        }
    }

    public UserSyncData findUserDataByUuid(String uuid) {
        Optional<UserSyncData> userSyncData = repository.findByUuid(uuid);
        if (userSyncData.isPresent()) {
            return userSyncData.get();
        }
        throw new UserSyncDataNotFoundException();
    }
}
