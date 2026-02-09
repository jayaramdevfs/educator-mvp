package com.educator.automation.entity;

import com.educator.automation.enums.AutomationTriggerType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Records an automation-triggering event.
 *
 * This entity is append-only by design and
 * serves as the foundation for future async processing.
 */
@Entity
@Table(name = "automation_event_logs")
public class AutomationEventLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger_type", nullable = false)
    private AutomationTriggerType triggerType;

    /**
     * Reference to the domain entity that triggered the event.
     * Example: courseCompletionId, certificateId, etc.
     */
    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    /**
     * Event context payload (JSON).
     */
    @Column(name = "event_payload", columnDefinition = "TEXT")
    private String eventPayload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /* -------------------- Lifecycle Hooks -------------------- */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
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

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }

    public String getEventPayload() {
        return eventPayload;
    }

    public void setEventPayload(String eventPayload) {
        this.eventPayload = eventPayload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
