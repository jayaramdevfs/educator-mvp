-- =====================================================
-- SUBSCRIPTION PLANS
-- =====================================================

CREATE TABLE subscription_plans (
                                    id UUID PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    price NUMERIC(19,2) NOT NULL,
                                    duration_days INTEGER NOT NULL,
                                    enabled BOOLEAN NOT NULL,
                                    created_at TIMESTAMP,
                                    updated_at TIMESTAMP
);

-- =====================================================
-- SUBSCRIPTION PLAN COURSES
-- =====================================================

CREATE TABLE subscription_plan_courses (
                                           id UUID PRIMARY KEY,
                                           plan_id UUID NOT NULL,
                                           course_id BIGINT NOT NULL,
                                           created_at TIMESTAMP
);

CREATE INDEX idx_spc_plan_id
    ON subscription_plan_courses(plan_id);

-- =====================================================
-- SUBSCRIPTION PLAN EXAMS
-- =====================================================

CREATE TABLE subscription_plan_exams (
                                         id UUID PRIMARY KEY,
                                         plan_id UUID NOT NULL,
                                         exam_id UUID NOT NULL,
                                         created_at TIMESTAMP
);

CREATE INDEX idx_spe_plan_id
    ON subscription_plan_exams(plan_id);

-- =====================================================
-- USER SUBSCRIPTIONS
-- =====================================================

CREATE TABLE user_subscriptions (
                                    id UUID PRIMARY KEY,
                                    user_id BIGINT NOT NULL,
                                    plan_id UUID NOT NULL,
                                    start_at TIMESTAMP NOT NULL,
                                    expires_at TIMESTAMP NOT NULL,
                                    status VARCHAR(50) NOT NULL,
                                    created_at TIMESTAMP
);

CREATE INDEX idx_user_subscriptions_user_id
    ON user_subscriptions(user_id);
