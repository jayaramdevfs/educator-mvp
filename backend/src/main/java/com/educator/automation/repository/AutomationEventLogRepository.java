package com.educator.automation.repository;

import com.educator.automation.entity.AutomationEventLog;
import com.educator.automation.enums.AutomationTriggerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for AutomationEventLog entity.
 */
public interface AutomationEventLogRepository extends JpaRepository<AutomationEventLog, UUID> {

    /**
     * Fetch events by trigger type.
     */
    List<AutomationEventLog> findByTriggerType(AutomationTriggerType triggerType);
}
