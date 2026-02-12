import type {
  AttemptStatus,
  BlockType,
  CourseDifficulty,
  CourseStatus,
  EnrollmentStatus,
  ExamStatus,
  LessonType,
  RoleName,
  SectionPosition,
} from "@/types/enums";
import type { IsoDateTime, LongId, Nullable, Uuid } from "@/types/primitives";

export interface RoleEntity {
  id: LongId;
  name: RoleName;
}

export interface UserEntity {
  id: LongId;
  email: string;
  // Backend currently serializes password in some nested responses.
  password?: string;
  roles: RoleEntity[];
}

export interface HierarchyNodeEntity {
  id: LongId;
  slug: string;
  nameEn: string;
  descriptionEn: Nullable<string>;
  sortOrder: number;
  version: number;
  published: boolean;
  visible: boolean;
  deleted: boolean;
  createdAt: IsoDateTime;
  updatedAt: IsoDateTime;
  createdBy: Nullable<string>;
  parent: Nullable<HierarchyNodeEntity>;
  children: HierarchyNodeEntity[];
}

export interface CourseEntity {
  id: LongId;
  hierarchyNode: HierarchyNodeEntity;
  titleEn: string;
  descriptionEn: string;
  status: CourseStatus;
  difficulty: CourseDifficulty;
  languageCode: string;
  estimatedDurationMinutes: number;
  createdByRole: string;
  archived: boolean;
  deleted: boolean;
  sortOrder: number;
  version: number;
  createdAt: IsoDateTime;
  updatedAt: IsoDateTime;
}

export interface LessonEntity {
  id: LongId;
  course: CourseEntity;
  parentLesson: Nullable<LessonEntity>;
  childLessons: LessonEntity[];
  path: string;
  depthLevel: number;
  type: LessonType;
  orderIndex: number;
  textContent: Nullable<string>;
  videoUrl: Nullable<string>;
  documentUrl: Nullable<string>;
  deleted: boolean;
  createdAt: IsoDateTime;
  updatedAt: IsoDateTime;
}

export interface LessonTreeNode {
  id: LongId;
  orderIndex: number;
  depthLevel: number;
  children: LessonTreeNode[];
}

export interface ExamEntity {
  id: Uuid;
  courseId: LongId;
  title: string;
  description: string;
  instructions: string;
  rulesSummary: string;
  passPercentage: number;
  maxAttempts: Nullable<number>;
  timeLimitMinutes: Nullable<number>;
  shuffleQuestions: boolean;
  shuffleOptions: boolean;
  status: ExamStatus;
  createdAt: IsoDateTime;
  updatedAt: IsoDateTime;
}

export interface ExamAttemptEntity {
  id: Uuid;
  examId: Uuid;
  userId: Uuid;
  status: AttemptStatus;
  totalQuestions: Nullable<number>;
  correctAnswers: Nullable<number>;
  scorePercentage: Nullable<number>;
  passed: Nullable<boolean>;
  startedAt: IsoDateTime;
  submittedAt: Nullable<IsoDateTime>;
  evaluatedAt: Nullable<IsoDateTime>;
}

export interface ExamAttemptAnswerEntity {
  id?: Uuid;
  attemptId?: Uuid;
  questionId: Uuid;
  selectedOptionId: Uuid;
}

export interface EnrollmentEntity {
  id: LongId;
  user: UserEntity;
  course: CourseEntity;
  status: EnrollmentStatus;
  enrolledAt: IsoDateTime;
  completedAt: Nullable<IsoDateTime>;
  lastAccessedAt: Nullable<IsoDateTime>;
}

export interface HomepageSectionEntity {
  id: Uuid;
  title: string;
  position: SectionPosition;
  orderIndex: number;
  enabled: boolean;
}

export interface SectionBlockEntity {
  id: Uuid;
  sectionId: Uuid;
  blockType: BlockType;
  orderIndex: number;
  enabled: boolean;
}

export interface HomepageResponseEntity {
  section: HomepageSectionEntity;
  blocks: SectionBlockEntity[];
}

export interface JwtResponse {
  token: string;
}

export interface ApiError {
  timestamp: IsoDateTime;
  status: number;
  error: string;
  code: string;
  message: string;
  path: string;
}

