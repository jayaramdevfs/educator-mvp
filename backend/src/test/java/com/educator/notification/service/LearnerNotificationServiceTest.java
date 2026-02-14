package com.educator.notification.service;

import com.educator.notification.entity.Notification;
import com.educator.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LearnerNotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private LearnerNotificationService service;

    @Test
    void list_delegatesToRepository() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Notification> page = new PageImpl<>(List.of(notificationFor(userId)));
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)).thenReturn(page);

        Page<Notification> result = service.list(userId, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(notificationRepository).findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Test
    void unreadCount_delegatesToRepository() {
        UUID userId = UUID.randomUUID();
        when(notificationRepository.countByUserIdAndReadFalse(userId)).thenReturn(4L);

        long count = service.unreadCount(userId);

        assertThat(count).isEqualTo(4L);
    }

    @Test
    void markRead_marksNotificationAndSavesWhenOwnedByUser() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        Notification notification = notificationFor(userId);
        when(notificationRepository.findByIdAndUserId(notificationId, userId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = service.markRead(userId, notificationId);

        assertThat(result.isRead()).isTrue();
        verify(notificationRepository).save(notification);
    }

    @Test
    void markRead_throwsWhenNotificationMissing() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.findByIdAndUserId(notificationId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markRead(userId, notificationId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Notification not found");
    }

    @Test
    void markRead_doesNotSaveWhenNotificationMissing() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.findByIdAndUserId(notificationId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markRead(userId, notificationId))
                .isInstanceOf(IllegalArgumentException.class);

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void markRead_throwsWhenNotificationBelongsToDifferentUser() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        // findByIdAndUserId returns empty when userId doesn't match
        when(notificationRepository.findByIdAndUserId(notificationId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markRead(userId, notificationId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Notification not found");
    }

    @Test
    void markRead_doesNotSaveWhenNotificationBelongsToDifferentUser() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.findByIdAndUserId(notificationId, userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markRead(userId, notificationId))
                .isInstanceOf(IllegalArgumentException.class);

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void markRead_returnsEarlyWhenAlreadyRead() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        Notification notification = notificationFor(userId);
        notification.setRead(true);
        when(notificationRepository.findByIdAndUserId(notificationId, userId)).thenReturn(Optional.of(notification));

        Notification result = service.markRead(userId, notificationId);

        assertThat(result.isRead()).isTrue();
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    private static Notification notificationFor(UUID userId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        return notification;
    }
}
