package com.educator.homepage.controller;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.homepage.dto.HomepageResponse;
import com.educator.homepage.service.HomepageQueryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/homepage")
public class PublicHomepageController {

    private final HomepageQueryService service;

    public PublicHomepageController(HomepageQueryService service) {
        this.service = service;
    }

    @GetMapping
    public PaginatedResponse<HomepageResponse> getHomepage(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.ASC, "orderIndex"));
        return new PaginatedResponse<>(service.getHomepage(pageable));
    }
}
