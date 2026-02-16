package com.educator.security.service;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.subscription.entity.SubscriptionPlanCourse;
import com.educator.subscription.entity.SubscriptionPlanExam;
import com.educator.subscription.entity.SubscriptionStatus;
import com.educator.subscription.entity.UserSubscription;
import com.educator.subscription.repository.SubscriptionPlanCourseRepository;
import com.educator.subscription.repository.SubscriptionPlanExamRepository;
import com.educator.subscription.repository.UserSubscriptionRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final SubscriptionPlanCourseRepository planCourseRepository;
    private final SubscriptionPlanExamRepository planExamRepository;

    public boolean canAccessCourse(Long userId, Long courseId) {

        User user = userRepository.findById(userId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (user == null || course == null) return false;

        if (enrollmentRepository.existsByUserAndCourse(user, course)) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();

        List<UserSubscription> activeSubscriptions =
                userSubscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        for (UserSubscription subscription : activeSubscriptions) {

            if (subscription.getExpiresAt().isBefore(now)) {
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                userSubscriptionRepository.save(subscription);
                continue;
            }

            List<SubscriptionPlanCourse> mappings =
                    planCourseRepository.findByPlanId(subscription.getPlanId());

            for (SubscriptionPlanCourse mapping : mappings) {
                if (mapping.getCourseId().equals(courseId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canAccessExam(Long userId, UUID examId) {

        LocalDateTime now = LocalDateTime.now();

        List<UserSubscription> activeSubscriptions =
                userSubscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        for (UserSubscription subscription : activeSubscriptions) {

            if (subscription.getExpiresAt().isBefore(now)) {
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                userSubscriptionRepository.save(subscription);
                continue;
            }

            List<SubscriptionPlanExam> exams =
                    planExamRepository.findByPlanId(subscription.getPlanId());

            for (SubscriptionPlanExam mapping : exams) {
                if (mapping.getExamId().equals(examId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasActiveSubscription(Long userId) {

        LocalDateTime now = LocalDateTime.now();

        List<UserSubscription> activeSubscriptions =
                userSubscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        return activeSubscriptions.stream()
                .anyMatch(sub -> sub.getExpiresAt().isAfter(now));
    }
}
