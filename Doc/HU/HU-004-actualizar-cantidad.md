# HU-004: Actualizar cantidad de producto en el carrito

**Como** usuario del sistema  
**Quiero** modificar la cantidad de un producto dentro de mi carrito  
**Para** ajustar mi seleccion de compra segun mis necesidades

## Criterios de Aceptacion

- [x] El sistema permite actualizar la cantidad de un producto existente en el carrito
- [x] La nueva cantidad debe ser valida y mayor que cero
- [x] El subtotal del producto se recalcula automaticamente
- [x] El total general del carrito se actualiza correctamente
- [x] El cambio queda persistido en la base de datos

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente principal:** backend
- **Entrada oficial:** gateway
- **Endpoint:** `PUT /api/v1/carts/{cartId}/items/{itemId}`
- **Base de datos:** PostgreSQL
- **Puerto PostgreSQL:** `5020`
- **Story Points:** 5

## Estado actual

La HU ya se encuentra implementada.

Componentes implementados:
- `backend` con logica para actualizar la cantidad de un item del carrito
- `gateway` como punto de entrada para exponer la operacion al frontend
- `PostgreSQL` para persistir la actualizacion en `cart_items`

Rutas disponibles:
- `gateway`: `PUT http://localhost:8080/api/v1/carts/{cartId}/items/{itemId}`
- `backend`: `PUT http://localhost:8081/api/v1/carts/{cartId}/items/{itemId}`

Comportamiento implementado:
- valida que el carrito exista
- valida que el carrito este en estado `ACTIVE`
- valida que el item exista
- valida que el item pertenezca al carrito indicado
- valida que `quantity` sea mayor a `0`
- recalcula automaticamente el `subtotal`
- mantiene actualizado el `total` del carrito al consultar su detalle

## Labels

`backend` `gateway` `shopping-cart` `update` `sprint-1`
