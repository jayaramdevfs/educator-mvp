package com.educator.repository;

import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.CourseRepository;
import com.educator.course.CourseStatus;
import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryDataJpaTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private HierarchyNodeRepository hierarchyNodeRepository;

    @Test
    void searchPublicCourses_appliesKeywordDifficultyAndStatusFilters() {
        HierarchyNode node = saveNode("history");

        saveCourse(node, "Ancient History", "Great civilization", CourseDifficulty.BEGINNER, CourseStatus.PUBLISHED, false, false);
        saveCourse(node, "Advanced History", "Deep analysis", CourseDifficulty.ADVANCED, CourseStatus.PUBLISHED, false, false);
        saveCourse(node, "Deleted Course", "Should not appear", CourseDifficulty.BEGINNER, CourseStatus.PUBLISHED, false, true);
        saveCourse(node, "Archived Course", "Should not appear", CourseDifficulty.BEGINNER, CourseStatus.PUBLISHED, true, false);
        saveCourse(node, "Draft Course", "Should not appear", CourseDifficulty.BEGINNER, CourseStatus.DRAFT, false, false);

        Page<Course> result = courseRepository.searchPublicCoursesWithQuery(
                "history",
                CourseDifficulty.BEGINNER,
                CourseStatus.PUBLISHED,
                PageRequest.of(0, 20)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitleEn()).isEqualTo("Ancient History");
    }

    @Test
    void searchPublicCoursesWithoutQuery_filtersWithoutTextSearch() {
        HierarchyNode node = saveNode("math");

        saveCourse(node, "Algebra Basics", "Intro to algebra", CourseDifficulty.BEGINNER, CourseStatus.PUBLISHED, false, false);
        saveCourse(node, "Calculus 101", "Derivatives", CourseDifficulty.ADVANCED, CourseStatus.PUBLISHED, false, false);
        saveCourse(node, "Deleted Math", "Gone", CourseDifficulty.BEGINNER, CourseStatus.PUBLISHED, false, true);
        saveCourse(node, "Archived Math", "Old", CourseDifficulty.BEGINNER, CourseStatus.PUBLISHED, true, false);

        Page<Course> result = courseRepository.searchPublicCoursesWithoutQuery(
                CourseDifficulty.BEGINNER,
                CourseStatus.PUBLISHED,
                PageRequest.of(0, 20)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitleEn()).isEqualTo("Algebra Basics");
    }

    @Test
    void findByIsDeletedFalse_returnsOnlyActiveCourses() {
        HierarchyNode node = saveNode("science");
        saveCourse(node, "Visible 1", "desc", CourseDifficulty.BEGINNER, CourseStatus.PUBLISHED, false, false);
        saveCourse(node, "Visible 2", "desc", CourseDifficulty.INTERMEDIATE, CourseStatus.DRAFT, false, false);
        saveCourse(node, "Deleted", "desc", CourseDifficulty.ADVANCED, CourseStatus.PUBLISHED, false, true);

        assertThat(courseRepository.findByIsDeletedFalse()).hasSize(2);
        assertThat(courseRepository.findByIsDeletedFalse(PageRequest.of(0, 10)).getContent()).hasSize(2);
        assertThat(courseRepository.countByIsDeletedFalse()).isEqualTo(2L);
    }

    private HierarchyNode saveNode(String slug) {
        HierarchyNode node = new HierarchyNode();
        node.setSlug(slug);
        node.setNameEn(slug + "-name");
        node.setDescriptionEn("desc");
        node.setSortOrder(1);
        node.setVisible(true);
        node.setPublished(true);
        node.setCreatedBy("test");
        return hierarchyNodeRepository.save(node);
    }

    private void saveCourse(
            HierarchyNode node,
            String title,
            String description,
            CourseDifficulty difficulty,
            CourseStatus status,
            boolean archived,
            boolean deleted
    ) {
        Course course = new Course();
        course.setHierarchyNode(node);
        course.setTitleEn(title);
        course.setDescriptionEn(description);
        course.setDifficulty(difficulty);
        course.setStatus(status);
        course.setLanguageCode("en");
        course.setEstimatedDurationMinutes(30);
        course.setCreatedByRole("ADMIN");
        course.setArchived(archived);
        course.setDeleted(deleted);
        course.setSortOrder(1);
        courseRepository.save(course);
    }
}


