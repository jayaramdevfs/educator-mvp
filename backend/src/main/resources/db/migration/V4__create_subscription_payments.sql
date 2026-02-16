CREATE TABLE subscription_payments (
                                       id UUID PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       plan_id UUID NOT NULL,
                                       amount NUMERIC(19,2) NOT NULL,
                                       status VARCHAR(20) NOT NULL,
                                       provider VARCHAR(255),
                                       provider_payment_id VARCHAR(255),
                                       created_at TIMESTAMP,
                                       completed_at TIMESTAMP
);

CREATE INDEX idx_subscription_payments_user_id
    ON subscription_payments(user_id);

CREATE INDEX idx_subscription_payments_plan_id
    ON subscription_payments(plan_id);

CREATE INDEX idx_subscription_payments_provider_payment_id
    ON subscription_payments(provider_payment_id);
