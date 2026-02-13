package com.educator.certificate.repository;

import com.educator.certificate.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

    Optional<Certificate> findByCourseIdAndUserId(Long courseId, UUID userId);

    Page<Certificate> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}
