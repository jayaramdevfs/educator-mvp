package com.educator.course.lesson.api;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.service.LessonService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<PaginatedResponse<Lesson>> getLessonsForCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.ASC, "orderIndex"));
        return ResponseEntity.ok(new PaginatedResponse<>(lessonService.getLessonsForCourse(course, pageable)));
    }
}
