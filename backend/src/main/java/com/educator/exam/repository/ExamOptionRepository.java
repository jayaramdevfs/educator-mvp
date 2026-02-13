package com.educator.exam.repository;

import com.educator.exam.entity.ExamOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ExamOptionRepository extends JpaRepository<ExamOption, UUID> {

    List<ExamOption> findByQuestionIdInOrderByDisplayOrderAsc(Collection<UUID> questionIds);
}
