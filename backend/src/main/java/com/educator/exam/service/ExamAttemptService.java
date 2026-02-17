package com.educator.exam.service;

import com.educator.certificate.service.CertificateService;
import com.educator.completion.entity.CourseCompletion;
import com.educator.completion.repository.CourseCompletionRepository;
import com.educator.exam.dto.ExamAttemptReviewResponse;
import com.educator.exam.entity.Exam;
import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.entity.ExamAttemptAnswer;
import com.educator.exam.entity.ExamOption;
import com.educator.exam.entity.ExamQuestion;
import com.educator.exam.enums.AttemptStatus;
import com.educator.exam.repository.*;
import com.educator.notification.service.NotificationPersistenceService;
import com.educator.security.service.AccessControlService;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamAttemptService {

    private static final Logger log = LoggerFactory.getLogger(ExamAttemptService.class);

    private final ExamRepository examRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final ExamAttemptAnswerRepository examAttemptAnswerRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamOptionRepository examOptionRepository;
    private final CourseCompletionRepository courseCompletionRepository;
    private final CertificateService certificateService;
    private final NotificationPersistenceService notificationPersistenceService;
    private final AccessControlService accessControlService;
    private final UserRepository userRepository;

    public ExamAttemptService(
            ExamRepository examRepository,
            ExamAttemptRepository examAttemptRepository,
            CourseCompletionRepository courseCompletionRepository,
            ExamAttemptAnswerRepository examAttemptAnswerRepository,
            ExamQuestionRepository examQuestionRepository,
            ExamOptionRepository examOptionRepository,
            CertificateService certificateService,
            NotificationPersistenceService notificationPersistenceService,
            AccessControlService accessControlService,
            UserRepository userRepository
    ) {
        this.examRepository = examRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.courseCompletionRepository = courseCompletionRepository;
        this.examAttemptAnswerRepository = examAttemptAnswerRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.examOptionRepository = examOptionRepository;
        this.certificateService = certificateService;
        this.notificationPersistenceService = notificationPersistenceService;
        this.accessControlService = accessControlService;
        this.userRepository = userRepository;
    }

    public ExamAttempt startAttempt(UUID examId, UUID userId, String email) {

        log.info("Exam start requested — examId={}, userId={}, email={}", examId, userId, email);

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        // Resolve the actual User.id (Long PK) from email for AccessControlService
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email=" + email));

        Long dbUserId = user.getId();
        log.info("Resolved email={} to dbUserId={}", email, dbUserId);

        // Subscription / Enrollment access check
        boolean allowed = accessControlService.canAccessExam(dbUserId, examId);

        if (!allowed) {
            log.warn("Access denied for dbUserId={} on examId={} — no active subscription", dbUserId, examId);
            throw new IllegalStateException("Access denied. Active subscription required.");
        }

        if (exam.getMaxAttempts() != null) {
            long usedAttempts = examAttemptRepository.countByExamIdAndUserId(examId, userId);
            if (usedAttempts >= exam.getMaxAttempts()) {
                log.warn("Max attempts reached — examId={}, userId={}, used={}, max={}",
                        examId, userId, usedAttempts, exam.getMaxAttempts());
                throw new IllegalStateException(
                        "Maximum attempts exceeded. You have used " + usedAttempts
                                + " of " + exam.getMaxAttempts() + " allowed attempts.");
            }
        }

        ExamAttempt attempt = new ExamAttempt();
        attempt.setExamId(examId);
        attempt.setUserId(userId);
        attempt.setStatus(AttemptStatus.IN_PROGRESS);
        attempt.setQuestionOrder(buildQuestionOrder(exam));

        ExamAttempt saved = examAttemptRepository.save(attempt);
        log.info("Exam attempt created — attemptId={}, examId={}, userId={}", saved.getId(), examId, userId);
        return saved;
    }

    public ExamAttempt submitAndEvaluateAttempt(
            UUID attemptId,
            UUID userId,
            List<ExamAttemptAnswer> answers
    ) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));

        if (!attempt.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Attempt does not belong to authenticated user");
        }

        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new IllegalStateException("Attempt is not in progress");
        }

        Exam exam = examRepository.findById(attempt.getExamId())
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));

        if (isTimedOut(attempt, exam.getTimeLimitMinutes())) {
            markExpired(attempt);
            throw new IllegalStateException("Time limit exceeded. Attempt expired");
        }

        for (ExamAttemptAnswer answer : answers) {
            answer.setAttemptId(attempt.getId());
        }
        examAttemptAnswerRepository.saveAll(answers);

        List<ExamQuestion> questions =
                examQuestionRepository.findByExamIdOrderByDisplayOrderAsc(attempt.getExamId());

        List<UUID> questionIds = questions.stream().map(ExamQuestion::getId).toList();

        List<ExamOption> options =
                examOptionRepository.findByQuestionIdInOrderByDisplayOrderAsc(questionIds);

        Map<UUID, ExamOption> correctOptionByQuestion = options.stream()
                .filter(ExamOption::isCorrect)
                .collect(Collectors.toMap(
                        ExamOption::getQuestionId,
                        Function.identity(),
                        (a, b) -> a
                ));

        int totalQuestions = questionIds.size();
        int correctAnswers = 0;

        for (ExamAttemptAnswer answer : answers) {
            ExamOption correctOption =
                    correctOptionByQuestion.get(answer.getQuestionId());

            if (correctOption != null &&
                    correctOption.getId().equals(answer.getSelectedOptionId())) {
                correctAnswers++;
            }
        }

        int scorePercentage = totalQuestions == 0
                ? 0
                : (int) ((correctAnswers * 100.0) / totalQuestions);

        attempt.setTotalQuestions(totalQuestions);
        attempt.setCorrectAnswers(correctAnswers);
        attempt.setScorePercentage(scorePercentage);
        attempt.setPassed(scorePercentage >= exam.getPassPercentage());
        attempt.setStatus(AttemptStatus.EVALUATED);
        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setEvaluatedAt(LocalDateTime.now());

        examAttemptRepository.save(attempt);

        if (Boolean.TRUE.equals(attempt.getPassed())) {
            notificationPersistenceService.notifyExamPassed(
                    attempt.getUserId(),
                    exam.getCourseId()
            );
        } else {
            notificationPersistenceService.notifyExamFailed(
                    attempt.getUserId(),
                    exam.getCourseId()
            );
        }

        if (Boolean.TRUE.equals(attempt.getPassed())) {

            CourseCompletion completion =
                    courseCompletionRepository
                            .findByCourseIdAndUserId(
                                    exam.getCourseId(),
                                    attempt.getUserId()
                            )
                            .orElseGet(() -> {
                                CourseCompletion newCompletion =
                                        new CourseCompletion();
                                newCompletion.setCourseId(exam.getCourseId());
                                newCompletion.setUserId(attempt.getUserId());
                                newCompletion.setExamAttemptId(attempt.getId());

                                CourseCompletion saved =
                                        courseCompletionRepository.save(newCompletion);

                                notificationPersistenceService.notifyCourseCompleted(
                                        attempt.getUserId(),
                                        exam.getCourseId()
                                );

                                return saved;
                            });

            certificateService.generate(completion.getId());
        }

        return attempt;
    }

    @Transactional(readOnly = true)
    public Page<ExamAttempt> getAttemptHistory(UUID examId, UUID userId, Pageable pageable) {
        return examAttemptRepository.findByExamIdAndUserIdOrderByStartedAtDesc(
                examId, userId, pageable);
    }

    @Transactional(readOnly = true)
    public ExamAttemptReviewResponse getAttemptReview(UUID attemptId, UUID userId) {

        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new IllegalArgumentException("Attempt not found"));

        if (!attempt.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Attempt does not belong to authenticated user");
        }

        if (attempt.getStatus() != AttemptStatus.EVALUATED) {
            throw new IllegalStateException("Attempt review is available only after evaluation");
        }

        List<ExamAttemptAnswer> answers = examAttemptAnswerRepository.findByAttemptId(attemptId);
        Map<UUID, ExamAttemptAnswer> answerByQuestion = answers.stream()
                .collect(Collectors.toMap(ExamAttemptAnswer::getQuestionId, Function.identity(), (a, b) -> a));

        List<ExamQuestion> questions = getQuestionsInAttemptOrder(attempt);
        List<UUID> questionIds = questions.stream().map(ExamQuestion::getId).toList();
        List<ExamOption> options = examOptionRepository.findByQuestionIdInOrderByDisplayOrderAsc(questionIds);

        Map<UUID, List<ExamOption>> optionsByQuestion = options.stream()
                .collect(Collectors.groupingBy(ExamOption::getQuestionId));

        List<ExamAttemptReviewResponse.QuestionReview> questionReviews = new ArrayList<>();

        for (ExamQuestion question : questions) {
            ExamAttemptAnswer answer = answerByQuestion.get(question.getId());
            List<ExamOption> questionOptions = optionsByQuestion.getOrDefault(question.getId(), List.of());

            Map<UUID, ExamOption> optionById = questionOptions.stream()
                    .collect(Collectors.toMap(ExamOption::getId, Function.identity(), (a, b) -> a));

            ExamOption selectedOption = answer == null ? null : optionById.get(answer.getSelectedOptionId());
            ExamOption correctOption = questionOptions.stream()
                    .filter(ExamOption::isCorrect)
                    .findFirst()
                    .orElse(null);

            boolean correct = selectedOption != null && correctOption != null
                    && selectedOption.getId().equals(correctOption.getId());

            questionReviews.add(new ExamAttemptReviewResponse.QuestionReview(
                    question.getId(),
                    question.getQuestionText(),
                    question.getExplanation(),
                    selectedOption == null ? null : selectedOption.getId(),
                    selectedOption == null ? null : selectedOption.getOptionText(),
                    correctOption == null ? null : correctOption.getId(),
                    correctOption == null ? null : correctOption.getOptionText(),
                    correct
            ));
        }

        return new ExamAttemptReviewResponse(
                attempt.getId(),
                attempt.getExamId(),
                attempt.getScorePercentage(),
                attempt.getPassed(),
                questionReviews
        );
    }

    @Scheduled(fixedDelayString = "${app.exam.expiry-check-ms:60000}")
    public void expireTimedOutAttempts() {
        List<ExamAttempt> inProgressAttempts =
                examAttemptRepository.findByStatus(AttemptStatus.IN_PROGRESS);

        for (ExamAttempt attempt : inProgressAttempts) {
            Exam exam = examRepository.findById(attempt.getExamId()).orElse(null);
            if (exam == null) continue;

            if (isTimedOut(attempt, exam.getTimeLimitMinutes())) {
                markExpired(attempt);
            }
        }
    }

    private boolean isTimedOut(ExamAttempt attempt, Integer timeLimitMinutes) {
        if (timeLimitMinutes == null || timeLimitMinutes <= 0) return false;
        return LocalDateTime.now()
                .isAfter(attempt.getStartedAt().plusMinutes(timeLimitMinutes));
    }

    private void markExpired(ExamAttempt attempt) {
        attempt.setStatus(AttemptStatus.EXPIRED);
        LocalDateTime now = LocalDateTime.now();
        attempt.setSubmittedAt(now);
        attempt.setEvaluatedAt(now);
        examAttemptRepository.save(attempt);
    }

    private String buildQuestionOrder(Exam exam) {
        List<ExamQuestion> questions =
                examQuestionRepository.findByExamIdOrderByDisplayOrderAsc(exam.getId());

        List<UUID> questionIds =
                new ArrayList<>(questions.stream().map(ExamQuestion::getId).toList());

        if (exam.isShuffleQuestions()) {
            Collections.shuffle(questionIds);
        }

        return questionIds.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));
    }

    private List<ExamQuestion> getQuestionsInAttemptOrder(ExamAttempt attempt) {
        List<ExamQuestion> orderedByDisplay =
                examQuestionRepository.findByExamIdOrderByDisplayOrderAsc(attempt.getExamId());

        if (attempt.getQuestionOrder() == null || attempt.getQuestionOrder().isBlank()) {
            return orderedByDisplay;
        }

        List<UUID> orderedIds = Arrays.stream(attempt.getQuestionOrder().split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(UUID::fromString)
                .toList();

        Map<UUID, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < orderedIds.size(); i++) {
            indexMap.put(orderedIds.get(i), i);
        }

        List<ExamQuestion> sorted = new ArrayList<>(orderedByDisplay);
        sorted.sort(Comparator.comparingInt(q -> indexMap.getOrDefault(q.getId(), Integer.MAX_VALUE)));
        return sorted;
    }
}
