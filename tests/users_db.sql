--
-- PostgreSQL database dump
--

-- Dumped from database version 11.22
-- Dumped by pg_dump version 11.22

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
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, name, mail, password, authority) FROM stdin;
39	test	test1@example.com	B0gPuehbk5avBvAGzxyVAkryUxxl+1Bc+9Ct0eLzFXM=	1
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 40, true);


--
-- PostgreSQL database dump complete
--

