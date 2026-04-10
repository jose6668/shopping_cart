# HU-001: Crear carrito de compras

**Como** usuario del sistema  
**Quiero** que se cree un carrito de compras asociado a mi identificador  
**Para** poder comenzar a agregar productos y gestionar una futura compra

## Criterios de Aceptacion

- [x] El sistema permite crear un carrito para un usuario
- [x] El carrito queda asociado a un `userId`
- [x] El carrito se almacena en la base de datos
- [x] El sistema retorna la informacion basica del carrito creado
- [x] Si el usuario ya tiene un carrito activo, el sistema evita crear duplicados innecesarios

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente principal:** backend
- **Entrada oficial:** gateway
- **Endpoint:** `POST /api/v1/carts`
- **Base de datos:** PostgreSQL
- **Puerto PostgreSQL:** `5020`
- **Story Points:** 3

## Estado actual

La HU ya se encuentra implementada.

Componentes implementados:
- `backend` con logica de creacion y reutilizacion de carrito activo
- `gateway` como punto de entrada para el consumo del frontend
- `PostgreSQL` con tabla `carts`
- `docker-compose.yml` para levantar el entorno

Rutas disponibles:
- `gateway`: `POST http://localhost:8080/api/v1/carts`
- `backend`: `POST http://localhost:8081/api/v1/carts`

Comportamiento implementado:
- si el usuario no tiene carrito activo, se crea uno nuevo y responde `201 Created`
- si el usuario ya tiene un carrito activo, se retorna el existente y responde `200 OK`

## Labels

`backend` `gateway` `shopping-cart` `cart` `sprint-1`
