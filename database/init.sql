CREATE TABLE IF NOT EXISTS carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_carts_status
        CHECK (status IN ('ACTIVE', 'CHECKED_OUT', 'CANCELLED'))
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_carts_active_user
    ON carts (user_id)
    WHERE status = 'ACTIVE';

CREATE INDEX IF NOT EXISTS idx_carts_user_id
    ON carts (user_id);

CREATE INDEX IF NOT EXISTS idx_carts_status
    ON carts (status);
