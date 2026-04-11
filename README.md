# shopping_cart

Proyecto de carrito de compras con arquitectura basada en:
- `frontend` Vue 3 para la interfaz visual del carrito
- `backend` Spring Boot para la logica del microservicio
- `gateway` Spring Boot como punto de entrada para el frontend
- `PostgreSQL` como base de datos
- `Docker Compose` para levantar el entorno local

## Estado actual

Actualmente se encuentran implementadas:
- `HU-001 - Crear carrito de compras`
- `HU-002 - Agregar producto al carrito`
- `HU-003 - Consultar carrito`
- `HU-004 - Actualizar cantidad de producto en el carrito`
- `HU-005 - Eliminar producto del carrito`
- `HU-006 - Calcular total del carrito`
- `HU-007 - Visualizar y gestionar el carrito desde frontend por medio del API Gateway`

Flujo disponible:
- el cliente consume el `gateway`
- el `gateway` redirige la solicitud al `backend`
- el `backend` crea o reutiliza el carrito activo
- el `backend` permite agregar productos al carrito existente
- el `backend` permite actualizar la cantidad de un item existente del carrito
- el `backend` permite eliminar un item existente del carrito
- el `backend` permite consultar el carrito con sus items y el total acumulado
- el `backend` permite consultar un resumen especializado del total del carrito
- si el producto ya existe en el carrito, actualiza la cantidad
- PostgreSQL persiste la informacion del carrito y sus items

## Servicios y puertos

- `frontend`: `http://localhost:5173`
- `gateway`: `http://localhost:8080`
- `backend`: `http://localhost:8081`
- `postgres`: `localhost:5020`

## Endpoint implementado

- `POST /api/v1/carts`
- `POST /api/v1/carts/{cartId}/items`
- `PUT /api/v1/carts/{cartId}/items/{itemId}`
- `DELETE /api/v1/carts/{cartId}/items/{itemId}`
- `GET /api/v1/carts/{cartId}`
- `GET /api/v1/carts/{cartId}/total`

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

Con este comando se levantan:
- `frontend`
- `gateway`
- `backend`
- `postgres`

El frontend Vue queda disponible en:

```bash
http://localhost:5173
```

Para levantarlo en segundo plano y reconstruir imagenes cuando haya cambios:

```bash
docker compose up -d --build
```

El archivo `docker-compose.yml` actual:
- incluye el `frontend` en un contenedor propio servido con Nginx
- conserva la base de datos mediante el volumen `postgres_data`
- asigna nombre fijo a las imagenes de `frontend`, `backend` y `gateway`
- agrega `healthcheck` para coordinar mejor el arranque de servicios

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

Y consultar el detalle del carrito:

```bash
curl -X GET http://localhost:8080/api/v1/carts/1
```

Tambien puedes consultar el resumen especializado del total del carrito:

```bash
curl -X GET http://localhost:8080/api/v1/carts/1/total
```

Tambien puedes actualizar la cantidad de un item existente:

```bash
curl -X PUT http://localhost:8080/api/v1/carts/1/items/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"quantity\":4}"
```

Y tambien puedes eliminar un item existente del carrito:

```bash
curl -X DELETE http://localhost:8080/api/v1/carts/1/items/1
```

Despues puedes volver a consultar el carrito para verificar que el item ya no aparece y que el total fue recalculado:

```bash
curl -X GET http://localhost:8080/api/v1/carts/1
```

O consultar directamente el nuevo resumen de total:

```bash
curl -X GET http://localhost:8080/api/v1/carts/1/total
```
