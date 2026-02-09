package com.educator.homepage.entity;

import com.educator.homepage.enums.BlockTargetType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "block_configs")
public class BlockConfig {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID blockId;

    private String title;
    private String subtitle;

    private String imageUrl;
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private BlockTargetType targetType;

    private UUID targetId;
    private String targetUrl;

    private boolean clickable = true;

    /* Getters & Setters */

    public UUID getId() { return id; }

    public UUID getBlockId() { return blockId; }
    public void setBlockId(UUID blockId) { this.blockId = blockId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public BlockTargetType getTargetType() { return targetType; }
    public void setTargetType(BlockTargetType targetType) { this.targetType = targetType; }

    public UUID getTargetId() { return targetId; }
    public void setTargetId(UUID targetId) { this.targetId = targetId; }

    public String getTargetUrl() { return targetUrl; }
    public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }

    public boolean isClickable() { return clickable; }
    public void setClickable(boolean clickable) { this.clickable = clickable; }
}
