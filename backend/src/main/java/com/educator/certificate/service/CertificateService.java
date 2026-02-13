package com.educator.certificate.service;

import com.educator.certificate.entity.Certificate;
import com.educator.certificate.enums.CertificateStatus;
import com.educator.certificate.repository.CertificateRepository;
import com.educator.completion.entity.CourseCompletion;
import com.educator.completion.repository.CourseCompletionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final CourseCompletionRepository completionRepository;

    public CertificateService(
            CertificateRepository certificateRepository,
            CourseCompletionRepository completionRepository
    ) {
        this.certificateRepository = certificateRepository;
        this.completionRepository = completionRepository;
    }

    public Certificate generate(UUID completionId) {
        CourseCompletion completion = completionRepository.findById(completionId)
                .orElseThrow(() -> new IllegalArgumentException("Course completion not found"));

        return certificateRepository
                .findByCourseIdAndUserId(completion.getCourseId(), completion.getUserId())
                .orElseGet(() -> {
                    Certificate certificate = new Certificate();
                    certificate.setCourseId(completion.getCourseId());
                    certificate.setUserId(completion.getUserId());
                    certificate.setCourseCompletionId(completion.getId());
                    certificate.setStatus(CertificateStatus.GENERATED);
                    return certificateRepository.save(certificate);
                });
    }

    public Certificate issue(UUID certificateId) {
        Certificate certificate = getById(certificateId);
        certificate.setStatus(CertificateStatus.ISSUED);
        certificate.setIssuedAt(LocalDateTime.now());
        return certificateRepository.save(certificate);
    }

    public Certificate revoke(UUID certificateId) {
        Certificate certificate = getById(certificateId);
        certificate.setStatus(CertificateStatus.REVOKED);
        certificate.setRevokedAt(LocalDateTime.now());
        return certificateRepository.save(certificate);
    }

    @Transactional(readOnly = true)
    public Page<Certificate> listForUser(UUID userId, Pageable pageable) {
        return certificateRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Certificate getById(UUID certificateId) {
        return certificateRepository.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException("Certificate not found"));
    }
}
