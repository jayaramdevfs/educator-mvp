package com.educator.notification.service;

import com.educator.notification.entity.NotificationType;
import com.educator.notification.enums.NotificationTrigger;

public class NotificationTriggerMapper {

    public static NotificationType map(NotificationTrigger trigger) {
        return NotificationType.valueOf(trigger.name());
    }
}
