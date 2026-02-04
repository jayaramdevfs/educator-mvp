package com.educator.hierarchy.api;

import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/hierarchy")
public class HierarchyAdminController {

    private final HierarchyNodeService service;

    public HierarchyAdminController(HierarchyNodeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HierarchyNode> create(
            @RequestParam String slug,
            @RequestParam String nameEn,
            @RequestParam(required = false) String descriptionEn,
            @RequestParam(required = false) Long parentId,
            @RequestParam(defaultValue = "0") int sortOrder,
            @RequestParam(required = false) String createdBy
    ) {
        return ResponseEntity.ok(
                service.createNode(slug, nameEn, descriptionEn, parentId, sortOrder, createdBy)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<HierarchyNode> update(
            @PathVariable Long id,
            @RequestParam String nameEn,
            @RequestParam(required = false) String descriptionEn,
            @RequestParam(defaultValue = "0") int sortOrder,
            @RequestParam boolean isPublished,
            @RequestParam boolean isVisible
    ) {
        return ResponseEntity.ok(
                service.updateNode(id, nameEn, descriptionEn, sortOrder, isPublished, isVisible)
        );
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<HierarchyNode> move(
            @PathVariable Long id,
            @RequestParam Long newParentId
    ) {
        return ResponseEntity.ok(service.moveNode(id, newParentId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }
}
