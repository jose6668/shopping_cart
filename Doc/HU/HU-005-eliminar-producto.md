# HU-005: Eliminar producto del carrito

**Como** usuario del sistema  
**Quiero** eliminar un producto de mi carrito  
**Para** retirar elementos que ya no deseo comprar

## Criterios de Aceptacion

- [x] El sistema permite eliminar un producto del carrito
- [x] El item eliminado deja de aparecer en la consulta del carrito
- [x] El total del carrito se recalcula despues de la eliminacion
- [x] Si el producto no existe en el carrito, el sistema responde adecuadamente
- [x] La eliminacion queda reflejada en la base de datos

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente:** backend
- **Endpoint:** `DELETE /api/v1/carts/{cartId}/items/{itemId}`
- **Base de datos:** relacional
- **Story Points:** 3

## Labels

`backend` `shopping-cart` `delete` `sprint-1`
