package com.educator.enrollment.controller;

import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonRepository;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.enrollment.service.LessonProgressService;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/learner/progress")
public class LearnerProgressController {

    private final LessonProgressService lessonProgressService;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    public LearnerProgressController(
            LessonProgressService lessonProgressService,
            EnrollmentRepository enrollmentRepository,
            LessonRepository lessonRepository,
            UserRepository userRepository
    ) {
        this.lessonProgressService = lessonProgressService;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // Start enrollment progress (touch access)
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/enrollment/{enrollmentId}/start")
    public void startProgress(
            Authentication authentication,
            @PathVariable Long enrollmentId
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"
                ));

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Enrollment not found"
                ));

        lessonProgressService.markLessonStarted(user, enrollment);
    }

    // ─────────────────────────────────────────────────────────────
    // Complete lesson
    // ─────────────────────────────────────────────────────────────

    @PostMapping("/enrollment/{enrollmentId}/lesson/{lessonId}/complete")
    public void completeLesson(
            Authentication authentication,
            @PathVariable Long enrollmentId,
            @PathVariable Long lessonId
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"
                ));

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Enrollment not found"
                ));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Lesson not found"
                ));

        lessonProgressService.markLessonCompleted(user, enrollment, lesson);
    }
}
