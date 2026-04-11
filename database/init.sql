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

CREATE TABLE IF NOT EXISTS cart_items (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    CONSTRAINT chk_cart_items_quantity
        CHECK (quantity > 0),
    CONSTRAINT chk_cart_items_price
        CHECK (price >= 0),
    CONSTRAINT chk_cart_items_subtotal
        CHECK (subtotal >= 0),
    CONSTRAINT uq_cart_items_cart_product
        UNIQUE (cart_id, product_id)
);

CREATE INDEX IF NOT EXISTS idx_cart_items_cart_id
    ON cart_items (cart_id);

CREATE INDEX IF NOT EXISTS idx_cart_items_product_id
    ON cart_items (product_id);
