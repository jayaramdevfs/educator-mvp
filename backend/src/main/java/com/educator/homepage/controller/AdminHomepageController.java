package com.educator.homepage.controller;

import com.educator.homepage.dto.HomepageSectionRequest;
import com.educator.homepage.dto.SectionBlockRequest;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;
import com.educator.homepage.service.HomepageAdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/homepage")
public class AdminHomepageController {

    private final HomepageAdminService service;

    public AdminHomepageController(HomepageAdminService service) {
        this.service = service;
    }

    @PostMapping("/sections")
    public HomepageSection createSection(@Valid @RequestBody HomepageSectionRequest req) {
        return service.createSection(req);
    }

    @PostMapping("/blocks")
    public SectionBlock createBlock(@Valid @RequestBody SectionBlockRequest req) {
        return service.createBlock(req);
    }
}
