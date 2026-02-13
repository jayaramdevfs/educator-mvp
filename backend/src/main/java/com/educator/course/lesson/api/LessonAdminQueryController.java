package com.educator.course.lesson.api;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.service.LessonService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lesson-queries")
//@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
public class LessonAdminQueryController {

    private final LessonService lessonService;
    private final CourseRepository courseRepository;

    public LessonAdminQueryController(
            LessonService lessonService,
            CourseRepository courseRepository
    ) {
        this.lessonService = lessonService;
        this.courseRepository = courseRepository;
    }

    /**
     * Admin / Instructor ordered lesson listing
     * Used for dashboards & management
     */
    @GetMapping("/course/{courseId}")
    public PaginatedResponse<Lesson> getOrderedLessonsForCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.ASC, "orderIndex"));
        return new PaginatedResponse<>(lessonService.getLessonsForCourse(course, pageable));
    }
}
