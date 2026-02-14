package com.educator.notification.service;

import com.educator.notification.entity.Notification;
import com.educator.notification.entity.NotificationType;
import com.educator.notification.enums.NotificationStatus;
import com.educator.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class NotificationPersistenceService {

    private final NotificationRepository notificationRepository;

    public NotificationPersistenceService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public Notification persist(UUID userId,
                                NotificationType type,
                                String title,
                                String message) {

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setStatus(NotificationStatus.PERSISTED);
        notification.setRead(false);

        return notificationRepository.save(notification);
    }

    // B3.1
    @Transactional
    public void notifyCourseCompleted(UUID userId, Long courseId) {
        persist(
                userId,
                NotificationType.COURSE_COMPLETED,
                "Course Completed",
                "You have successfully completed the course (ID: " + courseId + ")."
        );
    }

    // 🔵 B3.2 - Exam Passed
    @Transactional
    public void notifyExamPassed(UUID userId, Long courseId) {
        persist(
                userId,
                NotificationType.EXAM_PASSED,
                "Exam Passed",
                "Congratulations! You passed the exam for course (ID: " + courseId + ")."
        );
    }

    // 🔵 B3.2 - Exam Failed
    @Transactional
    public void notifyExamFailed(UUID userId, Long courseId) {
        persist(
                userId,
                NotificationType.EXAM_FAILED,
                "Exam Failed",
                "Your exam attempt for course (ID: " + courseId + ") was not successful."
        );
    }
}
