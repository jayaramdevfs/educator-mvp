package com.educator.exam.repository;

import com.educator.exam.entity.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, UUID> {

    List<ExamQuestion> findByExamIdOrderByDisplayOrderAsc(UUID examId);

    List<ExamQuestion> findByExamIdAndIdIn(UUID examId, List<UUID> ids);
}
