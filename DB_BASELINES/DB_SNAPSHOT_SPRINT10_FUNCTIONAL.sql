--
-- PostgreSQL database dump
--

\restrict Sg7n5v1HVohUOwDdIEmJlceZwPPcOkzkRSX37OLYSbyyCRapAnEkbYzydRZjF0n

-- Dumped from database version 15.15
-- Dumped by pg_dump version 15.15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: automation_event_logs; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.automation_event_logs (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    event_payload text,
    reference_id uuid NOT NULL,
    trigger_type character varying(255) NOT NULL,
    CONSTRAINT automation_event_logs_trigger_type_check CHECK (((trigger_type)::text = ANY ((ARRAY['COURSE_COMPLETED'::character varying, 'EXAM_PASSED'::character varying, 'CERTIFICATE_GENERATED'::character varying, 'SUBSCRIPTION_ACTIVATED'::character varying, 'SUBSCRIPTION_EXPIRED'::character varying])::text[])))
);


ALTER TABLE public.automation_event_logs OWNER TO educator;

--
-- Name: automation_rules; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.automation_rules (
    id uuid NOT NULL,
    action_payload text NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    enabled boolean NOT NULL,
    trigger_type character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone,
    CONSTRAINT automation_rules_trigger_type_check CHECK (((trigger_type)::text = ANY ((ARRAY['COURSE_COMPLETED'::character varying, 'EXAM_PASSED'::character varying, 'CERTIFICATE_GENERATED'::character varying, 'SUBSCRIPTION_ACTIVATED'::character varying, 'SUBSCRIPTION_EXPIRED'::character varying])::text[])))
);


ALTER TABLE public.automation_rules OWNER TO educator;

--
-- Name: block_configs; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.block_configs (
    id uuid NOT NULL,
    block_id uuid,
    clickable boolean NOT NULL,
    image_url character varying(255),
    subtitle character varying(255),
    target_id uuid,
    target_type character varying(255),
    target_url character varying(255),
    title character varying(255),
    video_url character varying(255),
    CONSTRAINT block_configs_target_type_check CHECK (((target_type)::text = ANY ((ARRAY['COURSE'::character varying, 'EXAM'::character varying, 'EXTERNAL'::character varying])::text[])))
);


ALTER TABLE public.block_configs OWNER TO educator;

--
-- Name: certificates; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.certificates (
    id uuid NOT NULL,
    course_completion_id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    expired_at timestamp(6) without time zone,
    issued_at timestamp(6) without time zone,
    revoked_at timestamp(6) without time zone,
    status character varying(255) NOT NULL,
    user_id uuid NOT NULL,
    course_id bigint NOT NULL,
    CONSTRAINT certificates_status_check CHECK (((status)::text = ANY ((ARRAY['GENERATED'::character varying, 'ISSUED'::character varying, 'REVOKED'::character varying, 'EXPIRED'::character varying])::text[])))
);


ALTER TABLE public.certificates OWNER TO educator;

--
-- Name: course_completions; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.course_completions (
    id uuid NOT NULL,
    completed_at timestamp(6) without time zone NOT NULL,
    course_id bigint NOT NULL,
    exam_attempt_id uuid,
    user_id uuid NOT NULL
);


ALTER TABLE public.course_completions OWNER TO educator;

--
-- Name: courses; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.courses (
    id bigint NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by_role character varying(50) NOT NULL,
    description_en text,
    difficulty character varying(20) NOT NULL,
    estimated_duration_minutes integer NOT NULL,
    is_archived boolean NOT NULL,
    is_deleted boolean NOT NULL,
    language_code character varying(10) NOT NULL,
    sort_order integer NOT NULL,
    status character varying(20) NOT NULL,
    title_en character varying(200) NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    version bigint,
    hierarchy_node_id bigint NOT NULL,
    CONSTRAINT courses_difficulty_check CHECK (((difficulty)::text = ANY ((ARRAY['BEGINNER'::character varying, 'INTERMEDIATE'::character varying, 'ADVANCED'::character varying])::text[]))),
    CONSTRAINT courses_status_check CHECK (((status)::text = ANY ((ARRAY['DRAFT'::character varying, 'PUBLISHED'::character varying])::text[])))
);


ALTER TABLE public.courses OWNER TO educator;

--
-- Name: courses_id_seq; Type: SEQUENCE; Schema: public; Owner: educator
--

ALTER TABLE public.courses ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.courses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: enrollments; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.enrollments (
    id bigint NOT NULL,
    completed_at timestamp(6) without time zone,
    enrolled_at timestamp(6) without time zone NOT NULL,
    last_accessed_at timestamp(6) without time zone,
    status character varying(20) NOT NULL,
    course_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT enrollments_status_check CHECK (((status)::text = ANY ((ARRAY['ACTIVE'::character varying, 'COMPLETED'::character varying, 'DROPPED'::character varying])::text[])))
);


ALTER TABLE public.enrollments OWNER TO educator;

--
-- Name: enrollments_id_seq; Type: SEQUENCE; Schema: public; Owner: educator
--

ALTER TABLE public.enrollments ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.enrollments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: exam_attempt_answers; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.exam_attempt_answers (
    id uuid NOT NULL,
    attempt_id uuid NOT NULL,
    question_id uuid NOT NULL,
    selected_option_id uuid NOT NULL
);


ALTER TABLE public.exam_attempt_answers OWNER TO educator;

--
-- Name: exam_attempts; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.exam_attempts (
    id uuid NOT NULL,
    correct_answers integer,
    evaluated_at timestamp(6) without time zone,
    exam_id uuid NOT NULL,
    passed boolean,
    score_percentage integer,
    started_at timestamp(6) without time zone NOT NULL,
    status character varying(255) NOT NULL,
    submitted_at timestamp(6) without time zone,
    total_questions integer,
    user_id uuid NOT NULL,
    question_order character varying(4000),
    CONSTRAINT exam_attempts_status_check CHECK (((status)::text = ANY ((ARRAY['IN_PROGRESS'::character varying, 'SUBMITTED'::character varying, 'EVALUATED'::character varying, 'EXPIRED'::character varying])::text[])))
);


ALTER TABLE public.exam_attempts OWNER TO educator;

--
-- Name: exam_options; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.exam_options (
    id uuid NOT NULL,
    is_correct boolean NOT NULL,
    display_order integer NOT NULL,
    option_text character varying(2000) NOT NULL,
    question_id uuid NOT NULL
);


ALTER TABLE public.exam_options OWNER TO educator;

--
-- Name: exam_questions; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.exam_questions (
    id uuid NOT NULL,
    display_order integer NOT NULL,
    exam_id uuid NOT NULL,
    explanation character varying(4000),
    question_text character varying(4000) NOT NULL
);


ALTER TABLE public.exam_questions OWNER TO educator;

--
-- Name: exams; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.exams (
    id uuid NOT NULL,
    course_id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(2000),
    instructions character varying(4000),
    max_attempts integer,
    pass_percentage integer NOT NULL,
    rules_summary character varying(2000),
    shuffle_options boolean NOT NULL,
    shuffle_questions boolean NOT NULL,
    status character varying(255) NOT NULL,
    time_limit_minutes integer,
    title character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone,
    CONSTRAINT exams_status_check CHECK (((status)::text = ANY ((ARRAY['DRAFT'::character varying, 'PUBLISHED'::character varying, 'ARCHIVED'::character varying])::text[])))
);


ALTER TABLE public.exams OWNER TO educator;

--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO educator;

--
-- Name: hierarchy_nodes; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.hierarchy_nodes (
    id bigint NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by character varying(150),
    description_en text,
    is_deleted boolean NOT NULL,
    is_published boolean NOT NULL,
    is_visible boolean NOT NULL,
    name_en character varying(255) NOT NULL,
    slug character varying(150) NOT NULL,
    sort_order integer NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    version bigint,
    parent_id bigint
);


ALTER TABLE public.hierarchy_nodes OWNER TO educator;

--
-- Name: hierarchy_nodes_id_seq; Type: SEQUENCE; Schema: public; Owner: educator
--

ALTER TABLE public.hierarchy_nodes ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.hierarchy_nodes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: homepage_sections; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.homepage_sections (
    id uuid NOT NULL,
    enabled boolean NOT NULL,
    order_index integer NOT NULL,
    "position" character varying(255),
    title character varying(255),
    CONSTRAINT homepage_sections_position_check CHECK ((("position")::text = ANY ((ARRAY['TOP'::character varying, 'LEFT'::character varying, 'CENTER'::character varying, 'RIGHT'::character varying, 'BOTTOM'::character varying])::text[])))
);


ALTER TABLE public.homepage_sections OWNER TO educator;

--
-- Name: lesson_progress; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.lesson_progress (
    id bigint NOT NULL,
    completed boolean NOT NULL,
    completed_at timestamp(6) without time zone,
    enrollment_id bigint NOT NULL,
    lesson_id bigint NOT NULL
);


ALTER TABLE public.lesson_progress OWNER TO educator;

--
-- Name: lesson_progress_id_seq; Type: SEQUENCE; Schema: public; Owner: educator
--

ALTER TABLE public.lesson_progress ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.lesson_progress_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: lessons; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.lessons (
    id bigint NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    depth_level integer NOT NULL,
    document_url character varying(255),
    is_deleted boolean NOT NULL,
    order_index integer NOT NULL,
    path character varying(512) NOT NULL,
    text_content text,
    type character varying(255) NOT NULL,
    updated_at timestamp(6) with time zone,
    video_url character varying(255),
    course_id bigint NOT NULL,
    parent_lesson_id bigint,
    CONSTRAINT lessons_type_check CHECK (((type)::text = ANY ((ARRAY['TEXT'::character varying, 'VIDEO'::character varying, 'DOCUMENT'::character varying])::text[])))
);


ALTER TABLE public.lessons OWNER TO educator;

--
-- Name: lessons_id_seq; Type: SEQUENCE; Schema: public; Owner: educator
--

ALTER TABLE public.lessons ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.lessons_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: media_references; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.media_references (
    id uuid NOT NULL,
    active boolean NOT NULL,
    duration_seconds integer,
    lesson_id uuid,
    media_type character varying(255),
    source_type character varying(255),
    source_url character varying(255),
    title character varying(255),
    CONSTRAINT media_references_media_type_check CHECK (((media_type)::text = ANY ((ARRAY['VIDEO'::character varying, 'PDF'::character varying])::text[]))),
    CONSTRAINT media_references_source_type_check CHECK (((source_type)::text = ANY ((ARRAY['EXTERNAL'::character varying, 'INTERNAL'::character varying])::text[])))
);


ALTER TABLE public.media_references OWNER TO educator;

--
-- Name: notifications; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.notifications (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone,
    message character varying(1000),
    read boolean NOT NULL,
    status character varying(255),
    title character varying(255),
    type character varying(255),
    user_id uuid,
    CONSTRAINT notifications_status_check CHECK (((status)::text = ANY ((ARRAY['PERSISTED'::character varying, 'ACKNOWLEDGED'::character varying])::text[]))),
    CONSTRAINT notifications_type_check CHECK (((type)::text = ANY ((ARRAY['COURSE_COMPLETED'::character varying, 'EXAM_PASSED'::character varying, 'EXAM_FAILED'::character varying, 'CERTIFICATE_ELIGIBLE'::character varying])::text[])))
);


ALTER TABLE public.notifications OWNER TO educator;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO educator;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: educator
--

ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: section_blocks; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.section_blocks (
    id uuid NOT NULL,
    block_type character varying(255),
    enabled boolean NOT NULL,
    order_index integer NOT NULL,
    section_id uuid,
    CONSTRAINT section_blocks_block_type_check CHECK (((block_type)::text = ANY ((ARRAY['COURSE'::character varying, 'EXAM'::character varying, 'IMAGE'::character varying, 'VIDEO'::character varying, 'TEXT'::character varying, 'CTA'::character varying])::text[])))
);


ALTER TABLE public.section_blocks OWNER TO educator;

--
-- Name: section_layouts; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.section_layouts (
    id uuid NOT NULL,
    auto_scroll boolean NOT NULL,
    layout_type character varying(255),
    scroll_interval_seconds integer,
    section_id uuid,
    CONSTRAINT section_layouts_layout_type_check CHECK (((layout_type)::text = ANY ((ARRAY['HORIZONTAL_SCROLL'::character varying, 'VERTICAL_LIST'::character varying, 'GRID'::character varying, 'CAROUSEL'::character varying, 'BANNER_ROTATOR'::character varying])::text[])))
);


ALTER TABLE public.section_layouts OWNER TO educator;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_roles OWNER TO educator;

--
-- Name: users; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO educator;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: educator
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: automation_event_logs; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.automation_event_logs (id, created_at, event_payload, reference_id, trigger_type) FROM stdin;
\.


--
-- Data for Name: automation_rules; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.automation_rules (id, action_payload, created_at, enabled, trigger_type, updated_at) FROM stdin;
\.


--
-- Data for Name: block_configs; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.block_configs (id, block_id, clickable, image_url, subtitle, target_id, target_type, target_url, title, video_url) FROM stdin;
\.


--
-- Data for Name: certificates; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.certificates (id, course_completion_id, created_at, expired_at, issued_at, revoked_at, status, user_id, course_id) FROM stdin;
\.


--
-- Data for Name: course_completions; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.course_completions (id, completed_at, course_id, exam_attempt_id, user_id) FROM stdin;
\.


--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.courses (id, created_at, created_by_role, description_en, difficulty, estimated_duration_minutes, is_archived, is_deleted, language_code, sort_order, status, title_en, updated_at, version, hierarchy_node_id) FROM stdin;
15	2026-02-12 01:07:40.471508+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770858458635	2026-02-12 01:07:43.285043+00	3	46
1	2026-02-11 22:18:22.347993+00	ADMIN	CourseDesc	BEGINNER	60	t	t	en	0	PUBLISHED	SuiteCourse-1770848300	2026-02-11 22:18:23.714539+00	3	14
2	2026-02-11 22:21:19.930116+00	ADMIN	D	BEGINNER	10	f	t	en	0	DRAFT	T1770848479	2026-02-11 22:21:19.961709+00	1	17
3	2026-02-11 22:23:58.302655+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770848636058	2026-02-11 22:24:01.255948+00	3	18
4	2026-02-11 22:24:14.717227+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770848653082	2026-02-11 22:24:17.151416+00	3	21
5	2026-02-11 22:24:34.320995+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770848672508	2026-02-11 22:24:36.769796+00	3	24
16	2026-02-12 01:35:18.926894+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770860112903	2026-02-12 01:35:23.028301+00	3	49
6	2026-02-11 22:30:21.904064+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770849020187	2026-02-11 22:30:24.543553+00	3	27
7	2026-02-11 22:42:46.540581+00	ADMIN	tmp	BEGINNER	30	f	f	en	0	PUBLISHED	LearnerCheckCourse1770849765335	2026-02-11 22:42:46.569465+00	1	30
8	2026-02-11 22:44:14.919024+00	ADMIN	tmp	BEGINNER	30	f	f	en	0	PUBLISHED	DropCheckCourse1770849854037	2026-02-11 22:44:14.953182+00	1	31
9	2026-02-11 22:49:37.593234+00	ADMIN	tmp	BEGINNER	30	f	f	en	0	PUBLISHED	VerifyImplCourse1770850176453	2026-02-11 22:49:37.62872+00	1	32
10	2026-02-11 22:51:01.802454+00	ADMIN	tmp	BEGINNER	30	f	f	en	0	PUBLISHED	VerifyImplCourse1770850260667	2026-02-11 22:51:01.849047+00	1	33
11	2026-02-11 22:52:20.501998+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770850338226	2026-02-11 22:52:24.411685+00	3	34
12	2026-02-11 23:13:02.579917+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770851579822	2026-02-11 23:13:05.7916+00	3	37
13	2026-02-12 00:09:15.975371+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770854953830	2026-02-12 00:09:19.065179+00	3	40
17	2026-02-12 01:37:31.938724+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770860249520	2026-02-12 01:37:35.315493+00	3	52
14	2026-02-12 01:07:22.806732+00	ADMIN	SuiteCourseDesc	BEGINNER	120	t	t	en	0	PUBLISHED	SuiteCourse1770858439536	2026-02-12 01:07:25.660316+00	3	43
\.


--
-- Data for Name: enrollments; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.enrollments (id, completed_at, enrolled_at, last_accessed_at, status, course_id, user_id) FROM stdin;
1	\N	2026-02-11 22:18:23.418806	2026-02-11 22:18:23.553453	ACTIVE	1	7
2	\N	2026-02-11 22:24:00.057991	2026-02-11 22:24:00.294325	ACTIVE	3	8
3	\N	2026-02-11 22:24:16.183464	2026-02-11 22:24:16.398227	ACTIVE	4	9
4	\N	2026-02-11 22:24:35.715046	2026-02-11 22:24:35.94699	ACTIVE	5	10
5	\N	2026-02-11 22:30:23.476526	2026-02-11 22:30:23.707807	ACTIVE	6	11
6	\N	2026-02-11 22:42:46.601631	\N	ACTIVE	7	12
7	\N	2026-02-11 22:44:14.993265	\N	ACTIVE	8	14
8	\N	2026-02-11 22:49:37.667427	\N	ACTIVE	9	15
9	\N	2026-02-11 22:51:01.900703	\N	DROPPED	10	16
10	\N	2026-02-11 22:52:22.699507	2026-02-11 22:52:23.225537	DROPPED	11	17
11	\N	2026-02-11 23:13:04.309875	2026-02-11 23:13:04.809083	DROPPED	12	18
12	\N	2026-02-12 00:09:17.623626	2026-02-12 00:09:18.014888	DROPPED	13	19
13	\N	2026-02-12 01:07:24.372051	2026-02-12 01:07:24.712087	DROPPED	14	20
14	\N	2026-02-12 01:07:41.88471	2026-02-12 01:07:42.248269	DROPPED	15	21
15	\N	2026-02-12 01:35:21.302313	2026-02-12 01:35:21.776224	DROPPED	16	22
16	\N	2026-02-12 01:37:33.681289	2026-02-12 01:37:34.096605	DROPPED	17	23
\.


--
-- Data for Name: exam_attempt_answers; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.exam_attempt_answers (id, attempt_id, question_id, selected_option_id) FROM stdin;
\.


--
-- Data for Name: exam_attempts; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.exam_attempts (id, correct_answers, evaluated_at, exam_id, passed, score_percentage, started_at, status, submitted_at, total_questions, user_id, question_order) FROM stdin;
ca6a5440-fa52-4ee7-bdb0-7f98518dbba4	0	2026-02-11 22:18:23.33517	283b366f-a09d-47d0-bfac-7035498231e9	f	0	2026-02-11 22:18:23.183167	EVALUATED	2026-02-11 22:18:23.33517	0	6f50975f-a597-4707-bdbe-e08ee43ac089	\N
4f596e75-7860-4c2a-88da-cbfa944a1318	0	2026-02-11 22:23:59.921322	46ffe61c-5da0-4069-92d7-ef1af0ee09dd	f	0	2026-02-11 22:23:59.783663	EVALUATED	2026-02-11 22:23:59.921322	0	15ed31f3-ec94-450f-b958-dc3a2e4998ec	\N
b80672b6-c6ac-4443-a8b0-d6e9f98ec833	0	2026-02-11 22:24:16.100118	50f62b2d-d200-4662-aa75-0b269c732edd	f	0	2026-02-11 22:24:15.990581	EVALUATED	2026-02-11 22:24:16.100118	0	adcc7550-7e40-43d6-84a5-f4bd81e92d18	\N
c9790c11-0edf-4bed-93bb-caf23d76cda9	0	2026-02-11 22:24:35.609071	8a07b03d-00f2-42ee-9b0d-4a0b25f60b26	f	0	2026-02-11 22:24:35.523385	EVALUATED	2026-02-11 22:24:35.609071	0	7546ba1e-39cc-44c6-bdcd-e2bb83d8e6a4	\N
74af3393-2a97-461d-aa89-4aa090db0c14	0	2026-02-11 22:30:23.336295	d33352fa-8bff-49e9-b8b3-42917b03dd5a	f	0	2026-02-11 22:30:23.214739	EVALUATED	2026-02-11 22:30:23.336295	0	98c3ae8b-0993-4129-af31-c8a572955d4c	\N
6711743d-f917-49e1-8dab-955b1732780d	0	2026-02-11 22:52:22.546513	a255b876-6bf3-4b4b-bece-40d6938a1dd9	f	0	2026-02-11 22:52:22.009726	EVALUATED	2026-02-11 22:52:22.546513	0	e10b6385-790e-48a0-b6e9-e2fcf187b9d1	\N
013bef77-2244-4980-adaf-33e7e7d2dcf6	0	2026-02-11 23:13:04.164248	62805877-8df9-4528-b64f-e6aebe8a51f1	f	0	2026-02-11 23:13:04.022995	EVALUATED	2026-02-11 23:13:04.164248	0	fbee17be-0305-4ee4-94b2-23fb3f54aef8	\N
0aae0006-52b1-42da-9c1d-91bbe463001d	0	2026-02-12 00:09:17.48807	7abd9fa7-df67-41e3-8676-fd5788083d65	f	0	2026-02-12 00:09:17.322549	EVALUATED	2026-02-12 00:09:17.48807	0	f556e759-0d82-4caf-af07-7eea3b21e93b	\N
31042958-1f3c-41cb-a596-1b114feefe14	0	2026-02-12 01:07:24.257374	6e3ba660-0309-48e8-b374-0764c9f9d59f	f	0	2026-02-12 01:07:24.147755	EVALUATED	2026-02-12 01:07:24.257374	0	5d7872b1-b9e2-4d6b-9056-410448fc6ad3	\N
5551c02a-2f72-4a5a-a87d-5b7833009017	0	2026-02-12 01:07:41.775483	26977a14-078d-4b5d-a047-2d92ee0aef4c	f	0	2026-02-12 01:07:41.66128	EVALUATED	2026-02-12 01:07:41.775483	0	73d72262-83b4-44f0-b7d1-0203f74d9b60	\N
258673b5-cbfe-45d1-a5d2-d8d56712d7b1	0	2026-02-12 01:35:21.144134	bed9ad5f-22ed-4953-bbb0-1299b3c0f797	f	0	2026-02-12 01:35:20.77567	EVALUATED	2026-02-12 01:35:21.14412	0	0a4c59e1-d085-3ae4-a337-73a53cbce731	\N
1d16c164-630a-4e79-9476-2c8d19be7717	0	2026-02-12 01:37:33.546327	d266ce17-4976-405c-b85e-4114f981589f	f	0	2026-02-12 01:37:33.397926	EVALUATED	2026-02-12 01:37:33.54632	0	a0d0e4ac-37c9-3b07-8d3f-e24bc451b198	\N
\.


--
-- Data for Name: exam_options; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.exam_options (id, is_correct, display_order, option_text, question_id) FROM stdin;
\.


--
-- Data for Name: exam_questions; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.exam_questions (id, display_order, exam_id, explanation, question_text) FROM stdin;
\.


--
-- Data for Name: exams; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.exams (id, course_id, created_at, description, instructions, max_attempts, pass_percentage, rules_summary, shuffle_options, shuffle_questions, status, time_limit_minutes, title, updated_at) FROM stdin;
283b366f-a09d-47d0-bfac-7035498231e9	1	2026-02-11 22:18:22.964346	d	i	3	50	r	f	f	ARCHIVED	30	SuiteExam-1770848300	2026-02-11 22:18:23.361417
46ffe61c-5da0-4069-92d7-ef1af0ee09dd	3	2026-02-11 22:23:59.421592	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770848636058	2026-02-11 22:24:00.92893
50f62b2d-d200-4662-aa75-0b269c732edd	4	2026-02-11 22:24:15.665053	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770848653082	2026-02-11 22:24:16.922699
8a07b03d-00f2-42ee-9b0d-4a0b25f60b26	5	2026-02-11 22:24:35.230247	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770848672508	2026-02-11 22:24:36.454492
d33352fa-8bff-49e9-b8b3-42917b03dd5a	6	2026-02-11 22:30:22.902851	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770849020187	2026-02-11 22:30:24.227852
a255b876-6bf3-4b4b-bece-40d6938a1dd9	11	2026-02-11 22:52:21.606031	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770850338226	2026-02-11 22:52:23.99269
62805877-8df9-4528-b64f-e6aebe8a51f1	12	2026-02-11 23:13:03.676816	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770851579822	2026-02-11 23:13:05.470091
7abd9fa7-df67-41e3-8676-fd5788083d65	13	2026-02-12 00:09:16.979913	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770854953830	2026-02-12 00:09:18.76223
6e3ba660-0309-48e8-b374-0764c9f9d59f	14	2026-02-12 01:07:23.852698	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770858439536	2026-02-12 01:07:25.353321
26977a14-078d-4b5d-a047-2d92ee0aef4c	15	2026-02-12 01:07:41.346373	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770858458635	2026-02-12 01:07:42.907932
bed9ad5f-22ed-4953-bbb0-1299b3c0f797	16	2026-02-12 01:35:20.282027	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770860112903	2026-02-12 01:35:22.622215
d266ce17-4976-405c-b85e-4114f981589f	17	2026-02-12 01:37:33.00997	Suite exam description	Answer all questions	3	50	No extra rules	f	f	ARCHIVED	30	SuiteExam1770860249520	2026-02-12 01:37:34.922795
\.


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	<< Flyway Baseline >>	BASELINE	<< Flyway Baseline >>	\N	educator	2026-02-13 13:16:36.90522	0	t
2	2	repair certificates course id type	SQL	V2__repair_certificates_course_id_type.sql	-1128306381	educator	2026-02-13 13:16:37.056407	92	t
3	3	add exam attempt question order	SQL	V3__add_exam_attempt_question_order.sql	-1921452960	educator	2026-02-13 13:17:14.210328	29	t
\.


--
-- Data for Name: hierarchy_nodes; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.hierarchy_nodes (id, created_at, created_by, description_en, is_deleted, is_published, is_visible, name_en, slug, sort_order, updated_at, version, parent_id) FROM stdin;
1	2026-02-11 21:18:06.736117+00	\N	\N	f	f	t	RootA	root-a-1770864487	0	2026-02-11 21:18:06.736117+00	0	\N
2	2026-02-11 21:18:06.801339+00	\N	\N	f	f	t	RootB	root-b-1770864487	0	2026-02-11 21:18:06.801339+00	0	\N
4	2026-02-11 21:18:23.677307+00	\N	\N	f	f	t	RootA	root-a-1770844703	0	2026-02-11 21:18:23.677307+00	0	\N
5	2026-02-11 21:18:23.905312+00	\N	\N	f	f	t	RootB	root-b-1770844703	0	2026-02-11 21:18:23.905312+00	0	\N
6	2026-02-11 21:18:23.963723+00	\N	\N	f	f	t	Child	child-1770844703	0	2026-02-11 21:18:23.963723+00	0	1
3	2026-02-11 21:18:06.845067+00	\N	\N	f	f	t	Child	child-1770864487	0	2026-02-11 21:18:24.036532+00	1	2
7	2026-02-11 21:18:33.899649+00	\N	\N	f	f	t	RootA	test-root-a-101	0	2026-02-11 21:18:33.899649+00	0	\N
8	2026-02-11 21:18:44.516926+00	\N	\N	f	f	t	RootB	test-root-b-101	0	2026-02-11 21:18:44.516926+00	0	\N
9	2026-02-11 21:18:44.774405+00	\N	\N	f	f	t	Child	test-child-101	0	2026-02-11 21:18:45.098964+00	1	8
10	2026-02-11 21:22:10.980657+00	\N	\N	f	f	t	A	mv-a-1770844930	0	2026-02-11 21:22:10.980657+00	0	\N
11	2026-02-11 21:22:11.103716+00	\N	\N	f	f	t	B	mv-b-1770844930	0	2026-02-11 21:22:11.103716+00	0	\N
12	2026-02-11 21:22:11.157276+00	\N	\N	f	f	t	C	mv-c-1770844930	0	2026-02-11 21:22:11.202944+00	1	11
13	2026-02-11 22:06:29.602071+00	\N	\N	f	f	t	Probe	probe-local-1	0	2026-02-11 22:06:29.602071+00	0	\N
15	2026-02-11 22:18:21.686597+00	\N	Root2	f	f	t	SuiteRoot2	suite-root2-1770848300	0	2026-02-11 22:18:21.686597+00	0	\N
14	2026-02-11 22:18:21.649231+00	\N	Updated	f	t	t	SuiteRoot1Updated	suite-root1-1770848300	1	2026-02-11 22:18:21.8856+00	1	\N
16	2026-02-11 22:18:21.764736+00	\N	\N	f	f	t	SuiteChild	suite-child-1770848300	0	2026-02-11 22:18:22.28134+00	3	15
17	2026-02-11 22:21:19.883983+00	\N	\N	f	f	t	R	deltest-root-1770848479	0	2026-02-11 22:21:19.883983+00	0	\N
19	2026-02-11 22:23:57.251274+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770848636058	1	2026-02-11 22:23:57.251274+00	0	\N
18	2026-02-11 22:23:57.120161+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770848636058	2	2026-02-11 22:23:57.508342+00	1	\N
49	2026-02-12 01:35:17.144355+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770860112903	2	2026-02-12 01:35:17.817891+00	1	\N
20	2026-02-11 22:23:57.390471+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770848636058	0	2026-02-11 22:23:58.181587+00	3	19
22	2026-02-11 22:24:13.930625+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770848653082	1	2026-02-11 22:24:13.930625+00	0	\N
21	2026-02-11 22:24:13.819523+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770848653082	2	2026-02-11 22:24:14.104128+00	1	\N
36	2026-02-11 22:52:19.493136+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770850338226	0	2026-02-11 22:52:20.391079+00	3	35
23	2026-02-11 22:24:14.02595+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770848653082	0	2026-02-11 22:24:14.618876+00	3	22
25	2026-02-11 22:24:33.376673+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770848672508	1	2026-02-11 22:24:33.376673+00	0	\N
24	2026-02-11 22:24:33.286318+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770848672508	2	2026-02-11 22:24:33.60624+00	1	\N
26	2026-02-11 22:24:33.491755+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770848672508	0	2026-02-11 22:24:34.209361+00	3	25
28	2026-02-11 22:30:21.020997+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770849020187	1	2026-02-11 22:30:21.020997+00	0	\N
27	2026-02-11 22:30:20.911627+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770849020187	2	2026-02-11 22:30:21.216859+00	1	\N
38	2026-02-11 23:13:01.446468+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770851579822	1	2026-02-11 23:13:01.446468+00	0	\N
29	2026-02-11 22:30:21.124363+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770849020187	0	2026-02-11 22:30:21.810375+00	3	28
30	2026-02-11 22:42:46.504125+00	api-check	tmp	f	f	t	LearnerCheckRoot	learner-check-root-1770849765335	0	2026-02-11 22:42:46.504125+00	0	\N
31	2026-02-11 22:44:14.883191+00	api-check	tmp	f	f	t	DropCheckRoot	drop-check-root-1770849854037	0	2026-02-11 22:44:14.883191+00	0	\N
32	2026-02-11 22:49:37.513768+00	api-check	tmp	f	f	t	VerifyImplRoot	verify-impl-root-1770850176453	0	2026-02-11 22:49:37.513768+00	0	\N
33	2026-02-11 22:51:01.750556+00	api-check	tmp	f	f	t	VerifyImplRoot	verify-impl-root-1770850260667	0	2026-02-11 22:51:01.750556+00	0	\N
35	2026-02-11 22:52:19.371409+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770850338226	1	2026-02-11 22:52:19.371409+00	0	\N
34	2026-02-11 22:52:19.232338+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770850338226	2	2026-02-11 22:52:19.61086+00	1	\N
37	2026-02-11 23:13:01.316372+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770851579822	2	2026-02-11 23:13:01.689516+00	1	\N
45	2026-02-12 01:07:21.864341+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770858439536	0	2026-02-12 01:07:22.693827+00	3	44
39	2026-02-11 23:13:01.582571+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770851579822	0	2026-02-11 23:13:02.449162+00	3	38
41	2026-02-12 00:09:14.943642+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770854953830	1	2026-02-12 00:09:14.943642+00	0	\N
40	2026-02-12 00:09:14.838044+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770854953830	2	2026-02-12 00:09:15.18292+00	1	\N
42	2026-02-12 00:09:15.064519+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770854953830	0	2026-02-12 00:09:15.88055+00	3	41
44	2026-02-12 01:07:21.765166+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770858439536	1	2026-02-12 01:07:21.765166+00	0	\N
43	2026-02-12 01:07:21.652367+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770858439536	2	2026-02-12 01:07:21.988794+00	1	\N
47	2026-02-12 01:07:39.564891+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770858458635	1	2026-02-12 01:07:39.564891+00	0	\N
46	2026-02-12 01:07:39.458584+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770858458635	2	2026-02-12 01:07:39.75718+00	1	\N
48	2026-02-12 01:07:39.672115+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770858458635	0	2026-02-12 01:07:40.361283+00	3	47
50	2026-02-12 01:35:17.417078+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770860112903	1	2026-02-12 01:35:17.417078+00	0	\N
51	2026-02-12 01:35:17.603596+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770860112903	0	2026-02-12 01:35:18.782072+00	3	50
53	2026-02-12 01:37:30.708365+00	suite-admin	RootB	f	f	t	SuiteRootB	suite-root-b-1770860249520	1	2026-02-12 01:37:30.708365+00	0	\N
52	2026-02-12 01:37:30.546283+00	suite-admin	UpdatedRootA	f	t	t	SuiteRootAUpdated	suite-root-a-1770860249520	2	2026-02-12 01:37:31.021532+00	1	\N
54	2026-02-12 01:37:30.873158+00	\N	ChildNode	f	f	t	SuiteChild	suite-child-1770860249520	0	2026-02-12 01:37:31.804117+00	3	53
\.


--
-- Data for Name: homepage_sections; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.homepage_sections (id, enabled, order_index, "position", title) FROM stdin;
48ca367d-e840-4928-a6f3-413b1128465f	t	0	TOP	Suite Section 1770848300
bb1a2c58-fca7-4328-a49f-66f82331064b	t	0	TOP	SuiteSection1770848636058
b26ee4aa-6cfb-4a00-8105-1a4a3111656c	t	0	TOP	SuiteSection1770848653082
1019c182-68a1-467e-9063-c9ebdb15b7b3	t	0	TOP	SuiteSection1770848672508
22c7c79d-7c85-44c1-a24b-c63bd5f99271	t	0	TOP	SuiteSection1770849020187
d6194953-9eb1-47da-95be-7a134f054f07	t	0	TOP	SuiteSection1770850338226
d6cccfc1-48c0-4524-b679-8607be32d37c	t	0	TOP	SuiteSection1770851579822
3c7d3de9-f814-4211-aa80-9bbe2f9b35c1	t	0	TOP	SuiteSection1770854953830
a800e2df-c1a7-40ae-8aa7-a282097a6502	t	0	TOP	SuiteSection1770858439536
b9bf070d-ff34-4ed0-b4ea-8ed9ee5d0f96	t	0	TOP	SuiteSection1770858458635
9b670377-5207-48d6-bad4-37bc50f62561	t	0	TOP	SuiteSection1770860112903
ce8ca1d3-80db-4ed1-8765-6f05f16a9637	t	0	TOP	SuiteSection1770860249520
\.


--
-- Data for Name: lesson_progress; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.lesson_progress (id, completed, completed_at, enrollment_id, lesson_id) FROM stdin;
1	t	2026-02-11 22:18:23.544125	1	1
2	t	2026-02-11 22:24:00.289992	2	4
3	t	2026-02-11 22:24:16.394862	3	7
4	t	2026-02-11 22:24:35.942678	4	10
5	t	2026-02-11 22:30:23.704566	5	13
6	t	2026-02-11 22:52:23.219342	10	16
7	t	2026-02-11 23:13:04.80388	11	19
8	t	2026-02-12 00:09:18.009247	12	22
9	t	2026-02-12 01:07:24.708215	13	25
10	t	2026-02-12 01:07:42.244346	14	28
11	t	2026-02-12 01:35:21.769116	15	31
12	t	2026-02-12 01:37:34.092712	16	34
\.


--
-- Data for Name: lessons; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.lessons (id, created_at, depth_level, document_url, is_deleted, order_index, path, text_content, type, updated_at, video_url, course_id, parent_lesson_id) FROM stdin;
1	2026-02-11 22:18:22.500722+00	0	\N	f	0	/course/1/lesson/1	Hello	TEXT	2026-02-11 22:18:22.516134+00	\N	1	\N
2	2026-02-11 22:18:22.574815+00	0	\N	f	1	/course/1/lesson/2	\N	VIDEO	2026-02-11 22:18:22.579907+00	https://example.com/v.mp4	1	\N
3	2026-02-11 22:18:22.615+00	0	https://example.com/d.pdf	t	2	/course/1/lesson/3	\N	DOCUMENT	2026-02-11 22:18:23.74701+00	\N	1	\N
4	2026-02-11 22:23:58.614961+00	0	\N	f	0	/course/3/lesson/4	WelcomeToTheCourse	TEXT	2026-02-11 22:23:58.626882+00	\N	3	\N
5	2026-02-11 22:23:58.756829+00	0	\N	f	1	/course/3/lesson/5	\N	VIDEO	2026-02-11 22:23:58.763643+00	https://example.com/video.mp4	3	\N
25	2026-02-12 01:07:23.019445+00	0	\N	f	0	/course/14/lesson/25	WelcomeToTheCourse	TEXT	2026-02-12 01:07:23.026039+00	\N	14	\N
6	2026-02-11 22:23:58.914275+00	0	https://example.com/document.pdf	t	2	/course/3/lesson/6	\N	DOCUMENT	2026-02-11 22:24:01.147785+00	\N	3	\N
7	2026-02-11 22:24:14.916896+00	0	\N	f	0	/course/4/lesson/7	WelcomeToTheCourse	TEXT	2026-02-11 22:24:14.920952+00	\N	4	\N
8	2026-02-11 22:24:15.013496+00	0	\N	f	1	/course/4/lesson/8	\N	VIDEO	2026-02-11 22:24:15.018411+00	https://example.com/video.mp4	4	\N
9	2026-02-11 22:24:15.160672+00	0	https://example.com/document.pdf	t	2	/course/4/lesson/9	\N	DOCUMENT	2026-02-11 22:24:17.09015+00	\N	4	\N
10	2026-02-11 22:24:34.534223+00	0	\N	f	0	/course/5/lesson/10	WelcomeToTheCourse	TEXT	2026-02-11 22:24:34.539421+00	\N	5	\N
11	2026-02-11 22:24:34.642791+00	0	\N	f	1	/course/5/lesson/11	\N	VIDEO	2026-02-11 22:24:34.64924+00	https://example.com/video.mp4	5	\N
26	2026-02-12 01:07:23.193544+00	0	\N	f	1	/course/14/lesson/26	\N	VIDEO	2026-02-12 01:07:23.202131+00	https://example.com/video.mp4	14	\N
12	2026-02-11 22:24:34.765533+00	0	https://example.com/document.pdf	t	2	/course/5/lesson/12	\N	DOCUMENT	2026-02-11 22:24:36.659319+00	\N	5	\N
13	2026-02-11 22:30:22.130522+00	0	\N	f	0	/course/6/lesson/13	WelcomeToTheCourse	TEXT	2026-02-11 22:30:22.138279+00	\N	6	\N
14	2026-02-11 22:30:22.2703+00	0	\N	f	1	/course/6/lesson/14	\N	VIDEO	2026-02-11 22:30:22.275006+00	https://example.com/video.mp4	6	\N
15	2026-02-11 22:30:22.386932+00	0	https://example.com/document.pdf	t	2	/course/6/lesson/15	\N	DOCUMENT	2026-02-11 22:30:24.425562+00	\N	6	\N
16	2026-02-11 22:52:20.735272+00	0	\N	f	0	/course/11/lesson/16	WelcomeToTheCourse	TEXT	2026-02-11 22:52:20.742038+00	\N	11	\N
17	2026-02-11 22:52:20.857875+00	0	\N	f	1	/course/11/lesson/17	\N	VIDEO	2026-02-11 22:52:20.863974+00	https://example.com/video.mp4	11	\N
18	2026-02-11 22:52:20.997735+00	0	https://example.com/document.pdf	t	2	/course/11/lesson/18	\N	DOCUMENT	2026-02-11 22:52:24.302139+00	\N	11	\N
19	2026-02-11 23:13:02.859548+00	0	\N	f	0	/course/12/lesson/19	WelcomeToTheCourse	TEXT	2026-02-11 23:13:02.867172+00	\N	12	\N
20	2026-02-11 23:13:02.997968+00	0	\N	f	1	/course/12/lesson/20	\N	VIDEO	2026-02-11 23:13:03.001743+00	https://example.com/video.mp4	12	\N
27	2026-02-12 01:07:23.369289+00	0	https://example.com/document.pdf	t	2	/course/14/lesson/27	\N	DOCUMENT	2026-02-12 01:07:25.551737+00	\N	14	\N
21	2026-02-11 23:13:03.128196+00	0	https://example.com/document.pdf	t	2	/course/12/lesson/21	\N	DOCUMENT	2026-02-11 23:13:05.694023+00	\N	12	\N
22	2026-02-12 00:09:16.217659+00	0	\N	f	0	/course/13/lesson/22	WelcomeToTheCourse	TEXT	2026-02-12 00:09:16.226759+00	\N	13	\N
23	2026-02-12 00:09:16.340598+00	0	\N	f	1	/course/13/lesson/23	\N	VIDEO	2026-02-12 00:09:16.345055+00	https://example.com/video.mp4	13	\N
24	2026-02-12 00:09:16.461442+00	0	https://example.com/document.pdf	t	2	/course/13/lesson/24	\N	DOCUMENT	2026-02-12 00:09:18.965396+00	\N	13	\N
28	2026-02-12 01:07:40.664446+00	0	\N	f	0	/course/15/lesson/28	WelcomeToTheCourse	TEXT	2026-02-12 01:07:40.667987+00	\N	15	\N
29	2026-02-12 01:07:40.78393+00	0	\N	f	1	/course/15/lesson/29	\N	VIDEO	2026-02-12 01:07:40.78818+00	https://example.com/video.mp4	15	\N
30	2026-02-12 01:07:40.897916+00	0	https://example.com/document.pdf	t	2	/course/15/lesson/30	\N	DOCUMENT	2026-02-12 01:07:43.135418+00	\N	15	\N
31	2026-02-12 01:35:19.236366+00	0	\N	f	0	/course/16/lesson/31	WelcomeToTheCourse	TEXT	2026-02-12 01:35:19.241761+00	\N	16	\N
32	2026-02-12 01:35:19.483752+00	0	\N	f	1	/course/16/lesson/32	\N	VIDEO	2026-02-12 01:35:19.491306+00	https://example.com/video.mp4	16	\N
33	2026-02-12 01:35:19.647028+00	0	https://example.com/document.pdf	t	2	/course/16/lesson/33	\N	DOCUMENT	2026-02-12 01:35:22.890965+00	\N	16	\N
34	2026-02-12 01:37:32.219663+00	0	\N	f	0	/course/17/lesson/34	WelcomeToTheCourse	TEXT	2026-02-12 01:37:32.223269+00	\N	17	\N
35	2026-02-12 01:37:32.371343+00	0	\N	f	1	/course/17/lesson/35	\N	VIDEO	2026-02-12 01:37:32.3765+00	https://example.com/video.mp4	17	\N
36	2026-02-12 01:37:32.529643+00	0	https://example.com/document.pdf	t	2	/course/17/lesson/36	\N	DOCUMENT	2026-02-12 01:37:35.175926+00	\N	17	\N
\.


--
-- Data for Name: media_references; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.media_references (id, active, duration_seconds, lesson_id, media_type, source_type, source_url, title) FROM stdin;
\.


--
-- Data for Name: notifications; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.notifications (id, created_at, message, read, status, title, type, user_id) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.roles (id, name) FROM stdin;
1	STUDENT
2	INSTRUCTOR
3	ADMIN
\.


--
-- Data for Name: section_blocks; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.section_blocks (id, block_type, enabled, order_index, section_id) FROM stdin;
941404f8-8864-4013-bcf6-ced755717022	TEXT	t	0	48ca367d-e840-4928-a6f3-413b1128465f
3861e0d9-4ba8-4bbe-80ae-a10326d6701b	TEXT	t	0	bb1a2c58-fca7-4328-a49f-66f82331064b
af27ad48-f962-453c-babc-25e4096122d3	TEXT	t	0	b26ee4aa-6cfb-4a00-8105-1a4a3111656c
9ededbf0-265c-4f82-be4f-4ef7e66059f0	TEXT	t	0	1019c182-68a1-467e-9063-c9ebdb15b7b3
d3b3d98e-22ad-4047-8beb-dd44ab6a991b	TEXT	t	0	22c7c79d-7c85-44c1-a24b-c63bd5f99271
e4177ad0-0742-4122-ba64-728d42b8b476	TEXT	t	0	d6194953-9eb1-47da-95be-7a134f054f07
abf2fdfd-38b0-4cc8-ac43-0a646e184c31	TEXT	t	0	d6cccfc1-48c0-4524-b679-8607be32d37c
14a6dd1d-086d-44a1-a464-df03484fd1e2	TEXT	t	0	3c7d3de9-f814-4211-aa80-9bbe2f9b35c1
b8e6e6f2-8137-466c-8a37-e14359910d1e	TEXT	t	0	a800e2df-c1a7-40ae-8aa7-a282097a6502
5f53f192-aae9-4db2-aba6-300f19056ebe	TEXT	t	0	b9bf070d-ff34-4ed0-b4ea-8ed9ee5d0f96
5400afec-3933-4e01-bd51-4f811c9c885c	TEXT	t	0	9b670377-5207-48d6-bad4-37bc50f62561
0a50f827-2114-4a3a-9ed1-f090d6d02655	TEXT	t	0	ce8ca1d3-80db-4ed1-8765-6f05f16a9637
\.


--
-- Data for Name: section_layouts; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.section_layouts (id, auto_scroll, layout_type, scroll_interval_seconds, section_id) FROM stdin;
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.user_roles (user_id, role_id) FROM stdin;
2	1
3	1
4	1
5	1
6	1
7	1
8	1
9	1
10	1
11	1
12	1
13	1
14	1
15	1
16	1
17	1
18	1
19	1
20	1
21	1
22	1
23	1
1	3
24	1
25	2
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.users (id, email, password) FROM stdin;
2	{{register_email}}	$2a$10$WVMzo3gxfnG7huvv0cNL/.W2rkt6Ls8.lg4W3Oov6uLfGqYIaph3C
3	admin@educator.com	$2a$10$do9nNongd3zZKjb5EHbHlO4VnSLx13ruawpuIKHqwgm/68P3rvQWq
4	student_1770845514208@test.com	$2a$10$12ajmGIWa0zimyYbnLIwAeukdyJqzBiq.zaCF.lSxixTOkaL4sTre
5	student@test.com	$2a$10$bubCu3kptNNgC2pLfOefrOfnvXt/jKYkvCfgRZU2HdVZ2k5yRqIgu
6	student_1770847150876@test.com	$2a$10$3xPLq1vVcQSQh5ImmKHDc.DnUXCF5Um/4hbTk78YOleOJTIEFspjO
7	suite_1770848300@test.com	$2a$10$YtiertgWjMAt1jE6hfDRxOUROfZIi3W3q7m4b.elkB29pVJ73KBO2
8	student_1770848636058@test.com	$2a$10$yEx40c/gsfCvmKxVwIo8ReHmmWMqNuZQvjPCxn5MP4Sj2Dz.NAKyO
9	student_1770848653082@test.com	$2a$10$QGnhz2RUNxCeHhEpReXOguBoy8G8xc7ax5lffmZRfzlzlMZIZnYSq
10	student_1770848672508@test.com	$2a$10$sxnNTxnIVOb9EM/qIn4hIOyswmOSkhRuGx58GqBTTrgMcHAe2VKBO
11	student_1770849020187@test.com	$2a$10$kt7AKCARtkmT7z4H2o43k.cXcgJRZhG6OZ536ZxlSvpxwMknGybRm
12	suite+learnercheck+1770849765335@example.com	$2a$10$fuvofuQLkOdUkqcdGbozye/9jILs4W.NE4yQmdhNvJiHQbCbwmu9G
13	suite+enrolllist+1770849782847@example.com	$2a$10$RcpKSlEWPsxh97k7MbLQsOOljfUHnG2UjH/kNZu2xpf3LIu6mBj.G
14	suite+dropcheck+1770849854037@example.com	$2a$10$Ciovxu6DxgfP5t6KXbXs6.hta2paksl0Vq64pbp.9NneOTcVIJZRe
15	suite+verifyimpl+1770850176453@example.com	$2a$10$5dNZI.6PlCU3UQW0FBvUnOR5s71tzrvwQGEZKuegoE8lJHlvmobBa
16	suite+verifyimpl+1770850260667@example.com	$2a$10$bB1AqB2nIuhmZrhLhhSKf.00OTvPhTKWoeQWJ4EqZs8K5DCCuXZd6
17	student_1770850338226@test.com	$2a$10$3P5ZumEN85RlXort3fZl4.ONZ.oiRDP.d9Kx0LxnuDHB/vGSw.Aze
18	student_1770851579822@test.com	$2a$10$C1DqcDuLObnz.0Zn38eVVeafbGbLT97ZTcNCjVTPQNI8uA0Hs3le6
19	student_1770854953830@test.com	$2a$10$Ia.Nr0QdpC1/7jXBwHwZredZcc1Uryt03gvU.bEIA7HuLPdy5DCJi
20	student_1770858439536@test.com	$2a$10$fbJ/.D2K7F82vJrFBvWFk.A0dInDDuJqUfeIo2gKvoEUG5fy.16Pa
21	student_1770858458635@test.com	$2a$10$MII05YRNT56fvzxyaBAziuTdxDRXTESWZEQEW2tnskbEGlW6aPnIe
22	student_1770860112903@test.com	$2a$10$LOY1p53h34Cjl3gfiMjjFeO8SA1p9G4OEFoVpcBI3EEr/Emsk.80a
23	student_1770860249520@test.com	$2a$10$Bb.lY.YQft/pJO1c5/yRbOKjr0E6e9RqfUYuqq2gW8fBPKJap7yPW
1	jayaramadmin@educate.com	$2a$10$YgVKJRR8J/lcy7IbxELZ9OkeMtZUnwFa9PWhsLUwzDgm58EI4jooi
24	jayaramstudent@educate.com	$2a$10$YgVKJRR8J/lcy7IbxELZ9OkeMtZUnwFa9PWhsLUwzDgm58EI4jooi
25	jayaraminstructor@educate.com	$2a$10$YgVKJRR8J/lcy7IbxELZ9OkeMtZUnwFa9PWhsLUwzDgm58EI4jooi
\.


--
-- Name: courses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.courses_id_seq', 17, true);


--
-- Name: enrollments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.enrollments_id_seq', 16, true);


--
-- Name: hierarchy_nodes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.hierarchy_nodes_id_seq', 54, true);


--
-- Name: lesson_progress_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.lesson_progress_id_seq', 12, true);


--
-- Name: lessons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.lessons_id_seq', 36, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.roles_id_seq', 3, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.users_id_seq', 25, true);


--
-- Name: automation_event_logs automation_event_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.automation_event_logs
    ADD CONSTRAINT automation_event_logs_pkey PRIMARY KEY (id);


--
-- Name: automation_rules automation_rules_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.automation_rules
    ADD CONSTRAINT automation_rules_pkey PRIMARY KEY (id);


--
-- Name: block_configs block_configs_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.block_configs
    ADD CONSTRAINT block_configs_pkey PRIMARY KEY (id);


--
-- Name: certificates certificates_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT certificates_pkey PRIMARY KEY (id);


--
-- Name: course_completions course_completions_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.course_completions
    ADD CONSTRAINT course_completions_pkey PRIMARY KEY (id);


--
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (id);


--
-- Name: enrollments enrollments_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.enrollments
    ADD CONSTRAINT enrollments_pkey PRIMARY KEY (id);


--
-- Name: exam_attempt_answers exam_attempt_answers_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.exam_attempt_answers
    ADD CONSTRAINT exam_attempt_answers_pkey PRIMARY KEY (id);


--
-- Name: exam_attempts exam_attempts_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.exam_attempts
    ADD CONSTRAINT exam_attempts_pkey PRIMARY KEY (id);


--
-- Name: exam_options exam_options_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.exam_options
    ADD CONSTRAINT exam_options_pkey PRIMARY KEY (id);


--
-- Name: exam_questions exam_questions_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.exam_questions
    ADD CONSTRAINT exam_questions_pkey PRIMARY KEY (id);


--
-- Name: exams exams_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.exams
    ADD CONSTRAINT exams_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: hierarchy_nodes hierarchy_nodes_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.hierarchy_nodes
    ADD CONSTRAINT hierarchy_nodes_pkey PRIMARY KEY (id);


--
-- Name: homepage_sections homepage_sections_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.homepage_sections
    ADD CONSTRAINT homepage_sections_pkey PRIMARY KEY (id);


--
-- Name: hierarchy_nodes idx_hierarchy_slug; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.hierarchy_nodes
    ADD CONSTRAINT idx_hierarchy_slug UNIQUE (slug);


--
-- Name: lesson_progress lesson_progress_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.lesson_progress
    ADD CONSTRAINT lesson_progress_pkey PRIMARY KEY (id);


--
-- Name: lessons lessons_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT lessons_pkey PRIMARY KEY (id);


--
-- Name: media_references media_references_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.media_references
    ADD CONSTRAINT media_references_pkey PRIMARY KEY (id);


--
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: section_blocks section_blocks_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.section_blocks
    ADD CONSTRAINT section_blocks_pkey PRIMARY KEY (id);


--
-- Name: section_layouts section_layouts_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.section_layouts
    ADD CONSTRAINT section_layouts_pkey PRIMARY KEY (id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: lesson_progress ukfsjlcxaxfenifmlnu5l6yxqgc; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.lesson_progress
    ADD CONSTRAINT ukfsjlcxaxfenifmlnu5l6yxqgc UNIQUE (enrollment_id, lesson_id);


--
-- Name: enrollments ukg1muiskd02x66lpy6fqcj6b9q; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.enrollments
    ADD CONSTRAINT ukg1muiskd02x66lpy6fqcj6b9q UNIQUE (user_id, course_id);


--
-- Name: certificates uki1uki0pqbx17xxvpwf1ocq0e; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT uki1uki0pqbx17xxvpwf1ocq0e UNIQUE (course_id, user_id);


--
-- Name: exams ukjq8evw709sky34ue2nd9joyp0; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.exams
    ADD CONSTRAINT ukjq8evw709sky34ue2nd9joyp0 UNIQUE (course_id);


--
-- Name: roles ukofx66keruapi6vyqpv6f2or37; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT ukofx66keruapi6vyqpv6f2or37 UNIQUE (name);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: idx_course_difficulty; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX idx_course_difficulty ON public.courses USING btree (difficulty);


--
-- Name: idx_course_hierarchy; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX idx_course_hierarchy ON public.courses USING btree (hierarchy_node_id);


--
-- Name: idx_course_status; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX idx_course_status ON public.courses USING btree (status);


--
-- Name: idx_hierarchy_parent; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX idx_hierarchy_parent ON public.hierarchy_nodes USING btree (parent_id);


--
-- Name: lessons fk17ucc7gjfjddsyi0gvstkqeat; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT fk17ucc7gjfjddsyi0gvstkqeat FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: enrollments fk3hjx6rcnbmfw368sxigrpfpx0; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.enrollments
    ADD CONSTRAINT fk3hjx6rcnbmfw368sxigrpfpx0 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: courses fk4ovevk9277x3k9fwm6brkmfa9; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT fk4ovevk9277x3k9fwm6brkmfa9 FOREIGN KEY (hierarchy_node_id) REFERENCES public.hierarchy_nodes(id);


--
-- Name: user_roles fkh8ciramu9cc9q3qcqiv4ue8a6; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles fkhfh9dx7w3ubf1co1vdev94g3f; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: enrollments fkho8mcicp4196ebpltdn9wl6co; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.enrollments
    ADD CONSTRAINT fkho8mcicp4196ebpltdn9wl6co FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: lesson_progress fkkx3nc17yyecdqwfgdydqmc24x; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.lesson_progress
    ADD CONSTRAINT fkkx3nc17yyecdqwfgdydqmc24x FOREIGN KEY (enrollment_id) REFERENCES public.enrollments(id);


--
-- Name: lessons fkm91rjsw6xa57ndbnkdnd8mypx; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT fkm91rjsw6xa57ndbnkdnd8mypx FOREIGN KEY (parent_lesson_id) REFERENCES public.lessons(id);


--
-- Name: hierarchy_nodes fkoimes405g5ov3pdiwa010j1c; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.hierarchy_nodes
    ADD CONSTRAINT fkoimes405g5ov3pdiwa010j1c FOREIGN KEY (parent_id) REFERENCES public.hierarchy_nodes(id);


--
-- Name: lesson_progress fkqwr70bkn0j6gok1y4op9jns8y; Type: FK CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.lesson_progress
    ADD CONSTRAINT fkqwr70bkn0j6gok1y4op9jns8y FOREIGN KEY (lesson_id) REFERENCES public.lessons(id);


--
-- PostgreSQL database dump complete
--

\unrestrict Sg7n5v1HVohUOwDdIEmJlceZwPPcOkzkRSX37OLYSbyyCRapAnEkbYzydRZjF0n

