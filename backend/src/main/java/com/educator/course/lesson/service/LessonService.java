package com.educator.course.lesson.service;

import com.educator.course.Course;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonRepository;
import com.educator.course.lesson.LessonType;
import com.educator.course.lesson.dto.UpdateLessonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    // -------------------------------------------------
    // QUERY ROOT LESSONS FOR COURSE
    // -------------------------------------------------
    @Transactional(readOnly = true)
    public Page<Lesson> getLessonsForCourse(Course course, Pageable pageable) {
        return lessonRepository
                .findByCourseAndParentLessonIsNullAndIsDeletedFalse(course, pageable);
    }

    // -------------------------------------------------
    // QUERY CHILD LESSONS (For tree)
    // -------------------------------------------------
    @Transactional(readOnly = true)
    public List<Lesson> getChildLessons(Lesson parentLesson) {
        return lessonRepository
                .findByParentLessonAndIsDeletedFalseOrderByOrderIndexAsc(parentLesson);
    }

    // -------------------------------------------------
    // ADD LESSON
    // -------------------------------------------------
    public Lesson addLesson(
            Course course,
            LessonType type,
            int orderIndex,
            String textContent,
            String videoUrl,
            String documentUrl
    ) {

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setType(type);
        lesson.setOrderIndex(orderIndex);
        lesson.setDepthLevel(0);
        lesson.setPath("/course/" + course.getId() + "/lesson/temp");

        if (type == LessonType.TEXT) {
            lesson.setTextContent(textContent);
        } else if (type == LessonType.VIDEO) {
            lesson.setVideoUrl(videoUrl);
        } else if (type == LessonType.DOCUMENT) {
            lesson.setDocumentUrl(documentUrl);
        }

        Lesson saved = lessonRepository.save(lesson);

        saved.setPath("/course/" + course.getId() + "/lesson/" + saved.getId());

        return lessonRepository.save(saved);
    }

    // -------------------------------------------------
    // DELETE LESSON (Soft delete)
    // -------------------------------------------------
    public void deleteLesson(Long lessonId) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        if (lessonRepository.existsByParentLessonAndIsDeletedFalse(lesson)) {
            throw new IllegalStateException("Cannot delete lesson with children");
        }

        lesson.setDeleted(true);
        lessonRepository.save(lesson);
    }

    // -------------------------------------------------
    // B4.2 UPDATE LESSON
    // -------------------------------------------------
    public Lesson updateLesson(Long lessonId, UpdateLessonRequest request) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        if (lesson.isDeleted()) {
            throw new IllegalStateException("Cannot update deleted lesson");
        }

        lesson.setType(request.getType());

        lesson.setTextContent(null);
        lesson.setVideoUrl(null);
        lesson.setDocumentUrl(null);

        if (request.getType() == LessonType.TEXT) {
            lesson.setTextContent(request.getTextContent());
        } else if (request.getType() == LessonType.VIDEO) {
            lesson.setVideoUrl(request.getVideoUrl());
        } else if (request.getType() == LessonType.DOCUMENT) {
            lesson.setDocumentUrl(request.getDocumentUrl());
        }

        if (request.getOrderIndex() != null) {
            lesson.setOrderIndex(request.getOrderIndex());
        }

        if (request.getParentLessonId() != null) {
            Lesson newParent = lessonRepository.findById(request.getParentLessonId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent lesson not found"));

            lesson.setParentLesson(newParent);
            lesson.setDepthLevel(newParent.getDepthLevel() + 1);
            lesson.setPath(newParent.getPath() + "/lesson/" + lesson.getId());
        }

        return lessonRepository.save(lesson);
    }

    // -------------------------------------------------
    // B4.3 REORDER LESSON
    // -------------------------------------------------
    public Lesson reorderLesson(Long lessonId, int newOrderIndex) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        if (lesson.isDeleted()) {
            throw new IllegalStateException("Cannot reorder deleted lesson");
        }

        Lesson parent = lesson.getParentLesson();

        List<Lesson> siblings;

        if (parent == null) {
            siblings = lessonRepository
                    .findByCourseAndParentLessonIsNullAndIsDeletedFalseOrderByOrderIndexAsc(
                            lesson.getCourse()
                    );
        } else {
            siblings = lessonRepository
                    .findByParentLessonAndIsDeletedFalseOrderByOrderIndexAsc(parent);
        }

        int currentIndex = lesson.getOrderIndex();

        if (newOrderIndex == currentIndex) {
            return lesson;
        }

        for (Lesson sibling : siblings) {

            if (sibling.getId().equals(lesson.getId())) {
                continue;
            }

            int siblingIndex = sibling.getOrderIndex();

            if (newOrderIndex > currentIndex) {
                if (siblingIndex > currentIndex && siblingIndex <= newOrderIndex) {
                    sibling.setOrderIndex(siblingIndex - 1);
                    lessonRepository.save(sibling);
                }
            } else {
                if (siblingIndex >= newOrderIndex && siblingIndex < currentIndex) {
                    sibling.setOrderIndex(siblingIndex + 1);
                    lessonRepository.save(sibling);
                }
            }
        }

        lesson.setOrderIndex(newOrderIndex);

        return lessonRepository.save(lesson);
    }
}
