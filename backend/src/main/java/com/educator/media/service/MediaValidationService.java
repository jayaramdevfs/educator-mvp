package com.educator.media.service;

import com.educator.lesson.enums.LessonType;
import com.educator.media.entity.MediaReference;

/**
 * Validation-only service.
 * NO uploads, NO streaming.
 */
public class MediaValidationService {

    public boolean isValid(LessonType lessonType, MediaReference reference) {

        if (lessonType == LessonType.TEXT) {
            return true;
        }

        if (lessonType == LessonType.VIDEO || lessonType == LessonType.PDF) {
            return reference != null && reference.getSourceUrl() != null;
        }

        return true;
    }
}
