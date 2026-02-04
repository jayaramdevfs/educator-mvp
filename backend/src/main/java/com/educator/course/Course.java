package com.educator.course;

import com.educator.hierarchy.HierarchyNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(
        name = "courses",
        indexes = {
                @Index(name = "idx_course_hierarchy", columnList = "hierarchy_node_id"),
                @Index(name = "idx_course_status", columnList = "status"),
                @Index(name = "idx_course_difficulty", columnList = "difficulty")
        }
)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Hierarchy attachment (category / sub-category / etc)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hierarchy_node_id", nullable = false)
    private HierarchyNode hierarchyNode;

    /**
     * English base title (translations later)
     */
    @Column(nullable = false, length = 200)
    private String titleEn;

    /**
     * English base description
     */
    @Column(columnDefinition = "TEXT")
    private String descriptionEn;

    /**
     * Course status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseStatus status = CourseStatus.DRAFT;

    /**
     * Difficulty level
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseDifficulty difficulty = CourseDifficulty.BEGINNER;

    /**
     * Primary language code (en, te, hi, etc)
     */
    @Column(nullable = false, length = 10)
    private String languageCode = "en";

    /**
     * Estimated duration in minutes
     */
    @Column(nullable = false)
    private int estimatedDurationMinutes = 0;

    /**
     * Role who created this course (ADMIN / INSTRUCTOR)
     */
    @Column(nullable = false, length = 50)
    private String createdByRole = "ADMIN";

    /**
     * Visibility & lifecycle flags
     */
    @Column(nullable = false)
    private boolean isArchived = false;

    @Column(nullable = false)
    private boolean isDeleted = false;

    /**
     * Ordering
     */
    @Column(nullable = false)
    private int sortOrder = 0;

    /**
     * Optimistic locking
     */
    @Version
    private Long version;

    /**
     * Audit timestamps
     */
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
