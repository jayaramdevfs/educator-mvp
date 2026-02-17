package com.educator.exam.controller;

import com.educator.exam.entity.Exam;
import com.educator.exam.enums.ExamStatus;
import com.educator.exam.repository.ExamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/public/exams")
public class PublicExamController {

    private final ExamRepository examRepository;

    public PublicExamController(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable Long courseId) {
        return examRepository.findByCourseId(courseId)
                .filter(exam -> exam.getStatus() == ExamStatus.PUBLISHED)
                .map(exam -> ResponseEntity.ok(List.of(exam)))
                .orElse(ResponseEntity.ok(Collections.emptyList()));
    }
}
