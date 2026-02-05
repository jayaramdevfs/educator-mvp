package com.educator.course.lesson.api;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.service.LessonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/lesson-queries")
@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
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
    public List<Lesson> getOrderedLessonsForCourse(
            @PathVariable Long courseId
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return lessonService.getLessonsForCourse(course);
    }
}
