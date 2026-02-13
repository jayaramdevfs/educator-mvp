-- Add missing column introduced in Sprint 9 for deterministic question review order.
ALTER TABLE public.exam_attempts
    ADD COLUMN IF NOT EXISTS question_order character varying(4000);
