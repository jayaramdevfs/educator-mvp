package com.educator.course.service;

import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.CourseRepository;
import com.educator.course.CourseStatus;
import com.educator.hierarchy.HierarchyNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // -------------------------------------------------
    // CREATE COURSE
    // -------------------------------------------------
    public Course createCourse(
            HierarchyNode hierarchyNode,
            String titleEn,
            String descriptionEn,
            CourseDifficulty difficulty,
            String languageCode,
            int estimatedDurationMinutes,
            String createdByRole
    ) {
        Course course = new Course();
        course.setHierarchyNode(hierarchyNode);
        course.setTitleEn(titleEn);
        course.setDescriptionEn(descriptionEn);
        course.setDifficulty(difficulty);
        course.setLanguageCode(languageCode);
        course.setEstimatedDurationMinutes(estimatedDurationMinutes);
        course.setCreatedByRole(createdByRole);
        course.setStatus(CourseStatus.DRAFT);

        return courseRepository.save(course);
    }

    // -------------------------------------------------
    // PUBLIC SEARCH (ALIGNED TO REPOSITORY)
    // -------------------------------------------------
    @Transactional(readOnly = true)
    public Page<Course> searchPublicCourses(
            String q,
            CourseDifficulty difficulty,
            CourseStatus status,
            Pageable pageable
    ) {
        // Normalize q (important to avoid JPQL bytea issue)
        String normalizedQ = (q == null || q.isBlank()) ? null : q;

        if (normalizedQ != null) {
            return courseRepository.searchPublicCoursesWithQuery(
                    normalizedQ,
                    difficulty,
                    status,
                    pageable
            );
        }

        return courseRepository.searchPublicCoursesWithoutQuery(
                difficulty,
                status,
                pageable
        );
    }

    // -------------------------------------------------
    // B4.1 UPDATE COURSE
    // -------------------------------------------------
    public Course updateCourse(
            Long courseId,
            HierarchyNode hierarchyNode,
            String titleEn,
            String descriptionEn,
            CourseDifficulty difficulty,
            String languageCode,
            int estimatedDurationMinutes
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.isDeleted()) {
            throw new IllegalStateException("Cannot update deleted course");
        }

        course.setHierarchyNode(hierarchyNode);
        course.setTitleEn(titleEn);
        course.setDescriptionEn(descriptionEn);
        course.setDifficulty(difficulty);
        course.setLanguageCode(languageCode);
        course.setEstimatedDurationMinutes(estimatedDurationMinutes);

        return courseRepository.save(course);
    }
}
