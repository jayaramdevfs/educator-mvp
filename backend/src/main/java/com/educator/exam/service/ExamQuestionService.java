package com.educator.exam.service;

import com.educator.exam.dto.UpsertExamQuestionRequest;
import com.educator.exam.entity.ExamQuestion;
import com.educator.exam.repository.ExamQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamQuestionService {

    private final ExamQuestionRepository examQuestionRepository;

    public ExamQuestionService(ExamQuestionRepository examQuestionRepository) {
        this.examQuestionRepository = examQuestionRepository;
    }

    // CREATE
    public ExamQuestion createQuestion(UUID examId, UpsertExamQuestionRequest request) {

        ExamQuestion question = new ExamQuestion();
        question.setExamId(examId);
        question.setQuestionText(request.getQuestionText());
        question.setDisplayOrder(request.getDisplayOrder());
        question.setExplanation(request.getExplanation());

        return examQuestionRepository.save(question);
    }

    // UPDATE
    public ExamQuestion updateQuestion(
            UUID examId,
            UUID questionId,
            UpsertExamQuestionRequest request
    ) {

        ExamQuestion question = examQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        if (!question.getExamId().equals(examId)) {
            throw new IllegalStateException("Question does not belong to this exam");
        }

        question.setQuestionText(request.getQuestionText());
        question.setDisplayOrder(request.getDisplayOrder());
        question.setExplanation(request.getExplanation());

        return examQuestionRepository.save(question);
    }

    // DELETE
    public void deleteQuestion(UUID examId, UUID questionId) {

        ExamQuestion question = examQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        if (!question.getExamId().equals(examId)) {
            throw new IllegalStateException("Question does not belong to this exam");
        }

        examQuestionRepository.delete(question);
    }

    // LIST
    @Transactional(readOnly = true)
    public List<ExamQuestion> getQuestions(UUID examId) {
        return examQuestionRepository
                .findByExamIdOrderByDisplayOrderAsc(examId);
    }
}
