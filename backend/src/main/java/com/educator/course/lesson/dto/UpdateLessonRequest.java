package com.educator.course.lesson.dto;

import com.educator.course.lesson.LessonType;
import jakarta.validation.constraints.NotNull;

public class UpdateLessonRequest {

    @NotNull
    private LessonType type;

    private String textContent;

    private String videoUrl;

    private String documentUrl;

    private Integer orderIndex;

    private Long parentLessonId;

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getParentLessonId() {
        return parentLessonId;
    }

    public void setParentLessonId(Long parentLessonId) {
        this.parentLessonId = parentLessonId;
    }
}
