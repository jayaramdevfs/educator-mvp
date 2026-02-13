package com.educator.course.service;

import com.educator.course.*;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.service.LessonService;
import com.educator.course.CourseRepository;
import com.educator.hierarchy.HierarchyNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final LessonService lessonService;

    public CourseService(CourseRepository courseRepository, LessonService lessonService) {
        this.courseRepository = courseRepository;
        this.lessonService = lessonService;
    }

    /**
     * Create a new course (DRAFT only)
     */
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

    /**
     * Publish a course
     */
    public Course publishCourse(Long courseId) {
        Course course = getCourseOrThrow(courseId);

        if (course.isArchived() || course.isDeleted()) {
            throw new IllegalStateException("Cannot publish archived or deleted course");
        }

        course.setStatus(CourseStatus.PUBLISHED);
        return courseRepository.save(course);
    }

    /**
     * Archive a course
     */
    public Course archiveCourse(Long courseId) {
        Course course = getCourseOrThrow(courseId);
        course.setArchived(true);
        return courseRepository.save(course);
    }

    /**
     * Soft delete
     */
    public void deleteCourse(Long courseId) {
        Course course = getCourseOrThrow(courseId);
        course.setDeleted(true);
        courseRepository.save(course);
    }

    /**
     * Public listing
     */
    @Transactional(readOnly = true)
    public List<Course> getPublishedCoursesByHierarchy(
            HierarchyNode hierarchyNode
    ) {
        return courseRepository.findByHierarchyNodeAndStatusAndIsArchivedFalseAndIsDeletedFalseOrderBySortOrderAsc(
                hierarchyNode,
                CourseStatus.PUBLISHED
        );
    }

    /**
     * Admin / Instructor listing
     */
    @Transactional(readOnly = true)
    public List<Course> getAllActiveCourses() {
        return courseRepository.findByIsDeletedFalseOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Page<Course> getAllActiveCourses(Pageable pageable) {
        return courseRepository.findByIsDeletedFalse(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Course> searchPublicCourses(
            String q,
            CourseDifficulty difficulty,
            CourseStatus status,
            Pageable pageable
    ) {
        return courseRepository.searchPublicCourses(q, difficulty, status, pageable);
    }

    @Transactional(readOnly = true)
    public Course getPublicCourseById(Long courseId) {
        Course course = courseRepository.findByIdAndIsDeletedFalse(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (course.isArchived()) {
            throw new IllegalArgumentException("Course is archived");
        }

        return course;
    }

    private Course getCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }
}
