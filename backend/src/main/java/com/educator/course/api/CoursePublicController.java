package com.educator.course.api;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.CourseStatus;
import com.educator.course.service.CourseService;
import com.educator.course.CourseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/public/courses")
public class CoursePublicController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    public CoursePublicController(CourseService courseService,
                                  CourseRepository courseRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/search")
    public PaginatedResponse<Course> searchCourses(
            Authentication authentication,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) CourseDifficulty difficulty,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        // Public + Students -> force PUBLISHED
        // Admin/Instructor can filter by status
        boolean isPrivileged = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN") ||
                                a.getAuthority().equals("ROLE_INSTRUCTOR")
                );

        if (!isPrivileged) {
            status = CourseStatus.PUBLISHED;
        }

        Pageable pageable = PageableFactory.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return new PaginatedResponse<>(
                courseService.searchPublicCourses(q, difficulty, status, pageable)
        );
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {

        return courseRepository
                .findByIdAndStatusAndIsDeletedFalse(courseId, CourseStatus.PUBLISHED)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new org.springframework.web.server.ResponseStatusException(
                                org.springframework.http.HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );
    }
}
