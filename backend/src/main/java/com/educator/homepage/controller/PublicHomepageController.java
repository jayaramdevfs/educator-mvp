package com.educator.homepage.controller;

import com.educator.homepage.dto.HomepageResponse;
import com.educator.homepage.service.HomepageQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/homepage")
public class PublicHomepageController {

    private final HomepageQueryService service;

    public PublicHomepageController(HomepageQueryService service) {
        this.service = service;
    }

    @GetMapping
    public List<HomepageResponse> getHomepage() {
        return service.getHomepage();
    }
}
