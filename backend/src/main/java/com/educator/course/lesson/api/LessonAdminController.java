package com.educator.course.lesson.api;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonType;
import com.educator.course.lesson.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lessons")
//@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
public class LessonAdminController {

    private final LessonService lessonService;
    private final CourseRepository courseRepository;

    public LessonAdminController(
            LessonService lessonService,
            CourseRepository courseRepository
    ) {
        this.lessonService = lessonService;
        this.courseRepository = courseRepository;
    }

    /**
     * Add lesson to a course
     */
    @PostMapping("/course/{courseId}")
    public ResponseEntity<Lesson> addLessonToCourse(
            @PathVariable Long courseId,
            @RequestParam LessonType type,
            @RequestParam int orderIndex,
            @RequestParam(required = false) String textContent,
            @RequestParam(required = false) String videoUrl,
            @RequestParam(required = false) String documentUrl
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Lesson lesson = lessonService.addLesson(
                course,
                type,
                orderIndex,
                textContent,
                videoUrl,
                documentUrl
        );

        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    /**
     * Soft delete lesson
     */
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Long lessonId
    ) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
}
