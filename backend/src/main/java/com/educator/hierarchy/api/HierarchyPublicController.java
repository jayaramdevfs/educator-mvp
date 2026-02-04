package com.educator.hierarchy.api;

import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hierarchy")
public class HierarchyPublicController {

    private final HierarchyNodeService service;

    public HierarchyPublicController(HierarchyNodeService service) {
        this.service = service;
    }

    @GetMapping("/roots")
    public ResponseEntity<List<HierarchyNode>> roots() {
        return ResponseEntity.ok(service.getRootNodes());
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<HierarchyNode>> children(@PathVariable Long parentId) {
        return ResponseEntity.ok(service.getChildren(parentId));
    }
}
