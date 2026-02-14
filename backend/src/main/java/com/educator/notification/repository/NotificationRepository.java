package com.educator.notification.repository;

import com.educator.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    long countByUserIdAndReadFalse(UUID userId);

    // 🔒 B3.5 — Secure lookup to prevent cross-user access
    Optional<Notification> findByIdAndUserId(UUID id, UUID userId);
}
