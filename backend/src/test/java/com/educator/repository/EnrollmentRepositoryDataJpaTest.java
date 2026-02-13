package com.educator.repository;

import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.CourseRepository;
import com.educator.course.CourseStatus;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.entity.EnrollmentStatus;
import com.educator.enrollment.repository.EnrollmentRepository;
import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeRepository;
import com.educator.users.User;
import com.educator.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EnrollmentRepositoryDataJpaTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private HierarchyNodeRepository hierarchyNodeRepository;

    @Test
    void derivedQueries_returnExpectedResultsAndCounts() {
        User learner = userRepository.save(new User("learner@example.com", "pw"));
        User other = userRepository.save(new User("other@example.com", "pw"));
        Course course1 = saveCourse("course-1");
        Course course2 = saveCourse("course-2");

        Enrollment active = enrollmentRepository.save(new Enrollment(learner, course1));
        Enrollment dropped = enrollmentRepository.save(new Enrollment(learner, course2));
        dropped.markDropped();
        enrollmentRepository.save(dropped);

        Enrollment otherActive = enrollmentRepository.save(new Enrollment(other, course1));

        assertThat(enrollmentRepository.findByUserAndCourse(learner, course1)).isPresent();
        assertThat(enrollmentRepository.existsByUserAndCourse(learner, course1)).isTrue();
        assertThat(enrollmentRepository.findAllByUser(learner)).hasSize(2);
        assertThat(enrollmentRepository.findAllByUserAndStatus(learner, EnrollmentStatus.ACTIVE))
                .extracting(Enrollment::getId)
                .containsExactly(active.getId());
        assertThat(enrollmentRepository.findAllByUser(learner, PageRequest.of(0, 10)).getContent()).hasSize(2);
        assertThat(enrollmentRepository.findAllByUserAndStatus(learner, EnrollmentStatus.DROPPED, PageRequest.of(0, 10)).getContent())
                .hasSize(1);
        assertThat(enrollmentRepository.countByStatus(EnrollmentStatus.ACTIVE)).isEqualTo(2L);
        assertThat(otherActive.getId()).isNotNull();
    }

    private Course saveCourse(String slug) {
        HierarchyNode node = new HierarchyNode();
        node.setSlug(slug + "-node");
        node.setNameEn(slug + "-node-name");
        node.setDescriptionEn("desc");
        node.setSortOrder(1);
        node.setVisible(true);
        node.setPublished(true);
        node.setCreatedBy("test");
        node = hierarchyNodeRepository.save(node);

        Course course = new Course();
        course.setHierarchyNode(node);
        course.setTitleEn(slug + "-title");
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
}


