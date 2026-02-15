package com.educator.integration;

import com.educator.CourseController;
import com.educator.admin.AdminStatsController;
import com.educator.admin.AdminStatsService;
import com.educator.admin.AdminUserController;
import com.educator.admin.AdminUserService;
import com.educator.admin.dto.AdminStatsResponse;
import com.educator.auth.AuthController;
import com.educator.auth.AuthService;
import com.educator.auth.PasswordResetService;
import com.educator.certificate.controller.AdminCertificateController;
import com.educator.certificate.controller.LearnerCertificateController;
import com.educator.certificate.entity.Certificate;
import com.educator.certificate.enums.CertificateStatus;
import com.educator.certificate.service.CertificateService;
import com.educator.course.Course;
import com.educator.course.CourseDifficulty;
import com.educator.course.CourseRepository;
import com.educator.course.CourseStatus;
import com.educator.course.api.CoursePublicController;
import com.educator.course.lesson.Lesson;
import com.educator.course.lesson.LessonRepository;
import com.educator.course.lesson.LessonType;
import com.educator.course.lesson.api.LessonAdminQueryController;
import com.educator.course.lesson.api.LessonPublicController;
import com.educator.course.lesson.api.LessonTreeController;
import com.educator.course.lesson.service.LessonService;
import com.educator.course.service.CourseService;
import com.educator.enrollment.controller.LearnerEnrollmentController;
import com.educator.enrollment.entity.Enrollment;
import com.educator.enrollment.service.EnrollmentService;
import com.educator.exam.controller.StudentExamController;
import com.educator.exam.dto.ExamAttemptReviewResponse;
import com.educator.exam.entity.ExamAttempt;
import com.educator.exam.enums.AttemptStatus;
import com.educator.exam.service.ExamAttemptService;
import com.educator.hierarchy.HierarchyNode;
import com.educator.hierarchy.HierarchyNodeRepository;
import com.educator.hierarchy.HierarchyNodeService;
import com.educator.hierarchy.api.HierarchyPublicController;
import com.educator.homepage.controller.PublicHomepageController;
import com.educator.homepage.dto.HomepageResponse;
import com.educator.homepage.entity.HomepageSection;
import com.educator.homepage.entity.SectionBlock;
import com.educator.homepage.enums.BlockType;
import com.educator.homepage.enums.SectionPosition;
import com.educator.homepage.service.HomepageQueryService;
import com.educator.notification.controller.LearnerNotificationController;
import com.educator.notification.entity.Notification;
import com.educator.notification.entity.NotificationType;
import com.educator.notification.enums.NotificationStatus;
import com.educator.notification.service.LearnerNotificationService;
import com.educator.profile.LearnerProfileController;
import com.educator.profile.ProfileService;
import com.educator.profile.dto.ProfileResponse;
import com.educator.security.JwtUtil;
import com.educator.users.User;
import com.educator.users.UserRepository;
import com.educator.users.dto.AdminUserResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        AuthController.class,
        LearnerProfileController.class,
        StudentExamController.class,
        LearnerNotificationController.class,
        LearnerCertificateController.class,
        AdminCertificateController.class,
        CoursePublicController.class,
        AdminUserController.class,
        AdminStatsController.class,
        CourseController.class,
        PublicHomepageController.class,
        HierarchyPublicController.class,
        LessonPublicController.class,
        LessonAdminQueryController.class,
        LessonTreeController.class,
        LearnerEnrollmentController.class
})
@AutoConfigureMockMvc(addFilters = false)
class Sprint9EndpointIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean private AuthService authService;
        @MockitoBean private JwtUtil jwtUtil;
        @MockitoBean private PasswordResetService passwordResetService;
        @MockitoBean private ProfileService profileService;
        @MockitoBean private ExamAttemptService examAttemptService;
        @MockitoBean private LearnerNotificationService learnerNotificationService;
        @MockitoBean private CertificateService certificateService;
        @MockitoBean private CourseService courseService;
        @MockitoBean private AdminUserService adminUserService;
        @MockitoBean private AdminStatsService adminStatsService;
        @MockitoBean private HierarchyNodeRepository hierarchyNodeRepository;
        @MockitoBean private HomepageQueryService homepageQueryService;
        @MockitoBean private HierarchyNodeService hierarchyNodeService;
        @MockitoBean private LessonService lessonService;
        @MockitoBean private CourseRepository courseRepository;
        @MockitoBean private EnrollmentService enrollmentService;
        @MockitoBean private UserRepository userRepository;
        @MockitoBean private LessonRepository lessonRepository;

        @Test
        void publicCourseEndpoints_returnSearchAndDetails() throws Exception {
                Course course = mockCourse(50L, "History 101");

                when(courseService.searchPublicCourses(anyString(), any(), any(), any()))
                        .thenReturn(new PageImpl<>(List.of(course), PageRequest.of(0, 20), 1));

                // FIXED HERE
                when(courseRepository.findByIdAndStatusAndIsDeletedFalse(eq(50L),
                        eq(CourseStatus.PUBLISHED)))
                        .thenReturn(java.util.Optional.of(course));

                mockMvc.perform(get("/api/public/courses/search")
                                .param("q", "history")
                                .param("difficulty", "BEGINNER")
                                .param("status", "PUBLISHED"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].titleEn").value("History 101"));

                mockMvc.perform(get("/api/public/courses/{courseId}", 50L))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(50));
        }

        @Test
        void paginatedListEndpoints_returnPaginatedResponses() throws Exception {

                // FIXED HERE (Removed EnrichedBlock)
                when(homepageQueryService.getHomepage(any()))
                        .thenReturn(new PageImpl<>(
                                List.of(
                                        new HomepageResponse(
                                                section("Hero", 1),
                                                List.of(block(1))
                                        )
                                ),
                                PageRequest.of(0, 20),
                                1
                        ));

                mockMvc.perform(get("/api/public/homepage"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content[0].section.title").value("Hero"));
        }

        private static UsernamePasswordAuthenticationToken authPrincipal(String email) {
                return new UsernamePasswordAuthenticationToken(email, "N/A");
        }

        private static Course mockCourse(Long id, String title) {
                Course course = new Course();
                ReflectionTestUtils.setField(course, "id", id);
                course.setTitleEn(title);
                course.setDescriptionEn("desc");
                course.setDifficulty(CourseDifficulty.BEGINNER);
                course.setStatus(CourseStatus.PUBLISHED);
                course.setLanguageCode("en");
                course.setEstimatedDurationMinutes(30);
                course.setCreatedByRole("ADMIN");
                course.setArchived(false);
                course.setDeleted(false);
                return course;
        }

        private static HomepageSection section(String title, int order) {
                HomepageSection section = new HomepageSection();
                section.setTitle(title);
                section.setPosition(SectionPosition.TOP);
                section.setOrderIndex(order);
                section.setEnabled(true);
                return section;
        }

        private static SectionBlock block(int order) {
                SectionBlock block = new SectionBlock();
                block.setSectionId(UUID.randomUUID());
                block.setBlockType(BlockType.TEXT);
                block.setOrderIndex(order);
                block.setEnabled(true);
                return block;
        }
}
