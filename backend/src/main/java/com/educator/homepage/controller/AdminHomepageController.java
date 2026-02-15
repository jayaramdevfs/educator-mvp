package com.educator.homepage.controller;

import com.educator.homepage.dto.HomepageSectionRequest;
import com.educator.homepage.dto.SectionBlockRequest;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;
import com.educator.homepage.service.HomepageAdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/homepage")
public class AdminHomepageController {

    private final HomepageAdminService service;

    public AdminHomepageController(HomepageAdminService service) {
        this.service = service;
    }

    // SECTION

    @PostMapping("/sections")
    public HomepageSection createSection(@Valid @RequestBody HomepageSectionRequest req) {
        return service.createSection(req);
    }

    @PutMapping("/sections/{sectionId}")
    public HomepageSection updateSection(
            @PathVariable UUID sectionId,
            @Valid @RequestBody HomepageSectionRequest req
    ) {
        return service.updateSection(sectionId, req);
    }

    @DeleteMapping("/sections/{sectionId}")
    public void deleteSection(@PathVariable UUID sectionId) {
        service.deleteSection(sectionId);
    }

    // BLOCK

    @PostMapping("/blocks")
    public SectionBlock createBlock(@Valid @RequestBody SectionBlockRequest req) {
        return service.createBlock(req);
    }

    @PutMapping("/blocks/{blockId}")
    public SectionBlock updateBlock(
            @PathVariable UUID blockId,
            @Valid @RequestBody SectionBlockRequest req
    ) {
        return service.updateBlock(blockId, req);
    }

    @DeleteMapping("/blocks/{blockId}")
    public void deleteBlock(@PathVariable UUID blockId) {
        service.deleteBlock(blockId);
    }

    @PutMapping("/blocks/{blockId}/reorder")
    public SectionBlock reorderBlock(
            @PathVariable UUID blockId,
            @RequestParam int newOrderIndex
    ) {
        return service.reorderBlock(blockId, newOrderIndex);
    }
}
