package com.educator.course.lesson.api;

import com.educator.course.Course;
import com.educator.course.CourseRepository;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.dto.LessonTreeResponse;
import com.educator.course.lesson.service.LessonService;
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
    public List<LessonTreeResponse> getLessonTree(
            @PathVariable Long courseId
    ) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        List<Lesson> roots = lessonService.getRootLessons(course);

        return roots.stream()
                .map(this::mapTree)
                .collect(Collectors.toList());
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
