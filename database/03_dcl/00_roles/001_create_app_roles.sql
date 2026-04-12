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
