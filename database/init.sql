-- Bootstrap legacy para el docker-compose principal del monorepo.
-- La fuente de verdad del componente database es Liquibase:
-- database/changelog-master.yaml

CREATE TABLE IF NOT EXISTS carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_carts_status
        CHECK (status IN ('ACTIVE', 'CHECKED_OUT', 'CANCELLED'))
);

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

DO
$$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'shopping_cart_admin') THEN
        CREATE ROLE shopping_cart_admin LOGIN PASSWORD 'admin123';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'shopping_cart_employee') THEN
        CREATE ROLE shopping_cart_employee LOGIN PASSWORD 'empleado123';
    END IF;
END
$$;

GRANT CONNECT ON DATABASE shopping_cart_db TO shopping_cart_admin;
GRANT CONNECT ON DATABASE shopping_cart_db TO shopping_cart_employee;

GRANT USAGE ON SCHEMA public TO shopping_cart_admin;
GRANT USAGE ON SCHEMA public TO shopping_cart_employee;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO shopping_cart_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO shopping_cart_admin;

GRANT SELECT, INSERT ON ALL TABLES IN SCHEMA public TO shopping_cart_employee;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO shopping_cart_employee;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL PRIVILEGES ON TABLES TO shopping_cart_admin;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT ALL PRIVILEGES ON SEQUENCES TO shopping_cart_admin;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT ON TABLES TO shopping_cart_employee;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT USAGE, SELECT ON SEQUENCES TO shopping_cart_employee;
