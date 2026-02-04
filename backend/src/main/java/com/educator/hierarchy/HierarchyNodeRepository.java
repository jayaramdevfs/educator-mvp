package com.educator.hierarchy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HierarchyNodeRepository extends JpaRepository<HierarchyNode, Long> {

    /**
     * Fetch all root nodes (parent = null), ordered
     */
    List<HierarchyNode> findByParentIsNullAndIsDeletedFalseOrderBySortOrderAsc();

    /**
     * Fetch children of a given parent, ordered
     */
    List<HierarchyNode> findByParentIdAndIsDeletedFalseOrderBySortOrderAsc(Long parentId);

    /**
     * Find node by slug (used for routing / uniqueness)
     */
    Optional<HierarchyNode> findBySlugAndIsDeletedFalse(String slug);

    /**
     * Check slug uniqueness (create/update validation)
     */
    boolean existsBySlug(String slug);

    /**
     * Fetch all visible & published nodes (for public tree later)
     */
    @Query("""
        select n from HierarchyNode n
        where n.isDeleted = false
          and n.isVisible = true
          and n.isPublished = true
        order by n.sortOrder asc
    """)
    List<HierarchyNode> findAllVisiblePublished();
}
