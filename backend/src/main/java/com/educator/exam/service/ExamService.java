package com.educator.exam.service;

import com.educator.exam.entity.Exam;
import com.educator.exam.enums.ExamStatus;
import com.educator.exam.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service responsible for Exam configuration and lifecycle management.
 */
@Service
@Transactional
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    /**
     * Create a new exam for a course.
     * Enforces one-exam-per-course rule.
     */
    public Exam createExam(Exam exam) {
        Optional<Exam> existing = examRepository.findByCourseId(exam.getCourseId());
        if (existing.isPresent()) {
            throw new IllegalStateException("Exam already exists for this course");
        }
        exam.setStatus(ExamStatus.DRAFT);
        return examRepository.save(exam);
    }

    /**
     * Publish an exam.
     */
    public Exam publishExam(UUID examId) {
        Exam exam = getExamOrThrow(examId);
        exam.setStatus(ExamStatus.PUBLISHED);
        return examRepository.save(exam);
    }

    /**
     * Archive an exam.
     */
    public Exam archiveExam(UUID examId) {
        Exam exam = getExamOrThrow(examId);
        exam.setStatus(ExamStatus.ARCHIVED);
        return examRepository.save(exam);
    }

    /**
     * Fetch exam by ID.
     */
    @Transactional(readOnly = true)
    public Exam getExamOrThrow(UUID examId) {
        return examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found"));
    }
}
