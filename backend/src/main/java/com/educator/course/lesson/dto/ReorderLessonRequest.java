package com.educator.course.lesson.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReorderLessonRequest {

    @NotNull
    @Min(0)
    private Integer newOrderIndex;

    public Integer getNewOrderIndex() {
        return newOrderIndex;
    }

    public void setNewOrderIndex(Integer newOrderIndex) {
        this.newOrderIndex = newOrderIndex;
    }
}
