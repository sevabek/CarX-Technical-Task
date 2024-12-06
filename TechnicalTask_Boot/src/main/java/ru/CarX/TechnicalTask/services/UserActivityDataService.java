package ru.CarX.TechnicalTask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.CarX.TechnicalTask.models.UserActivityData;
import ru.CarX.TechnicalTask.models.UserSyncData;
import ru.CarX.TechnicalTask.repositories.UserActivityDataRepository;

@Service
public class UserActivityDataService {
    private final UserActivityDataRepository repository;

    private final UserSyncDataService syncDataService;

    @Autowired
    public UserActivityDataService(UserActivityDataRepository repository,
                                   UserSyncDataService syncDataService) {
        this.repository = repository;
        this.syncDataService = syncDataService;
    }

    public void saveActivityData(UserActivityData activityData) {
        UserSyncData existingUserSyncData = syncDataService
                .findUserDataByUuid(activityData.getUserSyncData().getUuid());
        activityData.setUserSyncData(existingUserSyncData);
        repository.save(activityData);
    }
}
