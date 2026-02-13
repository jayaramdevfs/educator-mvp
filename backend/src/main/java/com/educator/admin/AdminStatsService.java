package com.educator.admin;

import com.educator.admin.dto.AdminStatsResponse;
import com.educator.certificate.repository.CertificateRepository;
import com.educator.completion.repository.CourseCompletionRepository;
import com.educator.course.CourseRepository;
import com.educator.enrollment.entity.EnrollmentStatus;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.exam.enums.ExamStatus;
import com.educator.exam.repository.ExamRepository;
import com.educator.roles.Role;
import com.educator.users.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminStatsService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseCompletionRepository courseCompletionRepository;
    private final CertificateRepository certificateRepository;

    public AdminStatsService(
            UserRepository userRepository,
            CourseRepository courseRepository,
            ExamRepository examRepository,
            EnrollmentRepository enrollmentRepository,
            CourseCompletionRepository courseCompletionRepository,
            CertificateRepository certificateRepository
    ) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.examRepository = examRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.courseCompletionRepository = courseCompletionRepository;
        this.certificateRepository = certificateRepository;
    }

    public AdminStatsResponse getStats() {
        return new AdminStatsResponse(
                userRepository.count(),
                userRepository.countByRoleName(Role.STUDENT),
                userRepository.countByRoleName(Role.INSTRUCTOR),
                userRepository.countByRoleName(Role.ADMIN),
                courseRepository.countByIsDeletedFalse(),
                examRepository.countByStatus(ExamStatus.PUBLISHED),
                enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE),
                courseCompletionRepository.count(),
                certificateRepository.count()
        );
    }
}
