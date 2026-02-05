package com.educator.course.lesson.dto;

import java.util.ArrayList;
import java.util.List;

public class LessonTreeResponse {

    private Long id;
    private int orderIndex;
    private int depthLevel;
    private List<LessonTreeResponse> children = new ArrayList<>();

    public LessonTreeResponse() {}

    public LessonTreeResponse(
            Long id,
            int orderIndex,
            int depthLevel
    ) {
        this.id = id;
        this.orderIndex = orderIndex;
        this.depthLevel = depthLevel;
    }

    public Long getId() {
        return id;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public int getDepthLevel() {
        return depthLevel;
    }

    public List<LessonTreeResponse> getChildren() {
        return children;
    }

    public void addChild(LessonTreeResponse child) {
        this.children.add(child);
    }
}
