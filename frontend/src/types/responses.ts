import type {
  CourseEntity,
  EnrollmentEntity,
  ExamAttemptEntity,
  ExamEntity,
  HomepageResponseEntity,
  HomepageSectionEntity,
  HierarchyNodeEntity,
  JwtResponse,
  LessonEntity,
  LessonTreeNode,
  SectionBlockEntity,
} from "@/types/entities";
import type { LongId } from "@/types/primitives";

export type RegisterResponse = string;
export type LoginResponse = JwtResponse;

export type CreateHierarchyNodeResponse = HierarchyNodeEntity;
export type UpdateHierarchyNodeResponse = HierarchyNodeEntity;
export type MoveHierarchyNodeResponse = HierarchyNodeEntity;
export type GetHierarchyRootsResponse = HierarchyNodeEntity[];
export type GetHierarchyChildrenResponse = HierarchyNodeEntity[];
export type SoftDeleteHierarchyNodeResponse = void;
export type RestoreHierarchyNodeResponse = void;

export type CreateCourseResponse = CourseEntity;
export type PublishCourseResponse = void;
export type ArchiveCourseResponse = void;
export type DeleteCourseResponse = void;
export type GetActiveCoursesResponse = CourseEntity[];

export type CreateLessonResponse = LessonEntity;
export type DeleteLessonResponse = void;
export type GetAdminLessonsByCourseResponse = LessonEntity[];
export type GetPublicLessonsByCourseResponse = LessonEntity[];
export type GetPublicLessonTreeResponse = LessonTreeNode[];

export type CreateExamResponse = ExamEntity;
export type GetInstructorExamResponse = ExamEntity;
export type PublishExamResponse = ExamEntity;
export type ArchiveExamResponse = ExamEntity;

export type StartExamAttemptResponse = ExamAttemptEntity;
export type SubmitExamAttemptResponse = ExamAttemptEntity;

export type EnrollInCourseResponse = EnrollmentEntity;
export type GetMyEnrollmentsResponse = EnrollmentEntity[];
export type StartEnrollmentProgressResponse = void;
export type CompleteLessonProgressResponse = void;
export type DropEnrollmentResponse = void;

export type CreateHomepageSectionResponse = HomepageSectionEntity;
export type CreateHomepageBlockResponse = SectionBlockEntity;
export type GetHomepageResponse = HomepageResponseEntity[];

export interface EndpointPathParams {
  courseId: LongId;
  lessonId: LongId;
  enrollmentId: LongId;
  hierarchyId: LongId;
}

