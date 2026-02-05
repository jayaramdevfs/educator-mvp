package com.educator.course.lesson.api;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/lessons")
public class LessonPublicController {

    private final LessonService lessonService;
    private final CourseRepository courseRepository;

    public LessonPublicController(
            LessonService lessonService,
            CourseRepository courseRepository
    ) {
        this.lessonService = lessonService;
        this.courseRepository = courseRepository;
    }

    /**
     * Get ordered lessons for a course (student view)
     * - Only non-deleted lessons
     * - Always ordered by orderIndex ASC
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Lesson>> getLessonsForCourse(
            @PathVariable Long courseId
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        List<Lesson> lessons = lessonService.getLessonsForCourse(course);
        return ResponseEntity.ok(lessons);
    }
}
