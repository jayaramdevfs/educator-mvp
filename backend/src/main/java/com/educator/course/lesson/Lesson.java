package com.educator.course.lesson;

import com.educator.course.Course;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Course to which this lesson belongs
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Self-referencing parent lesson (NULL for root lessons)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_lesson_id")
    private Lesson parentLesson;

    /**
     * Child lessons (infinite depth)
     */
    @OneToMany(
            mappedBy = "parentLesson",
            cascade = CascadeType.ALL,
            orphanRemoval = false
    )
    @OrderBy("orderIndex ASC")
    private List<Lesson> childLessons = new ArrayList<>();

    /**
     * Hierarchy path (e.g. /course/12/lesson/5/lesson/18)
     */
    @Column(name = "path", length = 512, nullable = false)
    private String path;

    /**
     * Depth level (0 = root lesson)
     */
    @Column(name = "depth_level", nullable = false)
    private int depthLevel;

    /**
     * Lesson type (TEXT / VIDEO / DOCUMENT)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LessonType type;

    /**
     * Order within same parent
     */
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    /**
     * Content fields
     */
    @Column(columnDefinition = "TEXT")
    private String textContent;

    private String videoUrl;

    private String documentUrl;

    /**
     * Soft delete flag
     */
    @Column(nullable = false)
    private boolean isDeleted = false;

    /**
     * Audit fields
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    /* -------------------- Getters & Setters -------------------- */

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Lesson getParentLesson() {
        return parentLesson;
    }

    public void setParentLesson(Lesson parentLesson) {
        this.parentLesson = parentLesson;
    }

    public List<Lesson> getChildLessons() {
        return childLessons;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public void setDepthLevel(int depthLevel) {
        this.depthLevel = depthLevel;
    }

    public LessonType getType() {
        return type;
    }

    public void setType(LessonType type) {
        this.type = type;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
