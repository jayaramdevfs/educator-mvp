package com.educator.hierarchy.api;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<PaginatedResponse<HierarchyNode>> roots(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        return ResponseEntity.ok(new PaginatedResponse<>(service.getRootNodes(pageable)));
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<PaginatedResponse<HierarchyNode>> children(
            @PathVariable Long parentId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        return ResponseEntity.ok(new PaginatedResponse<>(service.getChildren(parentId, pageable)));
    }
}
