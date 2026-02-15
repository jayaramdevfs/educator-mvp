package com.educator;

import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.dto.UpdateCourseRequest;
import com.educator.course.service.CourseService;
import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        HierarchyNode node = hierarchyNodeRepository.findById(hierarchyNodeId)
                .orElseThrow(() -> new IllegalArgumentException("Hierarchy node not found"));

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

    // ✅ B4.1 UPDATE ENDPOINT
    @PutMapping("/{id}")
    public Course updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request
    ) {
        HierarchyNode node = hierarchyNodeRepository.findById(request.getHierarchyNodeId())
                .orElseThrow(() -> new IllegalArgumentException("Hierarchy node not found"));

        return courseService.updateCourse(
                id,
                node,
                request.getTitleEn(),
                request.getDescriptionEn(),
                request.getDifficulty(),
                request.getLanguageCode(),
                request.getEstimatedDurationMinutes()
        );
    }
}
