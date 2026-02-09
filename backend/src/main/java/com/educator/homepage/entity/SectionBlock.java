package com.educator.homepage.entity;

import com.educator.homepage.enums.BlockType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "section_blocks")
public class SectionBlock {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID sectionId;

    @Enumerated(EnumType.STRING)
    private BlockType blockType;

    private int orderIndex;

    private boolean enabled = true;

    /* Getters & Setters */

    public UUID getId() { return id; }

    public UUID getSectionId() { return sectionId; }
    public void setSectionId(UUID sectionId) { this.sectionId = sectionId; }

    public BlockType getBlockType() { return blockType; }
    public void setBlockType(BlockType blockType) { this.blockType = blockType; }

    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
