export type RoleName = "STUDENT" | "INSTRUCTOR" | "ADMIN";

export type CourseStatus = "DRAFT" | "PUBLISHED";
export type CourseDifficulty = "BEGINNER" | "INTERMEDIATE" | "ADVANCED";

export type LessonType = "TEXT" | "VIDEO" | "DOCUMENT";

export type EnrollmentStatus = "ACTIVE" | "COMPLETED" | "DROPPED";

export type ExamStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";
export type AttemptStatus = "IN_PROGRESS" | "SUBMITTED" | "EVALUATED" | "EXPIRED";

export type SectionPosition = "TOP" | "LEFT" | "CENTER" | "RIGHT" | "BOTTOM";
export type BlockType = "COURSE" | "EXAM" | "IMAGE" | "VIDEO" | "TEXT" | "CTA";
export type LayoutType =
  | "HORIZONTAL_SCROLL"
  | "VERTICAL_LIST"
  | "GRID"
  | "CAROUSEL"
  | "BANNER_ROTATOR";
export type BlockTargetType = "COURSE" | "EXAM" | "EXTERNAL";

