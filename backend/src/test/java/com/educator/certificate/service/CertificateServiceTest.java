package com.educator.certificate.service;

import com.educator.certificate.entity.Certificate;
import com.educator.certificate.enums.CertificateStatus;
import com.educator.certificate.repository.CertificateRepository;
import com.educator.completion.entity.CourseCompletion;
import com.educator.completion.repository.CourseCompletionRepository;
import com.educator.notification.service.NotificationPersistenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private CourseCompletionRepository completionRepository;

    @Mock
    private NotificationPersistenceService notificationPersistenceService;

    @InjectMocks
    private CertificateService service;

    @Test
    void generate_createsCertificateWhenMissing() {
        UUID completionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CourseCompletion completion = completion(completionId, 42L, userId);
        when(completionRepository.findById(completionId)).thenReturn(Optional.of(completion));
        when(certificateRepository.findByCourseIdAndUserId(42L, userId)).thenReturn(Optional.empty());
        when(certificateRepository.save(any(Certificate.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Certificate generated = service.generate(completionId);

        ArgumentCaptor<Certificate> certificateCaptor = ArgumentCaptor.forClass(Certificate.class);
        verify(certificateRepository).save(certificateCaptor.capture());
        Certificate saved = certificateCaptor.getValue();
        assertThat(saved.getCourseId()).isEqualTo(42L);
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getCourseCompletionId()).isEqualTo(completionId);
        assertThat(saved.getStatus()).isEqualTo(CertificateStatus.GENERATED);
        assertThat(generated.getStatus()).isEqualTo(CertificateStatus.GENERATED);
    }

    @Test
    void generate_returnsExistingCertificateWithoutSaving() {
        UUID completionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CourseCompletion completion = completion(completionId, 42L, userId);
        Certificate existing = new Certificate();
        existing.setCourseId(42L);
        existing.setUserId(userId);
        existing.setCourseCompletionId(completionId);
        existing.setStatus(CertificateStatus.ISSUED);

        when(completionRepository.findById(completionId)).thenReturn(Optional.of(completion));
        when(certificateRepository.findByCourseIdAndUserId(42L, userId)).thenReturn(Optional.of(existing));

        Certificate result = service.generate(completionId);

        assertThat(result).isSameAs(existing);
        verify(certificateRepository, never()).save(any(Certificate.class));
    }

    @Test
    void generate_throwsWhenCompletionMissing() {
        UUID completionId = UUID.randomUUID();
        when(completionRepository.findById(completionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.generate(completionId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Course completion not found");
    }

    @Test
    void issue_setsIssuedStatusAndTimestamp() {
        UUID certificateId = UUID.randomUUID();
        Certificate certificate = new Certificate();
        certificate.setStatus(CertificateStatus.GENERATED);
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));
        when(certificateRepository.save(certificate)).thenReturn(certificate);

        Certificate result = service.issue(certificateId);

        assertThat(result.getStatus()).isEqualTo(CertificateStatus.ISSUED);
        assertThat(result.getIssuedAt()).isNotNull();
    }

    @Test
    void issue_throwsWhenCertificateMissing() {
        UUID certificateId = UUID.randomUUID();
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.issue(certificateId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Certificate not found");
    }

    @Test
    void revoke_setsRevokedStatusAndTimestamp() {
        UUID certificateId = UUID.randomUUID();
        Certificate certificate = new Certificate();
        certificate.setStatus(CertificateStatus.ISSUED);
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));
        when(certificateRepository.save(certificate)).thenReturn(certificate);

        Certificate result = service.revoke(certificateId);

        assertThat(result.getStatus()).isEqualTo(CertificateStatus.REVOKED);
        assertThat(result.getRevokedAt()).isNotNull();
    }

    @Test
    void revoke_throwsWhenCertificateMissing() {
        UUID certificateId = UUID.randomUUID();
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.revoke(certificateId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Certificate not found");
    }

    @Test
    void listForUser_returnsPagedCertificates() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 20);
        Page<Certificate> page = new PageImpl<>(List.of(new Certificate()));
        when(certificateRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)).thenReturn(page);

        Page<Certificate> result = service.listForUser(userId, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getById_returnsCertificateWhenPresent() {
        UUID certificateId = UUID.randomUUID();
        Certificate certificate = new Certificate();
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.of(certificate));

        Certificate result = service.getById(certificateId);

        assertThat(result).isSameAs(certificate);
    }

    @Test
    void getById_throwsWhenCertificateMissing() {
        UUID certificateId = UUID.randomUUID();
        when(certificateRepository.findById(certificateId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(certificateId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Certificate not found");
    }

    private static CourseCompletion completion(UUID id, Long courseId, UUID userId) {
        CourseCompletion completion = new CourseCompletion();
        ReflectionTestUtils.setField(completion, "id", id);
        completion.setCourseId(courseId);
        completion.setUserId(userId);
        return completion;
    }
}

