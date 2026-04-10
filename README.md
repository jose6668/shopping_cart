# shopping_cart

Proyecto de carrito de compras con arquitectura basada en:
- `backend` Spring Boot para la logica del microservicio
- `gateway` Spring Boot como punto de entrada para el frontend
- `PostgreSQL` como base de datos
- `Docker Compose` para levantar el entorno local

## Estado actual

Actualmente se encuentra implementada la `HU-001 - Crear carrito de compras`.

Flujo disponible:
- el cliente consume el `gateway`
- el `gateway` redirige la solicitud al `backend`
- el `backend` crea o reutiliza el carrito activo
- PostgreSQL persiste la informacion

## Servicios y puertos

- `gateway`: `http://localhost:8080`
- `backend`: `http://localhost:8081`
- `postgres`: `localhost:5020`

## Endpoint implementado

- `POST /api/v1/carts`

Ejemplo de request:

```json
{
  "userId": 1
}
```

## Levantar el proyecto

```bash
docker compose up --build
```

Si deseas reiniciar completamente la base de datos e inicializar de nuevo el script SQL:

```bash
docker compose down -v
docker compose up --build
```

## Base de datos

La base de datos usada es `shopping_cart_db`.

El contenedor de PostgreSQL:
- crea la base mediante `POSTGRES_DB`
- ejecuta el script `database/init.sql` para crear la tabla `carts`

## Prueba rapida

Puedes probar la HU desde Postman o con `curl` consumiendo el gateway:

```bash
curl -X POST http://localhost:8080/api/v1/carts ^
  -H "Content-Type: application/json" ^
  -d "{\"userId\":1}"
```
