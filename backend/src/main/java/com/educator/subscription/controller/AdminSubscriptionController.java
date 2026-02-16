package com.educator.subscription.controller;

import com.educator.subscription.entity.SubscriptionPlan;
import com.educator.subscription.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/subscriptions")
@RequiredArgsConstructor
public class AdminSubscriptionController {

    private final SubscriptionPlanRepository planRepository;

    @PostMapping
    public SubscriptionPlan createPlan(@RequestBody SubscriptionPlan plan) {
        return planRepository.save(plan);
    }

    @GetMapping
    public List<SubscriptionPlan> listPlans() {
        return planRepository.findAll();
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
}
