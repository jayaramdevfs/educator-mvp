package com.educator.course.lesson;

import com.educator.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    /**
     * Fetch lessons of a course in order
     */
    List<Lesson> findByCourseAndIsDeletedFalseOrderByOrderIndexAsc(Course course);
}
