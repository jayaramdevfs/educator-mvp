package com.educator.exam.controller;

import com.educator.exam.dto.UpsertExamQuestionRequest;
import com.educator.exam.entity.ExamQuestion;
import com.educator.exam.service.ExamQuestionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/exams/{examId}/questions")
public class ExamQuestionAdminController {

    private final ExamQuestionService examQuestionService;

    public ExamQuestionAdminController(ExamQuestionService examQuestionService) {
        this.examQuestionService = examQuestionService;
    }

    @PostMapping
    public ExamQuestion createQuestion(
            @PathVariable UUID examId,
            @Valid @RequestBody UpsertExamQuestionRequest request
    ) {
        return examQuestionService.createQuestion(examId, request);
    }

    @PutMapping("/{questionId}")
    public ExamQuestion updateQuestion(
            @PathVariable UUID examId,
            @PathVariable UUID questionId,
            @Valid @RequestBody UpsertExamQuestionRequest request
    ) {
        return examQuestionService.updateQuestion(examId, questionId, request);
    }

    @DeleteMapping("/{questionId}")
    public void deleteQuestion(
            @PathVariable UUID examId,
            @PathVariable UUID questionId
    ) {
        examQuestionService.deleteQuestion(examId, questionId);
    }

    @GetMapping
    public List<ExamQuestion> listQuestions(
            @PathVariable UUID examId
    ) {
        return examQuestionService.getQuestions(examId);
    }
}
