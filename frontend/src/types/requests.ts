import type { BlockType, CourseDifficulty, LessonType, SectionPosition } from "@/types/enums";
import type { ExamAttemptAnswerEntity } from "@/types/entities";
import type { LongId, Uuid } from "@/types/primitives";

export interface RegisterRequest {
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface CreateHierarchyNodeRequest {
  slug: string;
  nameEn: string;
  descriptionEn?: string;
  parentId?: LongId;
  sortOrder?: number;
  createdBy?: string;
}

export interface UpdateHierarchyNodeRequest {
  nameEn: string;
  descriptionEn?: string;
  sortOrder?: number;
  isPublished: boolean;
  isVisible: boolean;
}

export interface MoveHierarchyNodeRequest {
  newParentId: LongId;
}

export interface CreateCourseRequest {
  hierarchyNodeId: LongId;
  titleEn: string;
  descriptionEn: string;
  difficulty: CourseDifficulty;
  languageCode: string;
  estimatedDurationMinutes: number;
  createdByRole: string;
}

export interface CreateLessonRequest {
  type: LessonType;
  orderIndex: number;
  textContent?: string;
  videoUrl?: string;
  documentUrl?: string;
}

export interface CreateExamRequest {
  courseId: LongId;
  title: string;
  description: string;
  instructions: string;
  rulesSummary: string;
  passPercentage: number;
  maxAttempts?: number;
  timeLimitMinutes?: number;
  shuffleQuestions: boolean;
  shuffleOptions: boolean;
}

export interface StartExamAttemptRequest {
  examId: Uuid;
  // Current backend contract uses query param userId (planned to move to principal in Sprint 8).
  userId: Uuid;
}

export interface SubmitExamAttemptRequest {
  attemptId: Uuid;
  answers: ExamAttemptAnswerEntity[];
}

export interface CreateHomepageSectionRequest {
  title: string;
  position: SectionPosition;
  orderIndex: number;
  enabled: boolean;
}

export interface CreateSectionBlockRequest {
  sectionId: Uuid;
  blockType: BlockType;
  orderIndex: number;
  enabled: boolean;
}

