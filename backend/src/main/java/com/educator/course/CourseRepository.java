package com.educator.course;

import com.educator.hierarchy.HierarchyNode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Fetch all non-deleted courses under a hierarchy node
     */
    List<Course> findByHierarchyNodeAndIsDeletedFalseOrderBySortOrderAsc(
            HierarchyNode hierarchyNode
    );

    /**
     * Public listing: only published & non-archived courses
     */
    List<Course> findByHierarchyNodeAndStatusAndIsArchivedFalseAndIsDeletedFalseOrderBySortOrderAsc(
            HierarchyNode hierarchyNode,
            CourseStatus status
    );

    /**
     * Admin / Instructor listing
     */
    List<Course> findByIsDeletedFalseOrderByCreatedAtDesc();
}
