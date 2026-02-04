package com.educator.course.api;

import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.service.CourseService;
import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/courses")
public class CourseController {

    private final CourseService courseService;
    private final HierarchyNodeRepository hierarchyNodeRepository;

    public CourseController(
            CourseService courseService,
            HierarchyNodeRepository hierarchyNodeRepository
    ) {
        this.courseService = courseService;
        this.hierarchyNodeRepository = hierarchyNodeRepository;
    }

    @PostMapping
    public Course createCourse(
            @RequestParam Long hierarchyNodeId,
            @RequestParam String titleEn,
            @RequestParam String descriptionEn,
            @RequestParam CourseDifficulty difficulty,
            @RequestParam String languageCode,
            @RequestParam int estimatedDurationMinutes,
            @RequestParam String createdByRole
    ) {
        // ✅ CORRECT WAY: fetch existing node from DB
        HierarchyNode node = hierarchyNodeRepository
                .findById(hierarchyNodeId)
                .orElseThrow(() -> new RuntimeException("Hierarchy node not found"));

        return courseService.createCourse(
                node,
                titleEn,
                descriptionEn,
                difficulty,
                languageCode,
                estimatedDurationMinutes,
                createdByRole
        );
    }

    @PostMapping("/{id}/publish")
    public void publish(@PathVariable Long id) {
        courseService.publishCourse(id);
    }

    @PostMapping("/{id}/archive")
    public void archive(@PathVariable Long id) {
        courseService.archiveCourse(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @GetMapping("/active")
    public List<Course> getAllActiveCourses() {
        return courseService.getAllActiveCourses();
    }
}
