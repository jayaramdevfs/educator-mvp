package com.educator.subscription.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "subscription_plan_courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanCourse {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID planId;

    @Column(nullable = false)
    private Long courseId;
}
