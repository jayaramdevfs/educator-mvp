package com.educator.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.educator.hierarchy.HierarchyNode;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Existing non-paginated method (kept for backward compatibility)
    List<Course> findByIsDeletedFalse();

    // NEW — Paginated version
    Page<Course> findByIsDeletedFalse(Pageable pageable);

    Optional<Course> findByIdAndIsDeletedFalse(Long id);

    List<Course> findByStatusAndIsDeletedFalse(CourseStatus status);

    List<Course> findByHierarchyNodeAndStatusAndIsArchivedFalseAndIsDeletedFalseOrderBySortOrderAsc(
            HierarchyNode hierarchyNode,
            CourseStatus status
    );

    List<Course> findByIsDeletedFalseOrderByCreatedAtDesc();

    long countByIsDeletedFalse();

    @Query("""
            select c from Course c
            where c.isDeleted = false
              and c.isArchived = false
              and (:q is null or lower(c.titleEn) like lower(concat('%', :q, '%'))
                or lower(c.descriptionEn) like lower(concat('%', :q, '%')))
              and (:difficulty is null or c.difficulty = :difficulty)
              and (:status is null or c.status = :status)
            """)
    Page<Course> searchPublicCourses(
            @Param("q") String q,
            @Param("difficulty") CourseDifficulty difficulty,
            @Param("status") CourseStatus status,
            Pageable pageable
    );
}
