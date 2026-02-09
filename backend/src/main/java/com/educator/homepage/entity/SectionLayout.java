package com.educator.homepage.entity;

import com.educator.homepage.enums.LayoutType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "section_layouts")
public class SectionLayout {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID sectionId;

    @Enumerated(EnumType.STRING)
    private LayoutType layoutType;

    private boolean autoScroll;

    private Integer scrollIntervalSeconds;

    /* Getters & Setters */

    public UUID getId() { return id; }

    public UUID getSectionId() { return sectionId; }
    public void setSectionId(UUID sectionId) { this.sectionId = sectionId; }

    public LayoutType getLayoutType() { return layoutType; }
    public void setLayoutType(LayoutType layoutType) { this.layoutType = layoutType; }

    public boolean isAutoScroll() { return autoScroll; }
    public void setAutoScroll(boolean autoScroll) { this.autoScroll = autoScroll; }

    public Integer getScrollIntervalSeconds() { return scrollIntervalSeconds; }
    public void setScrollIntervalSeconds(Integer scrollIntervalSeconds) {
        this.scrollIntervalSeconds = scrollIntervalSeconds;
    }
}
