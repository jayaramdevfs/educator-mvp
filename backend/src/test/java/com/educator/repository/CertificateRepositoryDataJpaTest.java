package com.educator.repository;

import com.educator.certificate.entity.Certificate;
import com.educator.certificate.enums.CertificateStatus;
import com.educator.certificate.repository.CertificateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CertificateRepositoryDataJpaTest {

    @Autowired
    private CertificateRepository repository;

    @Test
    void findByCourseIdAndUserId_returnsMatchingCertificate() {
        UUID userId = UUID.randomUUID();
        Certificate certificate = saveCertificate(11L, userId, LocalDateTime.now().minusDays(1));

        assertThat(repository.findByCourseIdAndUserId(11L, userId))
                .isPresent()
                .get()
                .extracting(Certificate::getId)
                .isEqualTo(certificate.getId());
    }

    @Test
    void findByUserIdOrderByCreatedAtDesc_returnsNewestFirst() {
        UUID userId = UUID.randomUUID();
        Certificate older = saveCertificate(21L, userId, LocalDateTime.now().minusDays(2));
        Certificate newer = saveCertificate(22L, userId, LocalDateTime.now().minusDays(1));
        saveCertificate(23L, UUID.randomUUID(), LocalDateTime.now());

        var page = repository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent())
                .extracting(Certificate::getId)
                .containsExactlyInAnyOrder(newer.getId(), older.getId());
        assertThat(page.getContent().get(0).getCreatedAt())
                .isAfterOrEqualTo(page.getContent().get(1).getCreatedAt());
    }

    private Certificate saveCertificate(Long courseId, UUID userId, LocalDateTime createdAt) {
        Certificate certificate = new Certificate();
        certificate.setCourseId(courseId);
        certificate.setUserId(userId);
        certificate.setCourseCompletionId(UUID.randomUUID());
        certificate.setStatus(CertificateStatus.GENERATED);
        ReflectionTestUtils.setField(certificate, "createdAt", createdAt);
        return repository.save(certificate);
    }
}


