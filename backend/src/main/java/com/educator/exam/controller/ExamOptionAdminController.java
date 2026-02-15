package com.educator.exam.controller;

import com.educator.exam.dto.UpsertExamOptionRequest;
import com.educator.exam.entity.ExamOption;
import com.educator.exam.service.ExamOptionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/questions/{questionId}/options")
public class ExamOptionAdminController {

    private final ExamOptionService examOptionService;

    public ExamOptionAdminController(ExamOptionService examOptionService) {
        this.examOptionService = examOptionService;
    }

    @PostMapping
    public ExamOption createOption(
            @PathVariable UUID questionId,
            @Valid @RequestBody UpsertExamOptionRequest request
    ) {
        return examOptionService.createOption(questionId, request);
    }

    @PutMapping("/{optionId}")
    public ExamOption updateOption(
            @PathVariable UUID questionId,
            @PathVariable UUID optionId,
            @Valid @RequestBody UpsertExamOptionRequest request
    ) {
        return examOptionService.updateOption(questionId, optionId, request);
    }

    @DeleteMapping("/{optionId}")
    public void deleteOption(
            @PathVariable UUID questionId,
            @PathVariable UUID optionId
    ) {
        examOptionService.deleteOption(questionId, optionId);
    }

    @GetMapping
    public List<ExamOption> listOptions(
            @PathVariable UUID questionId
    ) {
        return examOptionService.getOptions(questionId);
    }
}
