package com.educator.course.api;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.CourseStatus;
import com.educator.course.service.CourseService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/courses")
public class CoursePublicController {

    private final CourseService courseService;

    public CoursePublicController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/search")
    public PaginatedResponse<Course> searchCourses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) CourseDifficulty difficulty,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PaginatedResponse<>(
                courseService.searchPublicCourses(q, difficulty, status, pageable)
        );
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getPublicCourseById(courseId));
    }
}
