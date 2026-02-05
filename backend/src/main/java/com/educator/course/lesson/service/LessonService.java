package com.educator.course.lesson.service;

import com.educator.course.Course;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonRepository;
import com.educator.course.lesson.LessonType;
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

    /* -------------------------------------------------
       CREATE LESSON (ROOT)
     ------------------------------------------------- */
    /**
     * Backward-compatible method for Sprint 3.x controllers
     * Creates ROOT-level lesson
     */
    public Lesson addLesson(
            Course course,
            LessonType type,
            int orderIndex,
            String textContent,
            String videoUrl,
            String documentUrl
    ) {
        return addRootLesson(
                course,
                type,
                orderIndex,
                textContent,
                videoUrl,
                documentUrl
        );
    }

    public Lesson addRootLesson(
            Course course,
            LessonType type,
            int orderIndex,
            String textContent,
            String videoUrl,
            String documentUrl
    ) {
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setParentLesson(null);
        lesson.setDepthLevel(0);
        lesson.setOrderIndex(orderIndex);
        lesson.setType(type);
        lesson.setTextContent(textContent);
        lesson.setVideoUrl(videoUrl);
        lesson.setDocumentUrl(documentUrl);
        lesson.setDeleted(false);

        // Temporary path, finalized after save
        lesson.setPath("/course/" + course.getId());

        lesson = lessonRepository.save(lesson);

        // Final path includes lesson ID
        lesson.setPath(lesson.getPath() + "/lesson/" + lesson.getId());
        return lessonRepository.save(lesson);
    }

    /* -------------------------------------------------
       CREATE LESSON (CHILD)
     ------------------------------------------------- */

    public Lesson addChildLesson(
            Lesson parentLesson,
            LessonType type,
            int orderIndex,
            String textContent,
            String videoUrl,
            String documentUrl
    ) {
        if (parentLesson.isDeleted()) {
            throw new IllegalStateException("Cannot add child to deleted lesson");
        }

        Lesson lesson = new Lesson();
        lesson.setCourse(parentLesson.getCourse());
        lesson.setParentLesson(parentLesson);
        lesson.setDepthLevel(parentLesson.getDepthLevel() + 1);
        lesson.setOrderIndex(orderIndex);
        lesson.setType(type);
        lesson.setTextContent(textContent);
        lesson.setVideoUrl(videoUrl);
        lesson.setDocumentUrl(documentUrl);
        lesson.setDeleted(false);

        lesson.setPath(parentLesson.getPath());

        lesson = lessonRepository.save(lesson);

        lesson.setPath(parentLesson.getPath() + "/lesson/" + lesson.getId());
        return lessonRepository.save(lesson);
    }

    /* -------------------------------------------------
       READ OPERATIONS
     ------------------------------------------------- */

    @Transactional(readOnly = true)
    public List<Lesson> getRootLessons(Course course) {
        return lessonRepository
                .findByCourseAndParentLessonIsNullAndIsDeletedFalseOrderByOrderIndexAsc(course);
    }

    @Transactional(readOnly = true)
    public List<Lesson> getChildLessons(Lesson parentLesson) {
        return lessonRepository
                .findByParentLessonAndIsDeletedFalseOrderByOrderIndexAsc(parentLesson);
    }

    @Transactional(readOnly = true)
    public List<Lesson> getAllLessonsFlat(Course course) {
        return lessonRepository.findAllByCourseFlatOrdered(course);
    }
    /**
     * Backward-compatible method for Sprint 3.x controllers
     * Returns ROOT lessons only
     */
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsForCourse(Course course) {
        return lessonRepository
                .findByCourseAndParentLessonIsNullAndIsDeletedFalseOrderByOrderIndexAsc(course);
    }

    /* -------------------------------------------------
       SAFE REPARENTING
     ------------------------------------------------- */

    public Lesson moveLesson(Lesson lesson, Lesson newParent) {
        if (lesson.getId().equals(newParent.getId())) {
            throw new IllegalArgumentException("Lesson cannot be parent of itself");
        }

        // Prevent circular hierarchy
        if (newParent.getPath().startsWith(lesson.getPath())) {
            throw new IllegalStateException("Circular hierarchy detected");
        }

        lesson.setParentLesson(newParent);
        lesson.setDepthLevel(newParent.getDepthLevel() + 1);
        lesson.setPath(newParent.getPath() + "/lesson/" + lesson.getId());

        return lessonRepository.save(lesson);
    }

    /* -------------------------------------------------
       SAFE DELETE (SOFT CASCADE)
     ------------------------------------------------- */

    public void deleteLessonCascade(Lesson lesson) {
        lesson.setDeleted(true);
        lessonRepository.save(lesson);

        List<Lesson> children =
                lessonRepository.findByParentLessonAndIsDeletedFalseOrderByOrderIndexAsc(lesson);

        for (Lesson child : children) {
            deleteLessonCascade(child);
        }
    }
    /**
     * Backward-compatible delete method for Sprint 3.x controllers
     * Delegates to soft cascade delete
     */
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        deleteLessonCascade(lesson);
    }

}
