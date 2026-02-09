package com.educator.automation.repository;

import com.educator.automation.entity.AutomationRule;
import com.educator.automation.enums.AutomationTriggerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for AutomationRule entity.
 */
public interface AutomationRuleRepository extends JpaRepository<AutomationRule, UUID> {

    /**
     * Fetch all enabled automation rules for a given trigger.
     */
    List<AutomationRule> findByTriggerTypeAndEnabledTrue(AutomationTriggerType triggerType);
}
