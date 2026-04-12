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
