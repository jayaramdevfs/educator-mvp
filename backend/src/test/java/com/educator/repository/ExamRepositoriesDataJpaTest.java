package com.educator.repository;

import com.educator.exam.entity.Exam;
import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.entity.ExamAttemptAnswer;
import com.educator.exam.entity.ExamOption;
import com.educator.exam.entity.ExamQuestion;
import com.educator.exam.enums.AttemptStatus;
import com.educator.exam.enums.ExamStatus;
import com.educator.exam.repository.ExamAttemptAnswerRepository;
import com.educator.exam.repository.ExamAttemptRepository;
import com.educator.exam.repository.ExamOptionRepository;
import com.educator.exam.repository.ExamQuestionRepository;
import com.educator.exam.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ExamRepositoriesDataJpaTest {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ExamOptionRepository examOptionRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamAttemptAnswerRepository examAttemptAnswerRepository;

    @Test
    void examRepository_methodsWorkForCourseAndStatus() {
        Exam published = saveExam(100L, ExamStatus.PUBLISHED);
        saveExam(101L, ExamStatus.DRAFT);

        assertThat(examRepository.findByCourseId(100L)).isPresent();
        assertThat(examRepository.existsByCourseIdAndStatus(100L, ExamStatus.PUBLISHED)).isTrue();
        assertThat(examRepository.existsByCourseIdAndStatus(100L, ExamStatus.DRAFT)).isFalse();
        assertThat(examRepository.countByStatus(ExamStatus.PUBLISHED)).isEqualTo(1L);
        assertThat(published.getId()).isNotNull();
    }

    @Test
    void questionAndOptionRepositories_returnExpectedOrderedData() {
        Exam exam = saveExam(200L, ExamStatus.PUBLISHED);

        ExamQuestion q2 = saveQuestion(exam.getId(), 2);
        ExamQuestion q1 = saveQuestion(exam.getId(), 1);

        ExamOption option2 = saveOption(q1.getId(), 2, false);
        ExamOption option1 = saveOption(q1.getId(), 1, true);

        List<ExamQuestion> orderedQuestions = examQuestionRepository.findByExamIdOrderByDisplayOrderAsc(exam.getId());
        assertThat(orderedQuestions).extracting(ExamQuestion::getId).containsExactly(q1.getId(), q2.getId());

        List<ExamQuestion> filteredQuestions = examQuestionRepository.findByExamIdAndIdIn(
                exam.getId(),
                List.of(q1.getId())
        );
        assertThat(filteredQuestions).extracting(ExamQuestion::getId).containsExactly(q1.getId());

        List<ExamOption> options = examOptionRepository.findByQuestionIdInOrderByDisplayOrderAsc(List.of(q1.getId()));
        assertThat(options).extracting(ExamOption::getId).containsExactly(option1.getId(), option2.getId());
    }

    @Test
    void attemptAndAnswerRepositories_supportHistoryLatestAndAnswerLookup() {
        Exam exam = saveExam(300L, ExamStatus.PUBLISHED);
        UUID userId = UUID.randomUUID();

        ExamAttempt older = saveAttempt(exam.getId(), userId, AttemptStatus.EVALUATED, LocalDateTime.now().minusDays(2));
        ExamAttempt latest = saveAttempt(exam.getId(), userId, AttemptStatus.EVALUATED, LocalDateTime.now().minusHours(1));
        saveAttempt(exam.getId(), userId, AttemptStatus.IN_PROGRESS, null);

        assertThat(examAttemptRepository.findByExamIdAndUserId(exam.getId(), userId)).hasSize(3);
        assertThat(examAttemptRepository.countByExamIdAndUserId(exam.getId(), userId)).isEqualTo(3L);

        assertThat(examAttemptRepository.findTopByExamIdAndUserIdAndStatusOrderByEvaluatedAtDesc(
                exam.getId(),
                userId,
                AttemptStatus.EVALUATED
        )).isPresent().get().extracting(ExamAttempt::getId).isEqualTo(latest.getId());

        assertThat(examAttemptRepository.findByExamIdAndUserIdOrderByStartedAtDesc(
                exam.getId(),
                userId,
                PageRequest.of(0, 10)
        ).getContent()).hasSize(3);

        ExamQuestion question = saveQuestion(exam.getId(), 1);
        ExamOption option = saveOption(question.getId(), 1, true);

        ExamAttemptAnswer answer = new ExamAttemptAnswer();
        answer.setAttemptId(older.getId());
        answer.setQuestionId(question.getId());
        answer.setSelectedOptionId(option.getId());
        answer = examAttemptAnswerRepository.save(answer);

        assertThat(examAttemptAnswerRepository.findByAttemptId(older.getId()))
                .extracting(ExamAttemptAnswer::getId)
                .containsExactly(answer.getId());
    }

    private Exam saveExam(Long courseId, ExamStatus status) {
        Exam exam = new Exam();
        exam.setCourseId(courseId);
        exam.setTitle("Exam-" + courseId);
        exam.setDescription("desc");
        exam.setInstructions("instructions");
        exam.setRulesSummary("rules");
        exam.setPassPercentage(60);
        exam.setMaxAttempts(3);
        exam.setTimeLimitMinutes(30);
        exam.setShuffleQuestions(false);
        exam.setShuffleOptions(false);
        exam.setStatus(status);
        return examRepository.save(exam);
    }

    private ExamQuestion saveQuestion(UUID examId, int displayOrder) {
        ExamQuestion question = new ExamQuestion();
        question.setExamId(examId);
        question.setQuestionText("Q-" + displayOrder);
        question.setDisplayOrder(displayOrder);
        question.setExplanation("exp");
        return examQuestionRepository.save(question);
    }

    private ExamOption saveOption(UUID questionId, int displayOrder, boolean correct) {
        ExamOption option = new ExamOption();
        option.setQuestionId(questionId);
        option.setOptionText("Option-" + displayOrder);
        option.setDisplayOrder(displayOrder);
        option.setCorrect(correct);
        return examOptionRepository.save(option);
    }

    private ExamAttempt saveAttempt(UUID examId, UUID userId, AttemptStatus status, LocalDateTime evaluatedAt) {
        ExamAttempt attempt = new ExamAttempt();
        attempt.setExamId(examId);
        attempt.setUserId(userId);
        attempt.setStatus(status);
        attempt.setPassed(status == AttemptStatus.EVALUATED);
        attempt.setScorePercentage(status == AttemptStatus.EVALUATED ? 80 : null);
        attempt.setEvaluatedAt(evaluatedAt);
        attempt = examAttemptRepository.save(attempt);

        // Ensure deterministic history ordering for startedAt-desc queries.
        ReflectionTestUtils.setField(attempt, "startedAt", LocalDateTime.now().minusMinutes((long) (Math.random() * 60)));
        return examAttemptRepository.save(attempt);
    }
}


