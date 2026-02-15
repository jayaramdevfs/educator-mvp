package com.educator.exam.service;

import com.educator.exam.dto.UpsertExamOptionRequest;
import com.educator.exam.entity.ExamOption;
import com.educator.exam.entity.ExamQuestion;
import com.educator.exam.repository.ExamOptionRepository;
import com.educator.exam.repository.ExamQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamOptionService {

    private final ExamOptionRepository examOptionRepository;
    private final ExamQuestionRepository examQuestionRepository;

    public ExamOptionService(
            ExamOptionRepository examOptionRepository,
            ExamQuestionRepository examQuestionRepository
    ) {
        this.examOptionRepository = examOptionRepository;
        this.examQuestionRepository = examQuestionRepository;
    }

    // CREATE
    public ExamOption createOption(UUID questionId, UpsertExamOptionRequest request) {

        // Ensure question exists
        examQuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        ExamOption option = new ExamOption();
        option.setQuestionId(questionId);
        option.setOptionText(request.getOptionText());
        option.setCorrect(request.getCorrect());
        option.setDisplayOrder(request.getDisplayOrder());

        return examOptionRepository.save(option);
    }

    // UPDATE
    public ExamOption updateOption(
            UUID questionId,
            UUID optionId,
            UpsertExamOptionRequest request
    ) {

        ExamOption option = examOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        if (!option.getQuestionId().equals(questionId)) {
            throw new IllegalStateException("Option does not belong to this question");
        }

        option.setOptionText(request.getOptionText());
        option.setCorrect(request.getCorrect());
        option.setDisplayOrder(request.getDisplayOrder());

        return examOptionRepository.save(option);
    }

    // DELETE
    public void deleteOption(UUID questionId, UUID optionId) {

        ExamOption option = examOptionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));

        if (!option.getQuestionId().equals(questionId)) {
            throw new IllegalStateException("Option does not belong to this question");
        }

        examOptionRepository.delete(option);
    }

    // LIST
    @Transactional(readOnly = true)
    public List<ExamOption> getOptions(UUID questionId) {

        return examOptionRepository
                .findByQuestionIdInOrderByDisplayOrderAsc(List.of(questionId));
    }
}
