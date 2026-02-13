package com.educator.notification.controller;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.common.security.UserIdentityUtil;
import com.educator.notification.entity.Notification;
import com.educator.notification.service.LearnerNotificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/learner/notifications")
public class LearnerNotificationController {

    private final LearnerNotificationService learnerNotificationService;

    public LearnerNotificationController(LearnerNotificationService learnerNotificationService) {
        this.learnerNotificationService = learnerNotificationService;
    }

    @GetMapping
    public PaginatedResponse<Notification> listNotifications(
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        UUID userId = UserIdentityUtil.toStableUuid(resolveEmail(authentication));
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PaginatedResponse<>(learnerNotificationService.list(userId, pageable));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markRead(
            Authentication authentication,
            @PathVariable("id") UUID notificationId
    ) {
        UUID userId = UserIdentityUtil.toStableUuid(resolveEmail(authentication));
        return ResponseEntity.ok(learnerNotificationService.markRead(userId, notificationId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(Authentication authentication) {
        UUID userId = UserIdentityUtil.toStableUuid(resolveEmail(authentication));
        return ResponseEntity.ok(Map.of("unreadCount", learnerNotificationService.unreadCount(userId)));
    }

    private String resolveEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalArgumentException("Authenticated user email is required");
        }
        return authentication.getName();
    }
}
