package com.educator.admin;

import com.educator.certificate.repository.CertificateRepository;
import com.educator.completion.repository.CourseCompletionRepository;
import com.educator.course.CourseRepository;
import com.educator.enrollment.entity.EnrollmentStatus;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.exam.enums.ExamStatus;
import com.educator.exam.repository.ExamRepository;
import com.educator.roles.Role;
import com.educator.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminStatsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseCompletionRepository courseCompletionRepository;

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private AdminStatsService service;

    @Test
    void getStats_aggregatesCountsFromRepositories() {
        when(userRepository.count()).thenReturn(100L);
        when(userRepository.countByRoleName(Role.STUDENT)).thenReturn(80L);
        when(userRepository.countByRoleName(Role.INSTRUCTOR)).thenReturn(15L);
        when(userRepository.countByRoleName(Role.ADMIN)).thenReturn(5L);
        when(courseRepository.countByIsDeletedFalse()).thenReturn(40L);
        when(examRepository.countByStatus(ExamStatus.PUBLISHED)).thenReturn(12L);
        when(enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE)).thenReturn(55L);
        when(courseCompletionRepository.count()).thenReturn(22L);
        when(certificateRepository.count()).thenReturn(20L);

        var stats = service.getStats();

        assertThat(stats.getTotalUsers()).isEqualTo(100L);
        assertThat(stats.getTotalStudents()).isEqualTo(80L);
        assertThat(stats.getTotalInstructors()).isEqualTo(15L);
        assertThat(stats.getTotalAdmins()).isEqualTo(5L);
        assertThat(stats.getTotalCourses()).isEqualTo(40L);
        assertThat(stats.getTotalPublishedExams()).isEqualTo(12L);
        assertThat(stats.getActiveEnrollments()).isEqualTo(55L);
        assertThat(stats.getTotalCompletions()).isEqualTo(22L);
        assertThat(stats.getTotalCertificates()).isEqualTo(20L);
    }

    @Test
    void getStats_usesExpectedRoleNamesAndEnums() {
        service.getStats();

        verify(userRepository).countByRoleName(Role.STUDENT);
        verify(userRepository).countByRoleName(Role.INSTRUCTOR);
        verify(userRepository).countByRoleName(Role.ADMIN);
        verify(examRepository).countByStatus(ExamStatus.PUBLISHED);
        verify(enrollmentRepository).countByStatus(EnrollmentStatus.ACTIVE);
    }

    @Test
    void getStats_handlesZeroCounts() {
        when(userRepository.count()).thenReturn(0L);
        when(userRepository.countByRoleName(Role.STUDENT)).thenReturn(0L);
        when(userRepository.countByRoleName(Role.INSTRUCTOR)).thenReturn(0L);
        when(userRepository.countByRoleName(Role.ADMIN)).thenReturn(0L);
        when(courseRepository.countByIsDeletedFalse()).thenReturn(0L);
        when(examRepository.countByStatus(ExamStatus.PUBLISHED)).thenReturn(0L);
        when(enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE)).thenReturn(0L);
        when(courseCompletionRepository.count()).thenReturn(0L);
        when(certificateRepository.count()).thenReturn(0L);

        var stats = service.getStats();

        assertThat(stats.getTotalUsers()).isZero();
        assertThat(stats.getTotalStudents()).isZero();
        assertThat(stats.getTotalInstructors()).isZero();
        assertThat(stats.getTotalAdmins()).isZero();
        assertThat(stats.getTotalCourses()).isZero();
        assertThat(stats.getTotalPublishedExams()).isZero();
        assertThat(stats.getActiveEnrollments()).isZero();
        assertThat(stats.getTotalCompletions()).isZero();
        assertThat(stats.getTotalCertificates()).isZero();
    }
}

