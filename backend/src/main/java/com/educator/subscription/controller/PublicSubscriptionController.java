package com.educator.subscription.controller;

import com.educator.subscription.entity.SubscriptionPlan;
import com.educator.subscription.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/subscriptions")
@RequiredArgsConstructor
public class PublicSubscriptionController {

    private final SubscriptionPlanRepository planRepository;

    @GetMapping("/plans")
    public List<SubscriptionPlan> getAvailablePlans() {
        return planRepository.findAll().stream()
                .filter(SubscriptionPlan::isEnabled)
                .toList();
    }
}
