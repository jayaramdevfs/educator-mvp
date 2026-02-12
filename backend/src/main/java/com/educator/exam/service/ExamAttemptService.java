package com.educator.exam.service;

import com.educator.completion.entity.CourseCompletion;
import com.educator.completion.repository.CourseCompletionRepository;
import com.educator.exam.entity.Exam;
import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.entity.ExamAttemptAnswer;
import com.educator.exam.entity.ExamOption;
import com.educator.exam.enums.AttemptStatus;
import com.educator.exam.repository.ExamAttemptRepository;
import com.educator.exam.repository.ExamRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamAttemptService {

    private final ExamRepository examRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final CourseCompletionRepository courseCompletionRepository;
    private final EntityManager entityManager;

    public ExamAttemptService(
            ExamRepository examRepository,
            ExamAttemptRepository examAttemptRepository,
            CourseCompletionRepository courseCompletionRepository,
            EntityManager entityManager
    ) {
        this.examRepository = examRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.courseCompletionRepository = courseCompletionRepository;
        this.entityManager = entityManager;
    }

    public ExamAttempt startAttempt(UUID examId, UUID userId) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        if (exam.getMaxAttempts() != null) {
            long usedAttempts =
                    examAttemptRepository.countByExamIdAndUserId(examId, userId);

            if (usedAttempts >= exam.getMaxAttempts()) {
                throw new IllegalStateException("Maximum attempts exceeded");
            }
        }

        ExamAttempt attempt = new ExamAttempt();
        attempt.setExamId(examId);
        attempt.setUserId(userId);
        attempt.setStatus(AttemptStatus.IN_PROGRESS);

        return examAttemptRepository.save(attempt);
    }

    public ExamAttempt submitAndEvaluateAttempt(
            UUID attemptId,
            List<ExamAttemptAnswer> answers
    ) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));

        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Attempt is not in progress");
        }

        // Persist answers
        answers.forEach(entityManager::persist);

        // Fetch correct options
        TypedQuery<ExamOption> query = entityManager.createQuery(
                "SELECT o FROM ExamOption o WHERE o.questionId IN (" +
                        "SELECT q.id FROM ExamQuestion q WHERE q.examId = :examId" +
                        ")",
                ExamOption.class
        );

        query.setParameter("examId", attempt.getExamId());

        List<ExamOption> correctOptions = query.getResultList();

        int totalQuestions = (int) correctOptions.stream()
                .map(ExamOption::getQuestionId)
                .distinct()
                .count();

        int correctAnswers = 0;

        for (ExamAttemptAnswer answer : answers) {
            for (ExamOption option : correctOptions) {
                if (option.getId().equals(answer.getSelectedOptionId())
                        && option.isCorrect()) {
                    correctAnswers++;
                }
            }
        }

        int scorePercentage =
                (int) ((correctAnswers * 100.0) / totalQuestions);

        Exam exam = examRepository.findById(attempt.getExamId())
                .orElseThrow();

        attempt.setTotalQuestions(totalQuestions);
        attempt.setCorrectAnswers(correctAnswers);
        attempt.setScorePercentage(scorePercentage);
        attempt.setPassed(scorePercentage >= exam.getPassPercentage());
        attempt.setStatus(AttemptStatus.EVALUATED);
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setEvaluatedAt(LocalDateTime.now());

        examAttemptRepository.save(attempt);

        // If passed → create course completion
        if (attempt.getPassed()) {

            CourseCompletion completion = new CourseCompletion();

            // 🔥 NOW RETURNS Long — matches CourseCompletion
            Long courseId = exam.getCourseId();

            completion.setCourseId(courseId);
            completion.setUserId(attempt.getUserId());
            completion.setExamAttemptId(attempt.getId());

            courseCompletionRepository.save(completion);
        }

        return attempt;
    }
}
