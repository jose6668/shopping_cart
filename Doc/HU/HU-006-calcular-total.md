# HU-006: Calcular total del carrito

**Como** usuario del sistema  
**Quiero** obtener el total actualizado de mi carrito  
**Para** conocer el valor acumulado de los productos seleccionados

## Criterios de Aceptacion

- [x] El sistema calcula el total del carrito a partir de los subtotales de cada item
- [x] El total se actualiza al agregar, modificar o eliminar productos
- [x] La respuesta incluye el numero total de items y el monto total
- [x] El calculo se realiza de forma consistente con los datos persistidos
- [x] El resultado puede ser consultado desde el backend y mostrado en el frontend

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente:** backend y gateway
- **Endpoint backend:** `GET /api/v1/carts/{cartId}/total`
- **Endpoint gateway:** `GET /api/v1/carts/{cartId}/total`
- **Base de datos:** relacional
- **Story Points:** 3

## Labels

`backend` `shopping-cart` `total` `sprint-1`
