package com.educator.subscription.service;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.subscription.entity.SubscriptionPlan;
import com.educator.subscription.entity.SubscriptionPlanCourse;
import com.educator.subscription.entity.SubscriptionStatus;
import com.educator.subscription.entity.UserSubscription;
import com.educator.subscription.repository.SubscriptionPlanCourseRepository;
import com.educator.subscription.repository.SubscriptionPlanRepository;
import com.educator.subscription.repository.UserSubscriptionRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionPlanCourseRepository planCourseRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    /**
     * Activates a subscription AFTER successful payment confirmation.
     */
    @Transactional
    public void activateSubscription(Long userId, UUID planId) {

        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        if (!plan.isEnabled()) {
            throw new IllegalStateException("Plan disabled");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalDateTime now = LocalDateTime.now();

        UserSubscription subscription =
                userSubscriptionRepository
                        .findByUserIdAndPlanIdAndStatus(userId, planId, SubscriptionStatus.ACTIVE)
                        .orElse(null);

        if (subscription != null && subscription.getExpiresAt().isAfter(now)) {
            subscription.setExpiresAt(subscription.getExpiresAt().plusDays(plan.getDurationDays()));
        } else {
            subscription = UserSubscription.builder()
                    .userId(userId)
                    .planId(planId)
                    .startAt(now)
                    .expiresAt(now.plusDays(plan.getDurationDays()))
                    .status(SubscriptionStatus.ACTIVE)
                    .build();
        }

        userSubscriptionRepository.save(subscription);

        List<SubscriptionPlanCourse> courses =
                planCourseRepository.findByPlanId(planId);

        for (SubscriptionPlanCourse mapping : courses) {

            Course course = courseRepository.findById(mapping.getCourseId())
                    .orElse(null);

            if (course == null) continue;

            boolean alreadyEnrolled =
                    enrollmentRepository.existsByUserAndCourse(user, course);

            if (!alreadyEnrolled) {
                Enrollment enrollment = new Enrollment(user, course);
                enrollmentRepository.save(enrollment);
            }
        }
    }
}
