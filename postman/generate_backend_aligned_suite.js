const fs = require("fs");
const path = require("path");

const SCHEMA = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";
const COLLECTION_FILE = path.join(__dirname, "Educator_MVP_Backend_Aligned.postman_collection.json");
const ENV_FILE = path.join(__dirname, "Educator_MVP_Backend_Aligned.postman_environment.json");

const RESPONSE_TIME_TEST =
  "pm.test('Response time is acceptable', function () { pm.expect(pm.response.responseTime).to.be.below(5000); });";

function statusTest(code, name = `Status code is ${code}`) {
  return `pm.test('${name}', function () { pm.response.to.have.status(${code}); });`;
}

function testEvent(lines) {
  return {
    listen: "test",
    script: {
      type: "text/javascript",
      exec: lines,
    },
  };
}

function preRequestEvent(lines) {
  return {
    listen: "prerequest",
    script: {
      type: "text/javascript",
      exec: lines,
    },
  };
}

function requestItem({
  name,
  method,
  url,
  description,
  headers = [],
  bodyRaw,
  preRequest = [],
  tests = [],
  auth,
}) {
  const item = {
    name,
    request: {
      method,
      header: headers,
      url,
      description,
    },
    response: [],
  };

  if (bodyRaw !== undefined) {
    item.request.body = {
      mode: "raw",
      raw: bodyRaw,
      options: {
        raw: {
          language: "json",
        },
      },
    };
  }

  if (auth) {
    item.request.auth = auth;
  }

  const events = [];
  if (preRequest.length) events.push(preRequestEvent(preRequest));
  if (tests.length) events.push(testEvent(tests));
  if (events.length) item.event = events;

  return item;
}

function folder(name, items, preRequest = []) {
  const node = { name, item: items };
  if (preRequest.length) node.event = [preRequestEvent(preRequest)];
  return node;
}

const ADMIN_PRE_REQUEST = [
  "const token = pm.environment.get('admin_token');",
  "if (!token) { throw new Error('Missing admin_token. Run Login Bootstrap Admin first.'); }",
  "pm.environment.set('jwt_token', token);",
];

const STUDENT_PRE_REQUEST = [
  "const token = pm.environment.get('student_token');",
  "if (!token) { throw new Error('Missing student_token. Run Login Test Student first.'); }",
  "pm.environment.set('jwt_token', token);",
  "if (!pm.environment.get('test_student_uuid')) {",
  "  pm.environment.set('test_student_uuid', pm.variables.replaceIn('{{$guid}}'));",
  "}",
];

const JSON_HEADERS = [{ key: "Content-Type", value: "application/json" }];

const collection = {
  info: {
    _postman_id: "d8f29d99-9a7f-4ab4-b9b9-9f05d662d9fb",
    name: "Educator MVP - Backend Aligned API Suite",
    description:
      "Backend-aligned Newman suite that validates every currently implemented REST endpoint in the Educator MVP backend.",
    schema: SCHEMA,
  },
  auth: {
    type: "bearer",
    bearer: [
      {
        key: "token",
        value: "{{jwt_token}}",
        type: "string",
      },
    ],
  },
  item: [
    folder("01. Auth Bootstrap", [
      requestItem({
        name: "Register Test Student",
        method: "POST",
        url: "{{base_url}}/api/auth/register",
        description: "Creates a unique student account for this run.",
        headers: JSON_HEADERS,
        bodyRaw: JSON.stringify(
          {
            email: "{{test_student_email}}",
            password: "{{test_student_password}}",
          },
          null,
          2
        ),
        auth: { type: "noauth" },
        preRequest: [
          "const runId = Date.now().toString();",
          "pm.environment.set('run_id', runId);",
          "pm.environment.set('test_student_email', `student_${runId}@test.com`);",
          "pm.environment.set('test_student_password', 'TestPass123!');",
          "pm.environment.set('test_student_uuid', pm.variables.replaceIn('{{$guid}}'));",
          "['course_id','lesson_text_id','lesson_video_id','lesson_doc_id','exam_id','attempt_id','enrollment_id','hierarchy_root_id_a','hierarchy_root_id_b','hierarchy_child_id','homepage_section_id'].forEach(k => pm.environment.unset(k));",
        ],
        tests: [
          RESPONSE_TIME_TEST,
          statusTest(200),
          "pm.test('Registration successful message returned', function () {",
          "  pm.expect(pm.response.text()).to.include('User registered successfully');",
          "});",
        ],
      }),
      requestItem({
        name: "Login Test Student",
        method: "POST",
        url: "{{base_url}}/api/auth/login",
        description: "Logs in the generated student and saves student JWT.",
        headers: JSON_HEADERS,
        bodyRaw: JSON.stringify(
          {
            email: "{{test_student_email}}",
            password: "{{test_student_password}}",
          },
          null,
          2
        ),
        auth: { type: "noauth" },
        tests: [
          RESPONSE_TIME_TEST,
          statusTest(200),
          "const jsonData = pm.response.json();",
          "pm.test('Student login returns token', function () {",
          "  pm.expect(jsonData).to.have.property('token');",
          "  pm.expect(jsonData.token).to.be.a('string');",
          "  pm.expect(jsonData.token.length).to.be.greaterThan(10);",
          "});",
          "pm.environment.set('student_token', jsonData.token);",
          "pm.environment.set('jwt_token', jsonData.token);",
        ],
      }),
      requestItem({
        name: "Login Bootstrap Admin",
        method: "POST",
        url: "{{base_url}}/api/auth/login",
        description: "Logs in bootstrap admin and saves admin JWT.",
        headers: JSON_HEADERS,
        bodyRaw: JSON.stringify(
          {
            email: "{{bootstrap_admin_email}}",
            password: "{{bootstrap_admin_password}}",
          },
          null,
          2
        ),
        auth: { type: "noauth" },
        tests: [
          RESPONSE_TIME_TEST,
          statusTest(200),
          "const jsonData = pm.response.json();",
          "pm.test('Admin login returns token', function () {",
          "  pm.expect(jsonData).to.have.property('token');",
          "  pm.expect(jsonData.token).to.be.a('string');",
          "  pm.expect(jsonData.token.length).to.be.greaterThan(10);",
          "});",
          "pm.environment.set('admin_token', jsonData.token);",
          "pm.environment.set('jwt_token', jsonData.token);",
        ],
      }),
    ]),

    folder(
      "02. Hierarchy APIs",
      [
        requestItem({
          name: "Create Hierarchy Root A",
          method: "POST",
          url: "{{base_url}}/api/admin/hierarchy?slug=suite-root-a-{{run_id}}&nameEn=SuiteRootA&descriptionEn=RootA&sortOrder=0&createdBy=suite-admin",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Root A created', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.slug).to.eql('suite-root-a-' + pm.environment.get('run_id'));",
            "});",
            "pm.environment.set('hierarchy_root_id_a', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Create Hierarchy Root B",
          method: "POST",
          url: "{{base_url}}/api/admin/hierarchy?slug=suite-root-b-{{run_id}}&nameEn=SuiteRootB&descriptionEn=RootB&sortOrder=1&createdBy=suite-admin",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Root B created', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.slug).to.eql('suite-root-b-' + pm.environment.get('run_id'));",
            "});",
            "pm.environment.set('hierarchy_root_id_b', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Create Hierarchy Child",
          method: "POST",
          url: "{{base_url}}/api/admin/hierarchy?slug=suite-child-{{run_id}}&nameEn=SuiteChild&descriptionEn=ChildNode&parentId={{hierarchy_root_id_a}}&sortOrder=0",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Child created under root A', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.parent).to.be.an('object');",
            "  pm.expect(String(jsonData.parent.id)).to.eql(pm.environment.get('hierarchy_root_id_a'));",
            "});",
            "pm.environment.set('hierarchy_child_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Update Hierarchy Root A",
          method: "PUT",
          url: "{{base_url}}/api/admin/hierarchy/{{hierarchy_root_id_a}}?nameEn=SuiteRootAUpdated&descriptionEn=UpdatedRootA&sortOrder=2&isPublished=true&isVisible=true",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Root A updated', function () {",
            "  pm.expect(jsonData.nameEn).to.eql('SuiteRootAUpdated');",
            "  pm.expect(jsonData.published).to.eql(true);",
            "  pm.expect(jsonData.visible).to.eql(true);",
            "});",
          ],
        }),
        requestItem({
          name: "Move Hierarchy Child to Root B",
          method: "PUT",
          url: "{{base_url}}/api/admin/hierarchy/{{hierarchy_child_id}}/move?newParentId={{hierarchy_root_id_b}}",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Child moved under root B', function () {",
            "  pm.expect(jsonData.parent).to.be.an('object');",
            "  pm.expect(String(jsonData.parent.id)).to.eql(pm.environment.get('hierarchy_root_id_b'));",
            "});",
          ],
        }),
        requestItem({
          name: "Get Hierarchy Roots (Public)",
          method: "GET",
          url: "{{base_url}}/api/hierarchy/roots",
          auth: { type: "noauth" },
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Roots response is an array', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "});",
          ],
        }),
        requestItem({
          name: "Get Hierarchy Children (Public)",
          method: "GET",
          url: "{{base_url}}/api/hierarchy/{{hierarchy_root_id_b}}/children",
          auth: { type: "noauth" },
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Children response includes moved child', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "  const ids = jsonData.map(x => String(x.id));",
            "  pm.expect(ids).to.include(pm.environment.get('hierarchy_child_id'));",
            "});",
          ],
        }),
        requestItem({
          name: "Soft Delete Hierarchy Child",
          method: "DELETE",
          url: "{{base_url}}/api/admin/hierarchy/{{hierarchy_child_id}}",
          tests: [RESPONSE_TIME_TEST, statusTest(204)],
        }),
        requestItem({
          name: "Restore Hierarchy Child",
          method: "PUT",
          url: "{{base_url}}/api/admin/hierarchy/{{hierarchy_child_id}}/restore",
          tests: [RESPONSE_TIME_TEST, statusTest(204)],
        }),
      ],
      ADMIN_PRE_REQUEST
    ),

    folder(
      "03. Course and Lesson APIs",
      [
        requestItem({
          name: "Create Course",
          method: "POST",
          url: "{{base_url}}/api/admin/courses?hierarchyNodeId={{hierarchy_root_id_a}}&titleEn=SuiteCourse{{run_id}}&descriptionEn=SuiteCourseDesc&difficulty=BEGINNER&languageCode=en&estimatedDurationMinutes=120&createdByRole=ADMIN",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Course created in DRAFT status', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.status).to.eql('DRAFT');",
            "});",
            "pm.environment.set('course_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Publish Course",
          method: "POST",
          url: "{{base_url}}/api/admin/courses/{{course_id}}/publish",
          tests: [RESPONSE_TIME_TEST, statusTest(200)],
        }),
        requestItem({
          name: "Create Lesson TEXT",
          method: "POST",
          url: "{{base_url}}/api/admin/lessons/course/{{course_id}}?type=TEXT&orderIndex=0&textContent=WelcomeToTheCourse",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(201),
            "const jsonData = pm.response.json();",
            "pm.test('TEXT lesson created', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.type).to.eql('TEXT');",
            "});",
            "pm.environment.set('lesson_text_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Create Lesson VIDEO",
          method: "POST",
          url: "{{base_url}}/api/admin/lessons/course/{{course_id}}?type=VIDEO&orderIndex=1&videoUrl=https://example.com/video.mp4",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(201),
            "const jsonData = pm.response.json();",
            "pm.test('VIDEO lesson created', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.type).to.eql('VIDEO');",
            "});",
            "pm.environment.set('lesson_video_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Create Lesson DOCUMENT",
          method: "POST",
          url: "{{base_url}}/api/admin/lessons/course/{{course_id}}?type=DOCUMENT&orderIndex=2&documentUrl=https://example.com/document.pdf",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(201),
            "const jsonData = pm.response.json();",
            "pm.test('DOCUMENT lesson created', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.type).to.eql('DOCUMENT');",
            "});",
            "pm.environment.set('lesson_doc_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Get Lessons for Course (Admin Query)",
          method: "GET",
          url: "{{base_url}}/api/admin/lesson-queries/course/{{course_id}}",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Admin lesson query returns array', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "  pm.expect(jsonData.length).to.be.greaterThan(0);",
            "});",
          ],
        }),
        requestItem({
          name: "Get Lessons for Course (Public)",
          method: "GET",
          url: "{{base_url}}/api/public/lessons/course/{{course_id}}",
          auth: { type: "noauth" },
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Public lessons response is array', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "});",
          ],
        }),
        requestItem({
          name: "Get Lesson Tree (Public)",
          method: "GET",
          url: "{{base_url}}/api/public/lesson-tree/course/{{course_id}}",
          auth: { type: "noauth" },
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Lesson tree response is array', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "});",
            "pm.test('Tree nodes include children field', function () {",
            "  if (jsonData.length > 0) {",
            "    pm.expect(jsonData[0]).to.have.property('children');",
            "  }",
            "});",
          ],
        }),
      ],
      ADMIN_PRE_REQUEST
    ),

    folder(
      "04. Exam APIs (Admin and Instructor)",
      [
        requestItem({
          name: "Create Exam",
          method: "POST",
          url: "{{base_url}}/api/admin/exams",
          headers: JSON_HEADERS,
          bodyRaw: [
            "{",
            '  "courseId": {{course_id}},',
            '  "title": "SuiteExam{{run_id}}",',
            '  "description": "Suite exam description",',
            '  "instructions": "Answer all questions",',
            '  "rulesSummary": "No extra rules",',
            '  "passPercentage": 50,',
            '  "maxAttempts": 3,',
            '  "timeLimitMinutes": 30,',
            '  "shuffleQuestions": false,',
            '  "shuffleOptions": false',
            "}",
          ].join("\n"),
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Exam created in DRAFT status', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.status).to.eql('DRAFT');",
            "});",
            "pm.environment.set('exam_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Get Exam Details (Instructor Endpoint)",
          method: "GET",
          url: "{{base_url}}/api/instructor/exams/{{exam_id}}",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Instructor exam response has expected id', function () {",
            "  pm.expect(String(jsonData.id)).to.eql(pm.environment.get('exam_id'));",
            "});",
          ],
        }),
        requestItem({
          name: "Publish Exam",
          method: "POST",
          url: "{{base_url}}/api/admin/exams/{{exam_id}}/publish",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Exam published', function () {",
            "  pm.expect(jsonData.status).to.eql('PUBLISHED');",
            "});",
          ],
        }),
      ],
      ADMIN_PRE_REQUEST
    ),

    folder(
      "05. Student Exam APIs",
      [
        requestItem({
          name: "Start Exam Attempt",
          method: "POST",
          url: "{{base_url}}/api/student/exams/{{exam_id}}/start?userId={{test_student_uuid}}",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Attempt created in IN_PROGRESS status', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.status).to.eql('IN_PROGRESS');",
            "});",
            "pm.environment.set('attempt_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Submit Exam Attempt",
          method: "POST",
          url: "{{base_url}}/api/student/exams/attempts/{{attempt_id}}/submit",
          headers: JSON_HEADERS,
          bodyRaw: "[]",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Attempt is evaluated', function () {",
            "  pm.expect(jsonData.status).to.eql('EVALUATED');",
            "});",
          ],
        }),
      ],
      STUDENT_PRE_REQUEST
    ),

    folder(
      "06. Learner Enrollment and Progress APIs",
      [
        requestItem({
          name: "Enroll in Published Course",
          method: "POST",
          url: "{{base_url}}/api/learner/enrollments/course/{{course_id}}",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Enrollment created with ACTIVE status', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(jsonData.status).to.eql('ACTIVE');",
            "});",
            "pm.environment.set('enrollment_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "List My Enrollments",
          method: "GET",
          url: "{{base_url}}/api/learner/enrollments",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Enrollment list response is array', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "});",
            "pm.test('Enrollment list includes current enrollment', function () {",
            "  const ids = jsonData.map(x => String(x.id));",
            "  pm.expect(ids).to.include(pm.environment.get('enrollment_id'));",
            "});",
          ],
        }),
        requestItem({
          name: "Start Enrollment Progress",
          method: "POST",
          url: "{{base_url}}/api/learner/progress/enrollment/{{enrollment_id}}/start",
          tests: [RESPONSE_TIME_TEST, statusTest(200)],
        }),
        requestItem({
          name: "Complete Lesson",
          method: "POST",
          url: "{{base_url}}/api/learner/progress/enrollment/{{enrollment_id}}/lesson/{{lesson_text_id}}/complete",
          tests: [RESPONSE_TIME_TEST, statusTest(200)],
        }),
        requestItem({
          name: "Drop Enrollment",
          method: "DELETE",
          url: "{{base_url}}/api/learner/enrollments/{{enrollment_id}}",
          tests: [RESPONSE_TIME_TEST, statusTest(204)],
        }),
      ],
      STUDENT_PRE_REQUEST
    ),

    folder(
      "07. Homepage and Finalization APIs",
      [
        requestItem({
          name: "Create Homepage Section",
          method: "POST",
          url: "{{base_url}}/api/admin/homepage/sections",
          headers: JSON_HEADERS,
          bodyRaw: [
            "{",
            '  "title": "SuiteSection{{run_id}}",',
            '  "position": "TOP",',
            '  "orderIndex": 0,',
            '  "enabled": true',
            "}",
          ].join("\n"),
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Homepage section created', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "});",
            "pm.environment.set('homepage_section_id', String(jsonData.id));",
          ],
        }),
        requestItem({
          name: "Create Homepage Block",
          method: "POST",
          url: "{{base_url}}/api/admin/homepage/blocks",
          headers: JSON_HEADERS,
          bodyRaw: [
            "{",
            '  "sectionId": "{{homepage_section_id}}",',
            '  "blockType": "TEXT",',
            '  "orderIndex": 0,',
            '  "enabled": true',
            "}",
          ].join("\n"),
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Homepage block created for section', function () {",
            "  pm.expect(jsonData).to.have.property('id');",
            "  pm.expect(String(jsonData.sectionId)).to.eql(pm.environment.get('homepage_section_id'));",
            "});",
          ],
        }),
        requestItem({
          name: "Get Homepage (Public)",
          method: "GET",
          url: "{{base_url}}/api/public/homepage",
          auth: { type: "noauth" },
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Homepage response is array', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "});",
          ],
        }),
        requestItem({
          name: "Get Active Courses",
          method: "GET",
          url: "{{base_url}}/api/admin/courses/active",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Active courses response is array', function () {",
            "  pm.expect(jsonData).to.be.an('array');",
            "});",
          ],
        }),
        requestItem({
          name: "Archive Exam",
          method: "POST",
          url: "{{base_url}}/api/admin/exams/{{exam_id}}/archive",
          tests: [
            RESPONSE_TIME_TEST,
            statusTest(200),
            "const jsonData = pm.response.json();",
            "pm.test('Exam archived', function () {",
            "  pm.expect(jsonData.status).to.eql('ARCHIVED');",
            "});",
          ],
        }),
        requestItem({
          name: "Archive Course",
          method: "POST",
          url: "{{base_url}}/api/admin/courses/{{course_id}}/archive",
          tests: [RESPONSE_TIME_TEST, statusTest(200)],
        }),
        requestItem({
          name: "Delete Lesson (Soft)",
          method: "DELETE",
          url: "{{base_url}}/api/admin/lessons/{{lesson_doc_id}}",
          tests: [RESPONSE_TIME_TEST, statusTest(204)],
        }),
        requestItem({
          name: "Delete Course (Soft)",
          method: "DELETE",
          url: "{{base_url}}/api/admin/courses/{{course_id}}",
          tests: [RESPONSE_TIME_TEST, statusTest(200)],
        }),
      ],
      ADMIN_PRE_REQUEST
    ),

    folder("08. Security Sanity Checks", [
      requestItem({
        name: "Admin Endpoint Without Token",
        method: "GET",
        url: "{{base_url}}/api/admin/courses/active",
        auth: { type: "noauth" },
        tests: [RESPONSE_TIME_TEST, statusTest(401)],
      }),
      requestItem({
        name: "Admin Endpoint With Student Token",
        method: "GET",
        url: "{{base_url}}/api/admin/courses/active",
        preRequest: [
          "const token = pm.environment.get('student_token');",
          "if (!token) { throw new Error('Missing student_token.'); }",
          "pm.environment.set('jwt_token', token);",
        ],
        tests: [
          RESPONSE_TIME_TEST,
          "pm.test('Status code is 401 or 403', function () {",
          "  pm.expect([401, 403]).to.include(pm.response.code);",
          "});",
        ],
      }),
    ]),
  ],
};

const environment = {
  id: "educator-mvp-backend-aligned-env",
  name: "Educator MVP - Backend Aligned Local",
  values: [
    { key: "base_url", value: "http://localhost:8080", type: "default", enabled: true },
    { key: "bootstrap_admin_email", value: "admin@educator.local", type: "default", enabled: true },
    { key: "bootstrap_admin_password", value: "Admin@123", type: "default", enabled: true },
    { key: "jwt_token", value: "", type: "secret", enabled: true },
    { key: "admin_token", value: "", type: "secret", enabled: true },
    { key: "student_token", value: "", type: "secret", enabled: true },
    { key: "run_id", value: "", type: "default", enabled: true },
    { key: "test_student_email", value: "", type: "default", enabled: true },
    { key: "test_student_password", value: "", type: "secret", enabled: true },
    { key: "test_student_uuid", value: "", type: "default", enabled: true },
    { key: "hierarchy_root_id_a", value: "", type: "default", enabled: true },
    { key: "hierarchy_root_id_b", value: "", type: "default", enabled: true },
    { key: "hierarchy_child_id", value: "", type: "default", enabled: true },
    { key: "course_id", value: "", type: "default", enabled: true },
    { key: "lesson_text_id", value: "", type: "default", enabled: true },
    { key: "lesson_video_id", value: "", type: "default", enabled: true },
    { key: "lesson_doc_id", value: "", type: "default", enabled: true },
    { key: "exam_id", value: "", type: "default", enabled: true },
    { key: "attempt_id", value: "", type: "default", enabled: true },
    { key: "enrollment_id", value: "", type: "default", enabled: true },
    { key: "homepage_section_id", value: "", type: "default", enabled: true },
  ],
  _postman_variable_scope: "environment",
  _postman_exported_at: "2026-02-11T00:00:00.000Z",
  _postman_exported_using: "Newman/6.2.2",
};

fs.writeFileSync(COLLECTION_FILE, JSON.stringify(collection, null, 2));
fs.writeFileSync(ENV_FILE, JSON.stringify(environment, null, 2));

console.log(`Generated:\n- ${COLLECTION_FILE}\n- ${ENV_FILE}`);
