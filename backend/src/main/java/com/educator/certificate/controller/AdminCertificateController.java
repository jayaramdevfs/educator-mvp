package com.educator.certificate.controller;

import com.educator.certificate.entity.Certificate;
import com.educator.certificate.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/certificates")
public class AdminCertificateController {

    private final CertificateService certificateService;

    public AdminCertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getById(@PathVariable("id") UUID certificateId) {
        return ResponseEntity.ok(certificateService.getById(certificateId));
    }

    @PostMapping("/generate/{completionId}")
    public ResponseEntity<Certificate> generate(@PathVariable UUID completionId) {
        return ResponseEntity.ok(certificateService.generate(completionId));
    }

    @PostMapping("/{id}/issue")
    public ResponseEntity<Certificate> issue(@PathVariable("id") UUID certificateId) {
        return ResponseEntity.ok(certificateService.issue(certificateId));
    }

    @PostMapping("/{id}/revoke")
    public ResponseEntity<Certificate> revoke(@PathVariable("id") UUID certificateId) {
        return ResponseEntity.ok(certificateService.revoke(certificateId));
    }
}
