package com.educator.hierarchy;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "hierarchy_nodes",
        indexes = {
                @Index(name = "idx_hierarchy_parent", columnList = "parent_id"),
                @Index(name = "idx_hierarchy_slug", columnList = "slug", unique = true)
        }
)
public class HierarchyNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Self-referencing parent (infinite depth)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private HierarchyNode parent;

    /**
     * Children nodes
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("sortOrder ASC")
    private List<HierarchyNode> children = new ArrayList<>();

    /**
     * Human-readable unique identifier (SEO / routing safe)
     */
    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    /**
     * Localization-ready fields (English base)
     */
    @Column(name = "name_en", nullable = false, length = 255)
    private String nameEn;

    @Column(name = "description_en", columnDefinition = "TEXT")
    private String descriptionEn;

    /**
     * Ordering among siblings
     */
    @Column(nullable = false)
    private int sortOrder = 0;

    /**
     * Versioning for safe updates
     */
    @Version
    private Long version;

    /**
     * Visibility & publishing controls
     */
    @Column(nullable = false)
    private boolean isPublished = false;

    @Column(nullable = false)
    private boolean isVisible = true;

    /**
     * Soft delete flag
     */
    @Column(nullable = false)
    private boolean isDeleted = false;

    /**
     * Audit fields
     */
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Column(length = 150)
    private String createdBy;

    /* =====================
       JPA Lifecycle Hooks
       ===================== */

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

    /* =====================
       Getters & Setters
       ===================== */

    public Long getId() {
        return id;
    }

    public HierarchyNode getParent() {
        return parent;
    }

    public void setParent(HierarchyNode parent) {
        this.parent = parent;
    }

    public List<HierarchyNode> getChildren() {
        return children;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getVersion() {
        return version;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
