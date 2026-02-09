package com.educator.notification.service;

import com.educator.notification.dto.NotificationCreateRequest;
import com.educator.notification.entity.Notification;
import com.educator.notification.enums.NotificationStatus;
import com.educator.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationPersistenceService {

    private final NotificationRepository repository;

    public NotificationPersistenceService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification persist(NotificationCreateRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setStatus(NotificationStatus.PERSISTED);
        notification.setRead(false);

        return repository.save(notification);
    }
}
