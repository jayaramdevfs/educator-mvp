package com.educator.repository;

import com.educator.notification.entity.Notification;
import com.educator.notification.entity.NotificationType;
import com.educator.notification.enums.NotificationStatus;
import com.educator.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationRepositoryDataJpaTest {

    @Autowired
    private NotificationRepository repository;

    @Test
    void countByUserIdAndReadFalse_countsOnlyUnreadForUser() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        saveNotification(userId, false, Instant.now().minusSeconds(10));
        saveNotification(userId, false, Instant.now().minusSeconds(5));
        saveNotification(userId, true, Instant.now().minusSeconds(1));
        saveNotification(otherUserId, false, Instant.now().minusSeconds(1));

        assertThat(repository.countByUserIdAndReadFalse(userId)).isEqualTo(2L);
    }

    @Test
    void findByUserIdOrderByCreatedAtDesc_returnsNewestFirst() {
        UUID userId = UUID.randomUUID();
        Notification oldest = saveNotification(userId, false, Instant.now().minusSeconds(30));
        Notification latest = saveNotification(userId, false, Instant.now().minusSeconds(5));

        var page = repository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getId()).isEqualTo(latest.getId());
        assertThat(page.getContent().get(1).getId()).isEqualTo(oldest.getId());
    }

    private Notification saveNotification(UUID userId, boolean read, Instant createdAt) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(NotificationType.EXAM_PASSED);
        notification.setTitle("title");
        notification.setMessage("message");
        notification.setStatus(NotificationStatus.PERSISTED);
        notification.setRead(read);
        ReflectionTestUtils.setField(notification, "createdAt", createdAt);
        return repository.save(notification);
    }
}


