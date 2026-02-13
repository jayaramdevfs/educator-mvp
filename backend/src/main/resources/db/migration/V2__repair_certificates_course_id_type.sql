-- Repair legacy local schemas where certificates.course_id was created as UUID.
-- Current domain model requires certificates.course_id as BIGINT (Course.id).
DO
$$
DECLARE
    current_type text;
    constraint_row record;
BEGIN
    IF to_regclass('public.certificates') IS NULL THEN
        RETURN;
    END IF;

    SELECT data_type
    INTO current_type
    FROM information_schema.columns
    WHERE table_schema = 'public'
      AND table_name = 'certificates'
      AND column_name = 'course_id';

    IF current_type = 'uuid' THEN
        -- Legacy UUID course IDs cannot be converted to BIGINT; keep schema consistent.
        TRUNCATE TABLE public.certificates;

        FOR constraint_row IN
            SELECT c.conname
            FROM pg_constraint c
            JOIN pg_class t ON t.oid = c.conrelid
            JOIN pg_namespace n ON n.oid = t.relnamespace
            WHERE n.nspname = 'public'
              AND t.relname = 'certificates'
              AND c.contype IN ('u', 'f')
              AND pg_get_constraintdef(c.oid) ILIKE '%course_id%'
        LOOP
            EXECUTE format('ALTER TABLE public.certificates DROP CONSTRAINT IF EXISTS %I', constraint_row.conname);
        END LOOP;

        ALTER TABLE public.certificates DROP COLUMN course_id;
        ALTER TABLE public.certificates ADD COLUMN course_id bigint NOT NULL;

        ALTER TABLE public.certificates
            ADD CONSTRAINT uki1uki0pqbx17xxvpwf1ocq0e UNIQUE (course_id, user_id);
    END IF;
END
$$;
