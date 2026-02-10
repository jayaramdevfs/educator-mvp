--
-- PostgreSQL database dump
--

\restrict BR6aszmnoulvxITpbF9jJve0Q2zdAEkOVv9ZEbAi2sSvMZR9eseMKtPd1BfH0Dv

-- Dumped from database version 15.15 (Debian 15.15-1.pgdg13+1)
-- Dumped by pg_dump version 15.15 (Debian 15.15-1.pgdg13+1)

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
    course_id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    expired_at timestamp(6) without time zone,
    issued_at timestamp(6) without time zone,
    revoked_at timestamp(6) without time zone,
    status character varying(255) NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT certificates_status_check CHECK (((status)::text = ANY ((ARRAY['GENERATED'::character varying, 'ISSUED'::character varying, 'REVOKED'::character varying, 'EXPIRED'::character varying])::text[])))
);


ALTER TABLE public.certificates OWNER TO educator;

--
-- Name: course_completions; Type: TABLE; Schema: public; Owner: educator
--

CREATE TABLE public.course_completions (
    id uuid NOT NULL,
    completed_at timestamp(6) without time zone NOT NULL,
    course_id uuid NOT NULL,
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
    created_at timestamp(6) without time zone,
    status character varying(20) NOT NULL,
    updated_at timestamp(6) without time zone,
    course_id bigint NOT NULL,
    user_id bigint NOT NULL,
    completed_at timestamp(6) without time zone,
    enrolled_at timestamp(6) without time zone NOT NULL,
    last_accessed_at timestamp(6) without time zone,
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
    course_id uuid NOT NULL,
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
    document_url character varying(255),
    is_deleted boolean NOT NULL,
    order_index integer NOT NULL,
    text_content text,
    type character varying(255) NOT NULL,
    video_url character varying(255),
    course_id bigint NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    depth_level integer NOT NULL,
    path character varying(512) NOT NULL,
    parent_lesson_id bigint,
    CONSTRAINT lessons_type_check CHECK (((type)::text = ANY (ARRAY[('TEXT'::character varying)::text, ('VIDEO'::character varying)::text, ('DOCUMENT'::character varying)::text])))
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

COPY public.certificates (id, course_completion_id, course_id, created_at, expired_at, issued_at, revoked_at, status, user_id) FROM stdin;
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
1	2026-02-05 16:24:47.573691+00	ADMIN	Beginner Java Course	BEGINNER	120	f	f	en	0	DRAFT	Java Basics	2026-02-05 16:24:47.573691+00	0	1
\.


--
-- Data for Name: enrollments; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.enrollments (id, created_at, status, updated_at, course_id, user_id, completed_at, enrolled_at, last_accessed_at) FROM stdin;
\.


--
-- Data for Name: exam_attempt_answers; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.exam_attempt_answers (id, attempt_id, question_id, selected_option_id) FROM stdin;
\.


--
-- Data for Name: exam_attempts; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.exam_attempts (id, correct_answers, evaluated_at, exam_id, passed, score_percentage, started_at, status, submitted_at, total_questions, user_id) FROM stdin;
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
\.


--
-- Data for Name: hierarchy_nodes; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.hierarchy_nodes (id, created_at, created_by, description_en, is_deleted, is_published, is_visible, name_en, slug, sort_order, updated_at, version, parent_id) FROM stdin;
1	2026-02-04 15:25:41.56288+00	admin	programming courses	f	f	t	programming	programming	1	2026-02-04 15:25:41.56288+00	0	\N
\.


--
-- Data for Name: homepage_sections; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.homepage_sections (id, enabled, order_index, "position", title) FROM stdin;
\.


--
-- Data for Name: lesson_progress; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.lesson_progress (id, completed, completed_at, enrollment_id, lesson_id) FROM stdin;
\.


--
-- Data for Name: lessons; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.lessons (id, document_url, is_deleted, order_index, text_content, type, video_url, course_id, created_at, updated_at, depth_level, path, parent_lesson_id) FROM stdin;
8	\N	f	0	Welcome to Java Basics	TEXT	\N	1	2026-02-05 18:27:28.789327+00	2026-02-05 18:27:28.942116+00	0	/course/1/lesson/8	\N
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
3	ROLE_ADMIN
1	ROLE_STUDENT
2	ROLE_INSTRUCTOR
4	STUDENT
5	INSTRUCTOR
6	ADMIN
\.


--
-- Data for Name: section_blocks; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.section_blocks (id, block_type, enabled, order_index, section_id) FROM stdin;
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
1	1
2	1
2	3
3	4
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: educator
--

COPY public.users (id, email, password) FROM stdin;
1	test@example.com	$2a$10$r8gxSWVP4iaZA6IAAYoABu8xXHe6NKO92PQKZJKL4a6JCo..zlJlC
2	admin@test.com	$2a$10$j3FFcUALghbMvMlMWqYi/OOP6j0H3KWE.jV8e85VHQU8jsxstL3QS
3	student109998@test.com	$2a$10$eFjdI1E9TzH19dFgcVV0GOAU8EuEyYHLQbX5v3zmbd17VAF3dC9jK
\.


--
-- Name: courses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.courses_id_seq', 1, true);


--
-- Name: enrollments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.enrollments_id_seq', 1, false);


--
-- Name: hierarchy_nodes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.hierarchy_nodes_id_seq', 1, true);


--
-- Name: lesson_progress_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.lesson_progress_id_seq', 1, false);


--
-- Name: lessons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.lessons_id_seq', 8, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.roles_id_seq', 6, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.users_id_seq', 3, true);


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
-- Name: course_completions uk239kcs2781oon1ca4pre0y0vb; Type: CONSTRAINT; Schema: public; Owner: educator
--

ALTER TABLE ONLY public.course_completions
    ADD CONSTRAINT uk239kcs2781oon1ca4pre0y0vb UNIQUE (course_id, user_id);


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
-- Name: idx_lesson_course; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX idx_lesson_course ON public.lessons USING btree (course_id);


--
-- Name: idx_lesson_order; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX idx_lesson_order ON public.lessons USING btree (order_index);


--
-- Name: idx_lesson_type; Type: INDEX; Schema: public; Owner: educator
--

CREATE INDEX idx_lesson_type ON public.lessons USING btree (type);


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

\unrestrict BR6aszmnoulvxITpbF9jJve0Q2zdAEkOVv9ZEbAi2sSvMZR9eseMKtPd1BfH0Dv

