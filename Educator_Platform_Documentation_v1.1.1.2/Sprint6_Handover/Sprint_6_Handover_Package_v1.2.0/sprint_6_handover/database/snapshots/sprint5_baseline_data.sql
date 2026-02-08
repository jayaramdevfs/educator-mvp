--
-- PostgreSQL database dump
--

\restrict 3ELGfRpUgiGOaYHLcjOMYSRlWhKZZSeEntiRGgI5fuaToF5N0Db2BHaeoLiYPve

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

--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: educator
--

INSERT INTO public.courses (id, created_at, created_by_role, description_en, difficulty, estimated_duration_minutes, is_archived, is_deleted, language_code, sort_order, status, title_en, updated_at, version, hierarchy_node_id) VALUES (1, '2026-02-05 16:24:47.573691+00', 'ADMIN', 'Beginner Java Course', 'BEGINNER', 120, false, false, 'en', 0, 'DRAFT', 'Java Basics', '2026-02-05 16:24:47.573691+00', 0, 1);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: educator
--

INSERT INTO public.users (id, email, password) VALUES (1, 'test@example.com', '$2a$10$r8gxSWVP4iaZA6IAAYoABu8xXHe6NKO92PQKZJKL4a6JCo..zlJlC');
INSERT INTO public.users (id, email, password) VALUES (2, 'admin@test.com', '$2a$10$j3FFcUALghbMvMlMWqYi/OOP6j0H3KWE.jV8e85VHQU8jsxstL3QS');


--
-- Data for Name: enrollments; Type: TABLE DATA; Schema: public; Owner: educator
--



--
-- Data for Name: lessons; Type: TABLE DATA; Schema: public; Owner: educator
--

INSERT INTO public.lessons (id, document_url, is_deleted, order_index, text_content, type, video_url, course_id, created_at, updated_at, depth_level, path, parent_lesson_id) VALUES (8, NULL, false, 0, 'Welcome to Java Basics', 'TEXT', NULL, 1, '2026-02-05 18:27:28.789327+00', '2026-02-05 18:27:28.942116+00', 0, '/course/1/lesson/8', NULL);


--
-- Data for Name: lesson_progress; Type: TABLE DATA; Schema: public; Owner: educator
--



--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: educator
--

INSERT INTO public.roles (id, name) VALUES (3, 'ROLE_ADMIN');
INSERT INTO public.roles (id, name) VALUES (1, 'ROLE_STUDENT');
INSERT INTO public.roles (id, name) VALUES (2, 'ROLE_INSTRUCTOR');
INSERT INTO public.roles (id, name) VALUES (4, 'STUDENT');
INSERT INTO public.roles (id, name) VALUES (5, 'INSTRUCTOR');
INSERT INTO public.roles (id, name) VALUES (6, 'ADMIN');


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: educator
--

INSERT INTO public.user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO public.user_roles (user_id, role_id) VALUES (2, 3);


--
-- Name: courses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.courses_id_seq', 1, true);


--
-- Name: enrollments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: educator
--

SELECT pg_catalog.setval('public.enrollments_id_seq', 1, false);


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

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- PostgreSQL database dump complete
--

\unrestrict 3ELGfRpUgiGOaYHLcjOMYSRlWhKZZSeEntiRGgI5fuaToF5N0Db2BHaeoLiYPve

