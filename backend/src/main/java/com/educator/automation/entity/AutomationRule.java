package com.educator.automation.entity;

import com.educator.automation.enums.AutomationTriggerType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Defines an automation rule.
 *
 * This entity declares WHEN an automation is triggered
 * and WHAT action should occur.
 *
 * Execution is intentionally deferred.
 */
@Entity
@Table(name = "automation_rules")
public class AutomationRule {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false)
    private AutomationTriggerType triggerType;

    /**
     * Action configuration payload.
     *
     * Example:
     * {
     *   "action": "GENERATE_CERTIFICATE",
     *   "template": "DEFAULT"
     * }
     */
    @Column(name = "action_payload", columnDefinition = "TEXT", nullable = false)
    private String actionPayload;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* -------------------- Lifecycle Hooks -------------------- */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /* -------------------- Getters & Setters -------------------- */

    public UUID getId() {
        return id;
    }

    public AutomationTriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(AutomationTriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public String getActionPayload() {
        return actionPayload;
    }

    public void setActionPayload(String actionPayload) {
        this.actionPayload = actionPayload;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
