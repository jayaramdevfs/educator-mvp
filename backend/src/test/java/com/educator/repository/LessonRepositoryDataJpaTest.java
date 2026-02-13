package com.educator.repository;

import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.CourseRepository;
import com.educator.course.CourseStatus;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonRepository;
import com.educator.course.lesson.LessonType;
import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LessonRepositoryDataJpaTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private HierarchyNodeRepository hierarchyNodeRepository;

    @Test
    void findAllByCourseFlatOrdered_ordersByPathThenOrderIndex() {
        Course course = saveCourse();

        Lesson lesson2 = saveLesson(course, null, "/course/1/lesson/20", 0, 2, false);
        Lesson lesson1 = saveLesson(course, null, "/course/1/lesson/10", 0, 1, false);
        saveLesson(course, null, "/course/1/lesson/30", 0, 3, true);

        List<Lesson> result = lessonRepository.findAllByCourseFlatOrdered(course);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(lesson1.getId());
        assertThat(result.get(1).getId()).isEqualTo(lesson2.getId());
    }

    @Test
    void findRootLessonsForTree_returnsOnlyRootNonDeletedOrderedByOrderIndex() {
        Course course = saveCourse();

        Lesson root2 = saveLesson(course, null, "/course/1/lesson/2", 0, 2, false);
        Lesson root1 = saveLesson(course, null, "/course/1/lesson/1", 0, 1, false);
        saveLesson(course, root1, "/course/1/lesson/1/lesson/3", 1, 1, false);
        saveLesson(course, null, "/course/1/lesson/4", 0, 4, true);

        List<Lesson> result = lessonRepository.findRootLessonsForTree(course);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(root1.getId());
        assertThat(result.get(1).getId()).isEqualTo(root2.getId());
    }

    @Test
    void existsByParentLessonAndIsDeletedFalse_checksChildPresence() {
        Course course = saveCourse();
        Lesson parent = saveLesson(course, null, "/course/1/lesson/1", 0, 1, false);
        saveLesson(course, parent, "/course/1/lesson/1/lesson/2", 1, 1, false);
        saveLesson(course, parent, "/course/1/lesson/1/lesson/3", 1, 2, true);

        assertThat(lessonRepository.existsByParentLessonAndIsDeletedFalse(parent)).isTrue();
    }

    private Course saveCourse() {
        HierarchyNode node = new HierarchyNode();
        node.setSlug("lesson-node-" + System.nanoTime());
        node.setNameEn("Lesson Node");
        node.setDescriptionEn("desc");
        node.setSortOrder(1);
        node.setVisible(true);
        node.setPublished(true);
        node.setCreatedBy("test");
        node = hierarchyNodeRepository.save(node);

        Course course = new Course();
        course.setHierarchyNode(node);
        course.setTitleEn("Lesson Course");
        course.setDescriptionEn("desc");
        course.setDifficulty(CourseDifficulty.BEGINNER);
        course.setStatus(CourseStatus.PUBLISHED);
        course.setLanguageCode("en");
        course.setEstimatedDurationMinutes(30);
        course.setCreatedByRole("ADMIN");
        course.setArchived(false);
        course.setDeleted(false);
        course.setSortOrder(1);
        return courseRepository.save(course);
    }

    private Lesson saveLesson(
            Course course,
            Lesson parent,
            String path,
            int depth,
            int order,
            boolean deleted
    ) {
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setParentLesson(parent);
        lesson.setPath(path);
        lesson.setDepthLevel(depth);
        lesson.setType(LessonType.TEXT);
        lesson.setOrderIndex(order);
        lesson.setTextContent("content");
        lesson.setDeleted(deleted);
        return lessonRepository.save(lesson);
    }
}


