package com.educator.course.lesson;

import com.educator.course.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Existing behavior (Sprint 3.x compatibility)
     * Root-level lessons only (parent IS NULL)
     */
    List<Lesson> findByCourseAndParentLessonIsNullAndIsDeletedFalseOrderByOrderIndexAsc(
            Course course
    );

    Page<Lesson> findByCourseAndParentLessonIsNullAndIsDeletedFalse(
            Course course,
            Pageable pageable
    );

    /**
     * Child lessons of a given parent lesson
     */
    List<Lesson> findByParentLessonAndIsDeletedFalseOrderByOrderIndexAsc(
            Lesson parentLesson
    );

    /**
     * Flat list of all lessons in a course
     * Ordered deterministically by path, then orderIndex
     * Used for admin, analytics, validation
     */
    @Query("""
        SELECT l FROM Lesson l
        WHERE l.course = :course
          AND l.isDeleted = false
        ORDER BY l.path ASC, l.orderIndex ASC
    """)
    List<Lesson> findAllByCourseFlatOrdered(
            @Param("course") Course course
    );

    /**
     * Fetch full lesson tree in a single course
     * Root lessons only (children loaded lazily but ordered)
     */
    @Query("""
        SELECT l FROM Lesson l
        WHERE l.course = :course
          AND l.parentLesson IS NULL
          AND l.isDeleted = false
        ORDER BY l.orderIndex ASC
    """)
    List<Lesson> findRootLessonsForTree(
            @Param("course") Course course
    );

    /**
     * Check if lesson has children (used for safe delete / reparent)
     */
    boolean existsByParentLessonAndIsDeletedFalse(Lesson parentLesson);
}
