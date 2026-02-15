package com.educator.course.lesson.api;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonType;
import com.educator.course.lesson.dto.UpdateLessonRequest;
import com.educator.course.lesson.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.educator.course.lesson.dto.ReorderLessonRequest;

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
     * B4.2 - Update Lesson
     */
    @PutMapping("/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody UpdateLessonRequest request
    ) {
        Lesson updatedLesson = lessonService.updateLesson(lessonId, request);
        return ResponseEntity.ok(updatedLesson);
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
    /**
     * B4.3 - Reorder Lesson
     */
    @PutMapping("/{lessonId}/reorder")
    public ResponseEntity<Lesson> reorderLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody ReorderLessonRequest request
    ) {
        Lesson reordered = lessonService.reorderLesson(
                lessonId,
                request.getNewOrderIndex()
        );
        return ResponseEntity.ok(reordered);
    }

}
