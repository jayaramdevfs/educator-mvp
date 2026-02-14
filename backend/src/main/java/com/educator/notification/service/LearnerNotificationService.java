package com.educator.notification.service;

import com.educator.notification.entity.Notification;
import com.educator.notification.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LearnerNotificationService {

    private final NotificationRepository notificationRepository;

    public LearnerNotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public Page<Notification> list(UUID userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Notification markRead(UUID userId, UUID notificationId) {

        // 🔒 Secure DB-level ownership validation
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        // Idempotent guard
        if (notification.isRead()) {
            return notification;
        }

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public long unreadCount(UUID userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }
}
