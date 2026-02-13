package com.educator.certificate.controller;

import com.educator.certificate.entity.Certificate;
import com.educator.certificate.service.CertificateService;
import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.common.security.UserIdentityUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/learner/certificates")
public class LearnerCertificateController {

    private final CertificateService certificateService;

    public LearnerCertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public PaginatedResponse<Certificate> listCertificates(
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        UUID userId = UserIdentityUtil.toStableUuid(resolveEmail(authentication));
        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return new PaginatedResponse<>(certificateService.listForUser(userId, pageable));
    }

    private String resolveEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalArgumentException("Authenticated user email is required");
        }
        return authentication.getName();
    }
}
