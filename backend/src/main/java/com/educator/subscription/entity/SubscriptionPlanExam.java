package com.educator.subscription.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "subscription_plan_exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanExam {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID planId;

    @Column(nullable = false)
    private UUID examId;
}
