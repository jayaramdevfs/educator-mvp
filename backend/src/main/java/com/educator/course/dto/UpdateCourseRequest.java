package com.educator.course.dto;

import com.educator.course.CourseDifficulty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCourseRequest {

    @NotBlank
    @Size(max = 255)
    private String titleEn;

    @NotBlank
    @Size(max = 2000)
    private String descriptionEn;

    @NotNull
    private CourseDifficulty difficulty;

    @NotBlank
    @Size(max = 10)
    private String languageCode;

    @Min(1)
    private int estimatedDurationMinutes;

    @NotNull
    private Long hierarchyNodeId;

    public String getTitleEn() { return titleEn; }
    public void setTitleEn(String titleEn) { this.titleEn = titleEn; }

    public String getDescriptionEn() { return descriptionEn; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }

    public CourseDifficulty getDifficulty() { return difficulty; }
    public void setDifficulty(CourseDifficulty difficulty) { this.difficulty = difficulty; }

    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }

    public int getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(int estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public Long getHierarchyNodeId() { return hierarchyNodeId; }
    public void setHierarchyNodeId(Long hierarchyNodeId) {
        this.hierarchyNodeId = hierarchyNodeId;
    }
}
