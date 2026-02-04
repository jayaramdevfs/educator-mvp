package com.educator.course.lesson.api;


import com.educator.course.Course;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonType;
import com.educator.course.lesson.service.LessonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public Lesson addLesson(
            @RequestParam Long courseId,
            @RequestParam LessonType type,
            @RequestParam int orderIndex,
            @RequestParam(required = false) String textContent,
            @RequestParam(required = false) String videoUrl,
            @RequestParam(required = false) String documentUrl
    ) {
        Course course = new Course();
        course.setId(courseId);

        return lessonService.addLesson(
                course,
                type,
                orderIndex,
                textContent,
                videoUrl,
                documentUrl
        );
    }
}
