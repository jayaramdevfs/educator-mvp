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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private PasswordResetService passwordResetService;

    @MockitoBean
    private ProfileService profileService;

    @MockitoBean
    private ExamAttemptService examAttemptService;

    @MockitoBean
    private LearnerNotificationService learnerNotificationService;

    @MockitoBean
    private CertificateService certificateService;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private AdminUserService adminUserService;

    @MockitoBean
    private AdminStatsService adminStatsService;

    @MockitoBean
    private HierarchyNodeRepository hierarchyNodeRepository;

    @MockitoBean
    private HomepageQueryService homepageQueryService;

    @MockitoBean
    private HierarchyNodeService hierarchyNodeService;

    @MockitoBean
    private LessonService lessonService;

    @MockitoBean
    private CourseRepository courseRepository;

    @MockitoBean
    private EnrollmentService enrollmentService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private LessonRepository lessonRepository;

    @Test
    void authResetRequestEndpoint_returnsResponsePayload() throws Exception {
        when(passwordResetService.createResetToken("learner@example.com")).thenReturn("reset-token");

        mockMvc.perform(post("/api/auth/reset-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"learner@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.token").value("reset-token"));
    }

    @Test
    void authResetConfirmEndpoint_returnsSuccess() throws Exception {
        doNothing().when(passwordResetService).resetPassword("reset-token", "StrongPass1");

        mockMvc.perform(post("/api/auth/reset-confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"reset-token\",\"newPassword\":\"StrongPass1\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void authRefreshEndpoint_returnsNewJwt() throws Exception {
        User user = new User("learner@example.com", "encoded");
        when(jwtUtil.validateToken("old-token")).thenReturn(true);
        when(jwtUtil.extractEmail("old-token")).thenReturn("learner@example.com");
        when(authService.getUserByEmail("learner@example.com")).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("new-token");

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"old-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new-token"));
    }

    @Test
    void learnerProfileEndpoints_workForGetUpdateAndPasswordChange() throws Exception {
        ProfileResponse profile = new ProfileResponse(1L, "learner@example.com", Set.of("STUDENT"));
        when(profileService.getProfile("learner@example.com")).thenReturn(profile);
        when(profileService.updateProfile(eq("learner@example.com"), eq("new@example.com"))).thenReturn(
                new ProfileResponse(1L, "new@example.com", Set.of("STUDENT"))
        );
        doNothing().when(profileService).changePassword("learner@example.com", "OldPass1", "NewPass1");

        mockMvc.perform(get("/api/learner/profile")
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("learner@example.com"));

        mockMvc.perform(put("/api/learner/profile")
                        .principal(authPrincipal("learner@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"new@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@example.com"));

        mockMvc.perform(put("/api/learner/profile/password")
                        .principal(authPrincipal("learner@example.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"OldPass1\",\"newPassword\":\"NewPass1\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void studentExamAttemptHistoryAndReviewEndpoints_returnData() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID attemptId = UUID.randomUUID();

        ExamAttempt attempt = new ExamAttempt();
        ReflectionTestUtils.setField(attempt, "id", attemptId);
        attempt.setExamId(examId);
        attempt.setUserId(UUID.randomUUID());
        attempt.setStatus(AttemptStatus.EVALUATED);

        when(examAttemptService.getAttemptHistory(any(UUID.class), any(UUID.class), any()))
                .thenReturn(new PageImpl<>(List.of(attempt), PageRequest.of(0, 20), 1));
        when(examAttemptService.getAttemptReview(any(UUID.class), any(UUID.class)))
                .thenReturn(new ExamAttemptReviewResponse(
                        attemptId,
                        examId,
                        80,
                        true,
                        List.of()
                ));

        mockMvc.perform(get("/api/student/exams/{examId}/attempts", examId)
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].examId").value(examId.toString()));

        mockMvc.perform(get("/api/student/exams/attempts/{attemptId}/review", attemptId)
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attemptId").value(attemptId.toString()));
    }

    @Test
    void notificationEndpoints_returnListReadAndUnreadCount() throws Exception {
        UUID notificationId = UUID.randomUUID();
        Notification notification = new Notification();
        ReflectionTestUtils.setField(notification, "id", notificationId);
        notification.setUserId(UUID.randomUUID());
        notification.setType(NotificationType.EXAM_PASSED);
        notification.setTitle("Passed");
        notification.setMessage("You passed");
        notification.setStatus(NotificationStatus.PERSISTED);
        notification.setRead(false);
        ReflectionTestUtils.setField(notification, "createdAt", Instant.now());

        when(learnerNotificationService.list(any(UUID.class), any()))
                .thenReturn(new PageImpl<>(List.of(notification), PageRequest.of(0, 20), 1));
        when(learnerNotificationService.markRead(any(UUID.class), eq(notificationId))).thenReturn(notification);
        when(learnerNotificationService.unreadCount(any(UUID.class))).thenReturn(3L);

        mockMvc.perform(get("/api/learner/notifications")
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(notificationId.toString()));

        mockMvc.perform(put("/api/learner/notifications/{id}/read", notificationId)
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notificationId.toString()));

        mockMvc.perform(get("/api/learner/notifications/unread-count")
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unreadCount").value(3));
    }

    @Test
    void certificateEndpoints_coverLearnerAndAdminApis() throws Exception {
        UUID certificateId = UUID.randomUUID();
        UUID completionId = UUID.randomUUID();
        Certificate certificate = new Certificate();
        ReflectionTestUtils.setField(certificate, "id", certificateId);
        certificate.setCourseId(10L);
        certificate.setUserId(UUID.randomUUID());
        certificate.setCourseCompletionId(completionId);
        certificate.setStatus(CertificateStatus.GENERATED);

        when(certificateService.listForUser(any(UUID.class), any()))
                .thenReturn(new PageImpl<>(List.of(certificate), PageRequest.of(0, 20), 1));
        when(certificateService.getById(certificateId)).thenReturn(certificate);
        when(certificateService.generate(completionId)).thenReturn(certificate);
        when(certificateService.issue(certificateId)).thenReturn(certificate);
        when(certificateService.revoke(certificateId)).thenReturn(certificate);

        mockMvc.perform(get("/api/learner/certificates")
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(certificateId.toString()));

        mockMvc.perform(get("/api/admin/certificates/{id}", certificateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificateId.toString()));

        mockMvc.perform(post("/api/admin/certificates/generate/{completionId}", completionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificateId.toString()));

        mockMvc.perform(post("/api/admin/certificates/{id}/issue", certificateId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/admin/certificates/{id}/revoke", certificateId))
                .andExpect(status().isOk());
    }

    @Test
    void publicCourseEndpoints_returnSearchAndDetails() throws Exception {
        Course course = mockCourse(50L, "History 101");
        when(courseService.searchPublicCourses(anyString(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(course), PageRequest.of(0, 20), 1));
        when(courseService.getPublicCourseById(50L)).thenReturn(course);

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
    void adminUserAndStatsEndpoints_returnData() throws Exception {
        AdminUserResponse user = new AdminUserResponse(1L, "admin@example.com", Set.of("ADMIN"));
        when(adminUserService.getUsers(any())).thenReturn(new PageImpl<>(List.of(user), PageRequest.of(0, 20), 1));
        when(adminUserService.updateRoles(eq(1L), anyList())).thenReturn(user);
        when(adminStatsService.getStats()).thenReturn(new AdminStatsResponse(
                10, 6, 2, 2, 5, 3, 4, 2, 2
        ));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("admin@example.com"));

        mockMvc.perform(put("/api/admin/users/{id}/roles", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roles\":[\"ADMIN\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/api/admin/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(10));
    }

    @Test
    void paginatedListEndpoints_returnPaginatedResponses() throws Exception {
        Course course = mockCourse(80L, "Paginated Course");
        when(courseService.getAllActiveCourses(any()))
                .thenReturn(new PageImpl<>(List.of(course), PageRequest.of(0, 20), 1));

        when(homepageQueryService.getHomepage(any())).thenReturn(new PageImpl<>(List.of(
                new HomepageResponse(section("Hero", 1), List.of(block(1)))
        ), PageRequest.of(0, 20), 1));

        when(hierarchyNodeService.getRootNodes(any())).thenReturn(new PageImpl<>(List.of(node(1L, "root")), PageRequest.of(0, 20), 1));
        when(hierarchyNodeService.getChildren(eq(1L), any())).thenReturn(new PageImpl<>(List.of(node(2L, "child")), PageRequest.of(0, 20), 1));

        Course lessonCourse = mockCourse(90L, "Lesson Course");
        when(courseRepository.findById(90L)).thenReturn(java.util.Optional.of(lessonCourse));
        Lesson rootLesson = lesson(100L, 1, 0, null);
        when(lessonService.getLessonsForCourse(eq(lessonCourse), any())).thenReturn(new PageImpl<>(List.of(rootLesson), PageRequest.of(0, 20), 1));
        when(lessonService.getChildLessons(rootLesson)).thenReturn(List.of());

        User learner = new User("learner@example.com", "pw");
        when(userRepository.findByEmail("learner@example.com")).thenReturn(java.util.Optional.of(learner));
        Enrollment enrollment = new Enrollment(learner, lessonCourse);
        when(enrollmentService.getMyEnrollments(eq(learner), any())).thenReturn(new PageImpl<>(List.of(enrollment), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/admin/courses/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titleEn").value("Paginated Course"));

        mockMvc.perform(get("/api/public/homepage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].section.title").value("Hero"));

        mockMvc.perform(get("/api/hierarchy/roots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].slug").value("root"));

        mockMvc.perform(get("/api/hierarchy/{parentId}/children", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].slug").value("child"));

        mockMvc.perform(get("/api/public/lessons/course/{courseId}", 90L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(100));

        mockMvc.perform(get("/api/admin/lesson-queries/course/{courseId}", 90L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(100));

        mockMvc.perform(get("/api/public/lesson-tree/course/{courseId}", 90L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(100));

        mockMvc.perform(get("/api/learner/enrollments")
                        .principal(authPrincipal("learner@example.com")))
                .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").isArray());
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

    private static HierarchyNode node(Long id, String slug) {
        HierarchyNode node = new HierarchyNode();
        ReflectionTestUtils.setField(node, "id", id);
        node.setSlug(slug);
        node.setNameEn(slug + "-name");
        node.setDescriptionEn("desc");
        node.setSortOrder(1);
        node.setVisible(true);
        node.setPublished(true);
        node.setCreatedBy("test");
        return node;
    }

    private static Lesson lesson(Long id, int orderIndex, int depth, Lesson parent) {
        Lesson lesson = new Lesson();
        ReflectionTestUtils.setField(lesson, "id", id);
        lesson.setOrderIndex(orderIndex);
        lesson.setDepthLevel(depth);
        lesson.setType(LessonType.TEXT);
        lesson.setPath("/course/1/lesson/" + id);
        lesson.setParentLesson(parent);
        return lesson;
    }
}


