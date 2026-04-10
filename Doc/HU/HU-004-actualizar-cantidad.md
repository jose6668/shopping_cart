# HU-004: Actualizar cantidad de producto en el carrito

**Como** usuario del sistema  
**Quiero** modificar la cantidad de un producto dentro de mi carrito  
**Para** ajustar mi seleccion de compra segun mis necesidades

## Criterios de Aceptacion

- [ ] El sistema permite actualizar la cantidad de un producto existente en el carrito
- [ ] La nueva cantidad debe ser valida y mayor que cero
- [ ] El subtotal del producto se recalcula automaticamente
- [ ] El total general del carrito se actualiza correctamente
- [ ] El cambio queda persistido en la base de datos

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente:** backend
- **Endpoint:** `PUT /api/v1/carts/{cartId}/items/{itemId}`
- **Base de datos:** relacional
- **Story Points:** 5

## Labels

`backend` `shopping-cart` `update` `sprint-1`
