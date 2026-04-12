CREATE UNIQUE INDEX IF NOT EXISTS uq_carts_active_user
    ON carts (user_id)
    WHERE status = 'ACTIVE';

CREATE INDEX IF NOT EXISTS idx_carts_user_id
    ON carts (user_id);

CREATE INDEX IF NOT EXISTS idx_carts_status
    ON carts (status);

CREATE INDEX IF NOT EXISTS idx_cart_items_cart_id
    ON cart_items (cart_id);

CREATE INDEX IF NOT EXISTS idx_cart_items_product_id
    ON cart_items (product_id);
