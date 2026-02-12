package com.educator.homepage.dto;

import com.educator.homepage.enums.SectionPosition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HomepageSectionRequest {
    @NotBlank(message = "Title is required")
    public String title;

    @NotNull(message = "Section position is required")
    public SectionPosition position;

    @Min(value = 0, message = "Order index must be >= 0")
    public int orderIndex;

    public boolean enabled;
}
