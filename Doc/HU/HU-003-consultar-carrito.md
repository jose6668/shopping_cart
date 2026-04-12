# HU-003: Consultar carrito

**Como** usuario del sistema  
**Quiero** consultar el contenido de mi carrito  
**Para** visualizar los productos seleccionados, sus cantidades y el total acumulado

## Criterios de Aceptacion

- [ ] El sistema permite consultar un carrito existente
- [ ] La respuesta incluye los productos agregados al carrito
- [ ] La respuesta muestra cantidades, precio unitario, subtotal y total
- [ ] Si el carrito no existe, el sistema responde con un mensaje adecuado
- [ ] La informacion retornada coincide con los datos almacenados en la base de datos

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente:** backend
- **Endpoint:** `GET /api/v1/carts/{cartId}`
- **Base de datos:** relacional
- **Story Points:** 3

## Labels

`backend` `shopping-cart` `query` `sprint-1`
