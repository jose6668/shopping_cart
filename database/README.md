# Shopping Cart DB

Repositorio de base de datos para `shopping_cart` versionado con `Liquibase` y `PostgreSQL`.

## Alcance actual
Este componente administra hoy:
- tablas `carts` y `cart_items`
- indices del dominio carrito
- usuarios de base de datos para aplicacion
- grants basicos para administracion y operacion

No incluye aun:
- vistas
- funciones
- procedimientos
- triggers
- datos semilla
- politicas avanzadas

## Estructura
```text
database/
|-- changelog-master.yaml
|-- 01_ddl/
|   |-- changelog.yaml
|   |-- 00_extensions/
|   |-- 01_schemas/
|   |-- 02_types/
|   |-- 03_tables/
|   |-- 04_views/
|   |-- 05_materialized_views/
|   |-- 06_functions/
|   |-- 07_procedures/
|   |-- 08_triggers/
|   `-- 09_indexes/
|-- 02_dml/
|-- 03_dcl/
|-- 04_tcl/
|-- 05_rollbacks/
|-- docker/
|-- docs/
|-- scripts/
|-- docker-compose.yml
|-- .env.example
|-- liquibase.properties.example
`-- init.sql
```

## Arquitectura de capas
- `01_ddl`: evolucion estructural del modelo fisico
- `02_dml`: cambios de datos versionados
- `03_dcl`: roles, grants y control de acceso
- `04_tcl`: operaciones transaccionales excepcionales
- `05_rollbacks`: reversas organizadas por capa

## Capa activa hoy
Actualmente el despliegue funcional usa:
- `01_ddl/03_tables`
- `01_ddl/09_indexes`
- `03_dcl/00_roles`
- `03_dcl/01_grants`

## Modelo actual
### Tabla `carts`
- `id`
- `user_id`
- `status`
- `created_at`
- `updated_at`

### Tabla `cart_items`
- `id`
- `cart_id`
- `product_id`
- `name`
- `quantity`
- `price`
- `subtotal`
- `created_at`
- `updated_at`

## Seguridad actual
Se crean dos usuarios de base de datos:
- `shopping_cart_admin`
  - permisos completos sobre tablas y secuencias
- `shopping_cart_employee`
  - permisos de consulta e insercion sobre tablas
  - permisos de uso y lectura sobre secuencias

## Uso rapido con Docker
1. Copia `.env.example` como `.env` si quieres cambiar credenciales o puerto.
2. Levanta PostgreSQL:

```bash
docker compose -p shopping-cart-db up -d postgres
```

3. Construye el runner de Liquibase:

```bash
docker compose -p shopping-cart-db --profile tooling build liquibase
```

4. Valida los changelogs:

```bash
docker compose -p shopping-cart-db --profile tooling run --rm liquibase validate
```

5. Aplica cambios:

```bash
docker compose -p shopping-cart-db --profile tooling run --rm liquibase update
```

## Nota de compatibilidad
`init.sql` se conserva como bootstrap legacy para el `docker-compose` principal del monorepo, pero la fuente de verdad del componente pasa a ser `changelog-master.yaml` y los `changeSet` asociados.
