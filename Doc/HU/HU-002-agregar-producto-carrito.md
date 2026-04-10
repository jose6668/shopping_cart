# HU-002: Agregar producto al carrito

**Como** usuario del sistema  
**Quiero** agregar un producto a mi carrito de compras  
**Para** mantener una lista de productos seleccionados antes de confirmar la compra

## Criterios de Aceptacion

- [ ] El sistema permite agregar un producto al carrito
- [ ] El producto agregado incluye identificador, nombre, cantidad y precio
- [ ] Si el producto ya existe en el carrito, el sistema actualiza la cantidad
- [ ] El subtotal del item se calcula correctamente
- [ ] La informacion se guarda en la base de datos

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente:** backend
- **Endpoint:** `POST /api/v1/carts/{cartId}/items`
- **Base de datos:** relacional
- **Story Points:** 5

## Labels

`backend` `shopping-cart` `cart-item` `sprint-1`
