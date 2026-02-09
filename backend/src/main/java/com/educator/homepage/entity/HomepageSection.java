package com.educator.homepage.entity;

import com.educator.homepage.enums.SectionPosition;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "homepage_sections")
public class HomepageSection {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    @Enumerated(EnumType.STRING)
    private SectionPosition position;

    private int orderIndex;

    private boolean enabled = true;

    /* Getters & Setters */

    public UUID getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public SectionPosition getPosition() { return position; }
    public void setPosition(SectionPosition position) { this.position = position; }

    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
