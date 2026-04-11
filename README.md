# shopping_cart

Proyecto de carrito de compras con arquitectura basada en:
- `backend` Spring Boot para la logica del microservicio
- `gateway` Spring Boot como punto de entrada para el frontend
- `PostgreSQL` como base de datos
- `Docker Compose` para levantar el entorno local

## Estado actual

Actualmente se encuentran implementadas:
- `HU-001 - Crear carrito de compras`
- `HU-002 - Agregar producto al carrito`

Flujo disponible:
- el cliente consume el `gateway`
- el `gateway` redirige la solicitud al `backend`
- el `backend` crea o reutiliza el carrito activo
- el `backend` permite agregar productos al carrito existente
- si el producto ya existe en el carrito, actualiza la cantidad
- PostgreSQL persiste la informacion del carrito y sus items

## Servicios y puertos

- `gateway`: `http://localhost:8080`
- `backend`: `http://localhost:8081`
- `postgres`: `localhost:5020`

## Endpoint implementado

- `POST /api/v1/carts`
- `POST /api/v1/carts/{cartId}/items`

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
- ejecuta el script `database/init.sql` para crear las tablas `carts` y `cart_items`

## Prueba rapida

Puedes probar la HU desde Postman o con `curl` consumiendo el gateway:

```bash
curl -X POST http://localhost:8080/api/v1/carts ^
  -H "Content-Type: application/json" ^
  -d "{\"userId\":1}"
```

Luego puedes agregar un producto a un carrito existente:

```bash
curl -X POST http://localhost:8080/api/v1/carts/1/items ^
  -H "Content-Type: application/json" ^
  -d "{\"productId\":1,\"name\":\"Mouse Logitech G203\",\"quantity\":2,\"price\":85000}"
```
