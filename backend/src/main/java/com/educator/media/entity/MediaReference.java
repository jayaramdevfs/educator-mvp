package com.educator.media.entity;

import com.educator.media.enums.MediaSourceType;
import com.educator.media.enums.MediaType;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Metadata-only reference to media.
 * DOES NOT store files.
 */
@Entity
@Table(name = "media_references")
public class MediaReference {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID lessonId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Enumerated(EnumType.STRING)
    private MediaSourceType sourceType;

    private String sourceUrl;

    private String title;

    private Integer durationSeconds;

    private boolean active = true;

    /* Getters & Setters */

    public UUID getId() {
        return id;
    }

    public UUID getLessonId() {
        return lessonId;
    }

    public void setLessonId(UUID lessonId) {
        this.lessonId = lessonId;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(MediaSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
