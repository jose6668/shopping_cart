# HU-007: Visualizar y gestionar el carrito desde el frontend a traves del API Gateway

**Como** usuario del sistema  
**Quiero** interactuar con mi carrito desde una interfaz visual conectada por medio del API Gateway  
**Para** gestionar mis productos de forma sencilla y centralizada

## Criterios de Aceptacion

- [x] El frontend permite visualizar el contenido del carrito
- [x] El frontend permite agregar, actualizar y eliminar productos
- [x] Las solicitudes del frontend pasan por el API Gateway
- [x] El API Gateway redirige correctamente las peticiones al microservicio `shopping_cart`
- [x] La informacion mostrada en la interfaz coincide con la respuesta del backend

## Detalles Tecnicos

- **Microservicio:** shopping-cart
- **Framework frontend:** Vue 3 + Vite
- **Componentes:** frontend, gateway, backend
- **Endpoints:** rutas expuestas a traves del API Gateway en `http://localhost:8080`
- **Puerto frontend:** `http://localhost:5173`
- **Despliegue:** compatible con `docker compose up --build`
- **Base de datos:** relacional
- **Story Points:** 8

## Labels

`frontend` `gateway` `shopping-cart` `integration` `sprint-1`
