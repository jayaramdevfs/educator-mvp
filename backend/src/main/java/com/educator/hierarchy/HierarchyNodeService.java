package com.educator.hierarchy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HierarchyNodeService {

    private static final int MAX_DEPTH = 10; // configurable later

    private final HierarchyNodeRepository repository;

    public HierarchyNodeService(HierarchyNodeRepository repository) {
        this.repository = repository;
    }

    /* =====================
       CREATE
       ===================== */

    public HierarchyNode createNode(
            String slug,
            String nameEn,
            String descriptionEn,
            Long parentId,
            int sortOrder,
            String createdBy
    ) {
        if (repository.existsBySlug(slug)) {
            throw new IllegalArgumentException("Slug already exists");
        }

        HierarchyNode node = new HierarchyNode();
        node.setSlug(slug);
        node.setNameEn(nameEn);
        node.setDescriptionEn(descriptionEn);
        node.setSortOrder(sortOrder);
        node.setCreatedBy(createdBy);

        if (parentId != null) {
            HierarchyNode parent = getNodeOrThrow(parentId);
            validateDepth(parent);
            node.setParent(parent);
        }

        return repository.save(node);
    }

    /* =====================
       UPDATE
       ===================== */

    public HierarchyNode updateNode(
            Long nodeId,
            String nameEn,
            String descriptionEn,
            int sortOrder,
            boolean isPublished,
            boolean isVisible
    ) {
        HierarchyNode node = getNodeOrThrow(nodeId);

        node.setNameEn(nameEn);
        node.setDescriptionEn(descriptionEn);
        node.setSortOrder(sortOrder);
        node.setPublished(isPublished);
        node.setVisible(isVisible);

        return repository.save(node);
    }

    /* =====================
       MOVE (RE-PARENT)
       ===================== */

    public HierarchyNode moveNode(Long nodeId, Long newParentId) {
        HierarchyNode node = getNodeOrThrow(nodeId);
        HierarchyNode newParent = getNodeOrThrow(newParentId);

        if (node.getId().equals(newParent.getId())) {
            throw new IllegalStateException("Node cannot be parent of itself");
        }

        if (isDescendant(node, newParent)) {
            throw new IllegalStateException("Cycle detected in hierarchy");
        }

        validateDepth(newParent);

        node.setParent(newParent);
        return repository.save(node);
    }

    /* =====================
       DELETE / RESTORE
       ===================== */

    public void softDelete(Long nodeId) {
        HierarchyNode node = getNodeOrThrow(nodeId);
        node.setDeleted(true);
        repository.save(node);
    }

    public void restore(Long nodeId) {
        HierarchyNode node = getNodeOrThrow(nodeId);
        node.setDeleted(false);
        repository.save(node);
    }

    /* =====================
       READ
       ===================== */

    @Transactional(readOnly = true)
    public List<HierarchyNode> getRootNodes() {
        return repository.findByParentIsNullAndIsDeletedFalseOrderBySortOrderAsc();
    }

    @Transactional(readOnly = true)
    public List<HierarchyNode> getChildren(Long parentId) {
        return repository.findByParentIdAndIsDeletedFalseOrderBySortOrderAsc(parentId);
    }

    /* =====================
       INTERNAL HELPERS
       ===================== */

    private HierarchyNode getNodeOrThrow(Long id) {
        return repository.findById(id)
                .filter(n -> !n.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Hierarchy node not found"));
    }

    private void validateDepth(HierarchyNode parent) {
        int depth = 1;
        HierarchyNode current = parent;

        while (current.getParent() != null) {
            depth++;
            if (depth >= MAX_DEPTH) {
                throw new IllegalStateException("Maximum hierarchy depth exceeded");
            }
            current = current.getParent();
        }
    }

    private boolean isDescendant(HierarchyNode node, HierarchyNode potentialParent) {
        HierarchyNode current = potentialParent;

        while (current != null) {
            if (current.getId().equals(node.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
