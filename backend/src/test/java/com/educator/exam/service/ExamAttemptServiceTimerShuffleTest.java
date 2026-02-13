package com.educator.exam.service;

import com.educator.certificate.service.CertificateService;
import com.educator.completion.repository.CourseCompletionRepository;
import com.educator.exam.entity.Exam;
import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.entity.ExamQuestion;
import com.educator.exam.enums.AttemptStatus;
import com.educator.exam.repository.ExamAttemptAnswerRepository;
import com.educator.exam.repository.ExamAttemptRepository;
import com.educator.exam.repository.ExamOptionRepository;
import com.educator.exam.repository.ExamQuestionRepository;
import com.educator.exam.repository.ExamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExamAttemptServiceTimerShuffleTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ExamAttemptRepository examAttemptRepository;

    @Mock
    private ExamAttemptAnswerRepository examAttemptAnswerRepository;

    @Mock
    private ExamQuestionRepository examQuestionRepository;

    @Mock
    private ExamOptionRepository examOptionRepository;

    @Mock
    private CourseCompletionRepository courseCompletionRepository;

    @Mock
    private CertificateService certificateService;

    @InjectMocks
    private ExamAttemptService service;

    @Test
    void startAttempt_throwsWhenMaxAttemptsExceeded() {
        UUID examId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Exam exam = exam(examId, true, 5, 1);

        when(examRepository.findById(examId)).thenReturn(java.util.Optional.of(exam));
        when(examAttemptRepository.countByExamIdAndUserId(examId, userId)).thenReturn(1L);

        assertThatThrownBy(() -> service.startAttempt(examId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Maximum attempts exceeded");
    }

    @Test
    void startAttempt_usesDisplayOrderWhenShuffleDisabled() {
        UUID examId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Exam exam = exam(examId, false, 5, 3);
        List<ExamQuestion> questions = List.of(
                question(examId, 1),
                question(examId, 2),
                question(examId, 3)
        );

        when(examRepository.findById(examId)).thenReturn(java.util.Optional.of(exam));
        when(examAttemptRepository.countByExamIdAndUserId(examId, userId)).thenReturn(0L);
        when(examQuestionRepository.findByExamIdOrderByDisplayOrderAsc(examId)).thenReturn(questions);
        when(examAttemptRepository.save(any(ExamAttempt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExamAttempt attempt = service.startAttempt(examId, userId);

        String expectedOrder = questions.stream()
                .map(q -> q.getId().toString())
                .collect(Collectors.joining(","));
        assertThat(attempt.getQuestionOrder()).isEqualTo(expectedOrder);
        assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.IN_PROGRESS);
    }

    @Test
    void startAttempt_shufflesQuestionOrderWhenEnabled() {
        UUID examId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Exam exam = exam(examId, true, 5, 50);
        List<ExamQuestion> questions = List.of(
                question(examId, 1),
                question(examId, 2),
                question(examId, 3),
                question(examId, 4),
                question(examId, 5)
        );
        when(examRepository.findById(examId)).thenReturn(java.util.Optional.of(exam));
        when(examAttemptRepository.countByExamIdAndUserId(examId, userId)).thenReturn(0L);
        when(examQuestionRepository.findByExamIdOrderByDisplayOrderAsc(examId)).thenReturn(questions);
        when(examAttemptRepository.save(any(ExamAttempt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<String> observedOrders = new java.util.HashSet<>();
        for (int i = 0; i < 20; i++) {
            ExamAttempt attempt = service.startAttempt(examId, userId);
            observedOrders.add(attempt.getQuestionOrder());
        }

        assertThat(observedOrders).allSatisfy(order -> {
            List<String> ids = List.of(order.split(","));
            assertThat(ids).hasSize(5);
            assertThat(new java.util.HashSet<>(ids))
                    .containsExactlyInAnyOrderElementsOf(
                            questions.stream().map(q -> q.getId().toString()).toList()
                    );
        });
        assertThat(observedOrders.size()).isGreaterThan(1);
    }

    @Test
    void submitAndEvaluateAttempt_expiresAttemptWhenTimedOut() {
        UUID examId = UUID.randomUUID();
        UUID attemptId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Exam exam = exam(examId, false, 5, 3);
        ExamAttempt attempt = new ExamAttempt();
        ReflectionTestUtils.setField(attempt, "id", attemptId);
        attempt.setExamId(examId);
        attempt.setUserId(userId);
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(attempt, "startedAt", LocalDateTime.now().minusMinutes(10));

        when(examAttemptRepository.findById(attemptId)).thenReturn(java.util.Optional.of(attempt));
        when(examRepository.findById(examId)).thenReturn(java.util.Optional.of(exam));

        assertThatThrownBy(() -> service.submitAndEvaluateAttempt(attemptId, userId, List.of()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Time limit exceeded. Attempt expired");

        ArgumentCaptor<ExamAttempt> attemptCaptor = ArgumentCaptor.forClass(ExamAttempt.class);
        verify(examAttemptRepository).save(attemptCaptor.capture());
        ExamAttempt saved = attemptCaptor.getValue();
        assertThat(saved.getStatus()).isEqualTo(AttemptStatus.EXPIRED);
        assertThat(saved.getSubmittedAt()).isNotNull();
        assertThat(saved.getEvaluatedAt()).isNotNull();
        verify(examAttemptAnswerRepository, never()).saveAll(any());
    }

    @Test
    void expireTimedOutAttempts_marksOnlyExpiredAttempts() {
        UUID examId = UUID.randomUUID();

        Exam exam = exam(examId, false, 5, 10);

        ExamAttempt timedOutAttempt = new ExamAttempt();
        timedOutAttempt.setExamId(examId);
        timedOutAttempt.setStatus(AttemptStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(timedOutAttempt, "startedAt", LocalDateTime.now().minusMinutes(8));

        ExamAttempt activeAttempt = new ExamAttempt();
        activeAttempt.setExamId(examId);
        activeAttempt.setStatus(AttemptStatus.IN_PROGRESS);
        ReflectionTestUtils.setField(activeAttempt, "startedAt", LocalDateTime.now().minusMinutes(2));

        when(examAttemptRepository.findByStatus(AttemptStatus.IN_PROGRESS))
                .thenReturn(List.of(timedOutAttempt, activeAttempt));
        when(examRepository.findById(examId)).thenReturn(java.util.Optional.of(exam));

        service.expireTimedOutAttempts();

        assertThat(timedOutAttempt.getStatus()).isEqualTo(AttemptStatus.EXPIRED);
        assertThat(activeAttempt.getStatus()).isEqualTo(AttemptStatus.IN_PROGRESS);
        verify(examAttemptRepository).save(timedOutAttempt);
        verify(examAttemptRepository, never()).save(activeAttempt);
    }

    private static Exam exam(UUID examId, boolean shuffleQuestions, int timeLimitMinutes, int maxAttempts) {
        Exam exam = new Exam();
        ReflectionTestUtils.setField(exam, "id", examId);
        exam.setCourseId(1L);
        exam.setTitle("Mock Exam");
        exam.setPassPercentage(60);
        exam.setShuffleQuestions(shuffleQuestions);
        exam.setTimeLimitMinutes(timeLimitMinutes);
        exam.setMaxAttempts(maxAttempts);
        return exam;
    }

    private static ExamQuestion question(UUID examId, int displayOrder) {
        ExamQuestion question = new ExamQuestion();
        question.setExamId(examId);
        question.setQuestionText("Q" + displayOrder);
        question.setDisplayOrder(displayOrder);
        ReflectionTestUtils.setField(question, "id", UUID.randomUUID());
        return question;
    }
}
