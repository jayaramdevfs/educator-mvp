package com.educator.course.lesson.api;

import com.educator.common.dto.PaginatedResponse;
import com.educator.common.pagination.PageableFactory;
import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.dto.LessonTreeResponse;
import com.educator.course.lesson.service.LessonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/lesson-tree")
public class LessonTreeController {

    private final LessonService lessonService;
    private final CourseRepository courseRepository;

    public LessonTreeController(
            LessonService lessonService,
            CourseRepository courseRepository
    ) {
        this.lessonService = lessonService;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/course/{courseId}")
    public PaginatedResponse<LessonTreeResponse> getLessonTree(
            @PathVariable Long courseId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Pageable pageable = PageableFactory.of(page, size, Sort.by(Sort.Direction.ASC, "orderIndex"));
        Page<Lesson> rootPage = lessonService.getLessonsForCourse(course, pageable);

        List<LessonTreeResponse> treeResponses = rootPage.getContent().stream()
                .map(this::mapTree)
                .collect(Collectors.toList());

        Page<LessonTreeResponse> responsePage = new PageImpl<>(
                treeResponses,
                pageable,
                rootPage.getTotalElements()
        );

        return new PaginatedResponse<>(responsePage);
    }

    private LessonTreeResponse mapTree(Lesson lesson) {
        LessonTreeResponse node = new LessonTreeResponse(
                lesson.getId(),
                lesson.getOrderIndex(),
                lesson.getDepthLevel()
        );

        List<Lesson> children = lessonService.getChildLessons(lesson);

        for (Lesson child : children) {
            node.addChild(mapTree(child));
        }

        return node;
    }
}
