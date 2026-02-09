package com.educator.homepage.dto;

import com.educator.homepage.enums.BlockType;

import java.util.UUID;

public class SectionBlockRequest {
    public UUID sectionId;
    public BlockType blockType;
    public int orderIndex;
    public boolean enabled;
}
