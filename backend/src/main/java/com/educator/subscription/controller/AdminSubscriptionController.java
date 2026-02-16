package com.educator.subscription.controller;

import com.educator.subscription.entity.SubscriptionPlan;
import com.educator.subscription.entity.SubscriptionPlanCourse;
import com.educator.subscription.entity.SubscriptionPlanExam;
import com.educator.subscription.repository.SubscriptionPlanCourseRepository;
import com.educator.subscription.repository.SubscriptionPlanExamRepository;
import com.educator.subscription.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/subscriptions")
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionPlanCourseRepository planCourseRepository;
    private final SubscriptionPlanExamRepository planExamRepository;

    // ─────────────────────────────────────────────
    // Basic Plan Management
    // ─────────────────────────────────────────────

    @PostMapping
    public SubscriptionPlan createPlan(@RequestBody SubscriptionPlan plan) {
        return planRepository.save(plan);
    }

    @GetMapping
    public List<SubscriptionPlan> listPlans() {
        return planRepository.findAll();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPlanDetails(@PathVariable UUID id) {

        SubscriptionPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        List<SubscriptionPlanCourse> courses = planCourseRepository.findByPlanId(id);
        List<SubscriptionPlanExam> exams = planExamRepository.findByPlanId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("plan", plan);
        response.put("courses", courses);
        response.put("exams", exams);

        return response;
    }

    @PutMapping("/{id}/enable")
    public void enablePlan(@PathVariable UUID id) {
        SubscriptionPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        plan.setEnabled(true);
        planRepository.save(plan);
    }

    @PutMapping("/{id}/disable")
    public void disablePlan(@PathVariable UUID id) {
        SubscriptionPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        plan.setEnabled(false);
        planRepository.save(plan);
    }

    // ─────────────────────────────────────────────
    // Course Attach / Remove
    // ─────────────────────────────────────────────

    @PostMapping("/{planId}/courses/{courseId}")
    public void attachCourse(@PathVariable UUID planId,
                             @PathVariable Long courseId) {

        if (!planRepository.existsById(planId)) {
            throw new IllegalArgumentException("Plan not found");
        }

        boolean exists = planCourseRepository.existsByPlanIdAndCourseId(planId, courseId);

        if (!exists) {
            SubscriptionPlanCourse mapping = SubscriptionPlanCourse.builder()
                    .planId(planId)
                    .courseId(courseId)
                    .build();

            planCourseRepository.save(mapping);
        }
    }

    @DeleteMapping("/{planId}/courses/{courseId}")
    public void removeCourse(@PathVariable UUID planId,
                             @PathVariable Long courseId) {

        planCourseRepository.deleteByPlanIdAndCourseId(planId, courseId);
    }

    // ─────────────────────────────────────────────
    // Exam Attach / Remove
    // ─────────────────────────────────────────────

    @PostMapping("/{planId}/exams/{examId}")
    public void attachExam(@PathVariable UUID planId,
                           @PathVariable UUID examId) {

        if (!planRepository.existsById(planId)) {
            throw new IllegalArgumentException("Plan not found");
        }

        boolean exists = planExamRepository.existsByPlanIdAndExamId(planId, examId);

        if (!exists) {
            SubscriptionPlanExam mapping = SubscriptionPlanExam.builder()
                    .planId(planId)
                    .examId(examId)
                    .build();

            planExamRepository.save(mapping);
        }
    }

    @DeleteMapping("/{planId}/exams/{examId}")
    public void removeExam(@PathVariable UUID planId,
                           @PathVariable UUID examId) {

        planExamRepository.deleteByPlanIdAndExamId(planId, examId);
    }
}
