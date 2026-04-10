# HU-001: Crear carrito de compras

**Como** usuario del sistema  
**Quiero** que se cree un carrito de compras asociado a mi identificador  
**Para** poder comenzar a agregar productos y gestionar una futura compra

## Criterios de Aceptacion

- [ ] El sistema permite crear un carrito para un usuario
- [ ] El carrito queda asociado a un `userId`
- [ ] El carrito se almacena en la base de datos
- [ ] El sistema retorna la informacion basica del carrito creado
- [ ] Si el usuario ya tiene un carrito activo, el sistema evita crear duplicados innecesarios

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Componente:** backend
- **Endpoint:** `POST /api/v1/carts`
- **Base de datos:** relacional
- **Story Points:** 3

## Labels

`backend` `shopping-cart` `cart` `sprint-1`
