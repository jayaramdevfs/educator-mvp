package com.educator.exam.repository;

import com.educator.exam.entity.ExamAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamAttemptAnswerRepository extends JpaRepository<ExamAttemptAnswer, UUID> {

    List<ExamAttemptAnswer> findByAttemptId(UUID attemptId);
}
