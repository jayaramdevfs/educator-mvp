package com.educator.homepage.controller;

import com.educator.homepage.dto.HomepageSectionRequest;
import com.educator.homepage.dto.SectionBlockRequest;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;
import com.educator.homepage.service.HomepageAdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/homepage")
public class AdminHomepageController {

    private final HomepageAdminService service;

    public AdminHomepageController(HomepageAdminService service) {
        this.service = service;
    }

    @PostMapping("/sections")
    public HomepageSection createSection(@RequestBody HomepageSectionRequest req) {
        return service.createSection(req);
    }

    @PostMapping("/blocks")
    public SectionBlock createBlock(@RequestBody SectionBlockRequest req) {
        return service.createBlock(req);
    }
}
