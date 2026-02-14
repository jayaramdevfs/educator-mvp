import { http, HttpResponse } from "msw";

const BASE = "http://localhost:8080";

// --- Mock Data ---

const mockEnrollments = [
  {
    id: 1,
    status: "ACTIVE",
    enrolledAt: "2025-12-01T10:00:00",
    completedAt: null,
    lastAccessedAt: null,
    user: { id: 1, email: "student@test.com", roles: [] },
    course: {
      id: 101,
      titleEn: "Intro to History",
      descriptionEn: "Learn the basics of world history.",
      difficulty: "BEGINNER",
      estimatedDurationMinutes: 60,
      status: "PUBLISHED",
    },
  },
  {
    id: 2,
    status: "COMPLETED",
    enrolledAt: "2025-11-01T10:00:00",
    completedAt: "2025-11-30T10:00:00",
    lastAccessedAt: "2025-11-30T10:00:00",
    user: { id: 1, email: "student@test.com", roles: [] },
    course: {
      id: 102,
      titleEn: "Advanced Math",
      descriptionEn: "Deep dive into calculus.",
      difficulty: "ADVANCED",
      estimatedDurationMinutes: 120,
      status: "PUBLISHED",
    },
  },
];

const mockLessons = [
  {
    id: 1,
    orderIndex: 0,
    depthLevel: 0,
    type: "TEXT",
    textContent: "<h1>Welcome</h1><p>This is the first lesson.</p>",
    videoUrl: null,
    documentUrl: null,
    childLessons: [],
    deleted: false,
    createdAt: "2025-12-01T00:00:00",
    updatedAt: "2025-12-01T00:00:00",
    course: { id: 101 },
  },
  {
    id: 2,
    orderIndex: 1,
    depthLevel: 0,
    type: "VIDEO",
    textContent: null,
    videoUrl: "https://www.youtube.com/embed/test",
    documentUrl: null,
    childLessons: [],
    deleted: false,
    createdAt: "2025-12-01T00:00:00",
    updatedAt: "2025-12-01T00:00:00",
    course: { id: 101 },
  },
  {
    id: 3,
    orderIndex: 2,
    depthLevel: 0,
    type: "DOCUMENT",
    textContent: null,
    videoUrl: null,
    documentUrl: "https://example.com/doc.pdf",
    childLessons: [],
    deleted: false,
    createdAt: "2025-12-01T00:00:00",
    updatedAt: "2025-12-01T00:00:00",
    course: { id: 101 },
  },
];

const mockNotifications = [
  {
    id: "n1",
    userId: "u1",
    type: "COURSE_COMPLETED",
    title: "Course Completed",
    message: "You completed Intro to History.",
    status: "PERSISTED",
    read: false,
    createdAt: "2025-12-15T10:00:00Z",
  },
  {
    id: "n2",
    userId: "u1",
    type: "EXAM_PASSED",
    title: "Exam Passed",
    message: "Congratulations! You passed the final exam.",
    status: "PERSISTED",
    read: true,
    createdAt: "2025-12-14T10:00:00Z",
  },
];

const mockCertificates = [
  {
    id: "c1",
    courseId: 102,
    userId: "u1",
    courseCompletionId: "cc1",
    status: "ISSUED",
    issuedAt: "2025-12-01T10:00:00",
    revokedAt: null,
    expiredAt: null,
    createdAt: "2025-11-30T10:00:00",
  },
];

const mockExamAttempt = {
  id: "a1",
  examId: "e1",
  userId: "u1",
  status: "EVALUATED",
  totalQuestions: 10,
  correctAnswers: 8,
  scorePercentage: 80.0,
  passed: true,
  startedAt: "2025-12-15T10:00:00Z",
  submittedAt: "2025-12-15T10:30:00Z",
  evaluatedAt: "2025-12-15T10:30:01Z",
};

// --- Handlers ---

export const handlers = [
  // Auth
  http.post(`${BASE}/api/auth/login`, async () => {
    return HttpResponse.json({
      token:
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZXJAZWR1Y2F0b3IubG9jYWwiLCJyb2xlcyI6WyJBRE1JTiJdLCJpYXQiOjE3Nzc3NzAwMDAsImV4cCI6MTc3Nzc3MzYwMH0.signature",
    });
  }),

  http.get(`${BASE}/api/test/ping`, () => {
    return HttpResponse.json({ ok: true, source: "msw" });
  }),

  // Learner Enrollments
  http.get(`${BASE}/api/learner/enrollments`, () => {
    return HttpResponse.json({
      content: mockEnrollments,
      pageNumber: 0,
      pageSize: 100,
      totalElements: mockEnrollments.length,
      totalPages: 1,
      last: true,
    });
  }),

  http.post(`${BASE}/api/learner/enrollments/course/:courseId`, () => {
    return HttpResponse.json({
      id: 99,
      status: "ACTIVE",
      enrolledAt: new Date().toISOString(),
      completedAt: null,
      lastAccessedAt: null,
      course: { id: 101 },
      user: { id: 1, email: "student@test.com" },
    });
  }),

  http.delete(`${BASE}/api/learner/enrollments/:id`, () => {
    return new HttpResponse(null, { status: 204 });
  }),

  // Learner Progress
  http.post(`${BASE}/api/learner/progress/enrollment/:enrollmentId/start`, () => {
    return new HttpResponse(null, { status: 204 });
  }),

  http.post(
    `${BASE}/api/learner/progress/enrollment/:enrollmentId/lesson/:lessonId/complete`,
    () => {
      return new HttpResponse(null, { status: 204 });
    },
  ),

  // Lessons
  http.get(`${BASE}/api/public/lessons/course/:courseId`, () => {
    return HttpResponse.json(mockLessons);
  }),

  // Course detail
  http.get(`${BASE}/api/public/courses/:courseId`, ({ params }) => {
    const course = mockEnrollments.find((e) => String(e.course.id) === params.courseId)?.course;
    if (!course) {
      return HttpResponse.json(
        { id: Number(params.courseId), titleEn: "Test Course", descriptionEn: "Desc", difficulty: "BEGINNER", estimatedDurationMinutes: 60, status: "PUBLISHED" },
      );
    }
    return HttpResponse.json(course);
  }),

  // Notifications
  http.get(`${BASE}/api/learner/notifications`, () => {
    return HttpResponse.json({
      content: mockNotifications,
      pageNumber: 0,
      pageSize: 50,
      totalElements: mockNotifications.length,
      totalPages: 1,
      last: true,
    });
  }),

  http.get(`${BASE}/api/learner/notifications/unread-count`, () => {
    return HttpResponse.json({ unreadCount: 1 });
  }),

  http.put(`${BASE}/api/learner/notifications/:id/read`, () => {
    return HttpResponse.json({ ...mockNotifications[0], read: true });
  }),

  // Certificates
  http.get(`${BASE}/api/learner/certificates`, () => {
    return HttpResponse.json({
      content: mockCertificates,
      pageNumber: 0,
      pageSize: 50,
      totalElements: mockCertificates.length,
      totalPages: 1,
      last: true,
    });
  }),

  // Exams
  http.get(`${BASE}/api/public/exams/course/:courseId`, () => {
    return HttpResponse.json([
      {
        id: "e1",
        courseId: 101,
        title: "Final Exam",
        description: "Test your knowledge",
        instructions: "Answer all questions",
        rulesSummary: "No cheating",
        passPercentage: 70,
        maxAttempts: 3,
        timeLimitMinutes: 30,
        shuffleQuestions: true,
        shuffleOptions: false,
        status: "PUBLISHED",
      },
    ]);
  }),

  http.get(`${BASE}/api/student/exams/:examId/attempts`, () => {
    return HttpResponse.json([mockExamAttempt]);
  }),

  http.post(`${BASE}/api/student/exams/:examId/start`, () => {
    return HttpResponse.json({
      id: "a2",
      examId: "e1",
      userId: "u1",
      status: "IN_PROGRESS",
      totalQuestions: null,
      correctAnswers: null,
      scorePercentage: null,
      passed: null,
      startedAt: new Date().toISOString(),
      submittedAt: null,
      evaluatedAt: null,
      questions: [
        {
          id: "q1",
          questionText: "What is 2+2?",
          options: [
            { id: "o1", optionText: "3" },
            { id: "o2", optionText: "4" },
            { id: "o3", optionText: "5" },
          ],
        },
        {
          id: "q2",
          questionText: "Capital of France?",
          options: [
            { id: "o4", optionText: "London" },
            { id: "o5", optionText: "Paris" },
            { id: "o6", optionText: "Berlin" },
          ],
        },
      ],
    });
  }),

  http.post(`${BASE}/api/student/exams/attempts/:attemptId/submit`, () => {
    return HttpResponse.json(mockExamAttempt);
  }),

  // Profile / Password (may not exist in backend yet)
  http.put(`${BASE}/api/learner/profile`, () => {
    return HttpResponse.json({ success: true });
  }),

  http.put(`${BASE}/api/learner/password`, () => {
    return HttpResponse.json({ success: true });
  }),

  // Password reset
  http.post(`${BASE}/api/auth/forgot-password`, () => {
    return HttpResponse.json({ success: true });
  }),

  http.post(`${BASE}/api/auth/reset-password`, () => {
    return HttpResponse.json({ success: true });
  }),
];
