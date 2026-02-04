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

    /**
     * Add lesson to a course
     */
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
        lesson.setTextContent(textContent);
        lesson.setVideoUrl(videoUrl);
        lesson.setDocumentUrl(documentUrl);
        lesson.setDeleted(false);

        return lessonRepository.save(lesson);
    }

    /**
     * Fetch lessons for a course
     */
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsForCourse(Course course) {
        return lessonRepository.findByCourseAndIsDeletedFalseOrderByOrderIndexAsc(course);
    }

    /**
     * Soft delete lesson
     */
    public void deleteLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        lesson.setDeleted(true);
        lessonRepository.save(lesson);
    }
}
