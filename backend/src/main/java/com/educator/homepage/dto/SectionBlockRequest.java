package com.educator.homepage.dto;

import com.educator.homepage.enums.BlockType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class SectionBlockRequest {
    @NotNull(message = "Section id is required")
    public UUID sectionId;

    @NotNull(message = "Block type is required")
    public BlockType blockType;

    @Min(value = 0, message = "Order index must be >= 0")
    public int orderIndex;

    public boolean enabled;
}
