# Estructura General del Proyecto

Este documento centraliza la documentacion de la estructura del proyecto `shopping_cart`. A diferencia del ejemplo de arquitectura monorepo mostrado en clase, en este caso el proyecto queda compuesto por un microservicio principal, un `API Gateway`, su frontend en `Vue`, la base de datos y la configuracion necesaria para Docker. Para el desarrollo del backend se utiliza `Spring Boot`.

## 1. Proposito de la Estructura

La finalidad de esta estructura es organizar claramente cada parte del sistema para que el desarrollo sea mas facil de entender, mantener y escalar. Aunque se trabaja con un unico microservicio, se conserva una organizacion tipo monorepo para separar responsabilidades y facilitar el trabajo por modulos.

## 2. Estructura Actual del Proyecto

```text
shopping_cart/
|-- README.md
|-- docker-compose.yml
|-- Doc/
|   |-- Changes/
|   |-- Diagramas/
|   `-- HU/
|-- gateway/                         (API Gateway - Spring Boot)
|   |-- src/
|   |-- pom.xml
|   `-- Dockerfile
|-- backend/                         (Shopping Cart Microservice - Spring Boot)
|   |-- src/
|   |-- pom.xml
|   `-- Dockerfile
|-- frontend/                        (Vue 3 + Vite User Interface)
|   |-- src/
|   |-- package.json
|   |-- vite.config.js
|   |-- .env
|   `-- Dockerfile
`-- database/                        (Database configuration)
    `-- init.sql
```

## 3. Descripcion de Cada Parte

### `README.md`

Archivo principal del proyecto. Incluye una vista general del sistema, instrucciones de ejecucion, tecnologias utilizadas y pasos de instalacion.

### `docker-compose.yml`

Permite levantar todos los servicios necesarios del proyecto en conjunto:

- Frontend
- API Gateway
- Backend
- Base de datos

Esto facilita las pruebas locales y el despliegue del entorno completo.

### `gateway/`

Esta carpeta contiene el `API Gateway` del proyecto, desarrollado con `Spring Boot`. Su funcion es actuar como punto de entrada principal para las solicitudes del frontend, centralizando el acceso al microservicio `shopping_cart`.

Entre sus responsabilidades estan:

- recibir las solicitudes entrantes del frontend
- redirigir las peticiones hacia el microservicio correspondiente
- facilitar una arquitectura mas ordenada y escalable
- servir como punto central de acceso para futuras ampliaciones del sistema

Dentro de esta carpeta se maneja una estructura tipica de Spring Boot:

- codigo fuente en `src/main/java`
- configuracion en `src/main/resources/application.yml`
- dependencias y construccion con `pom.xml`
- contenerizacion mediante `Dockerfile`

### `Doc/`

Carpeta destinada a reunir la documentacion del proyecto. Aqui se almacenan los archivos explicativos de cada fase del desarrollo para que cualquier persona pueda entender como se construyo el proyecto paso a paso.

### `backend/`

Esta carpeta contiene el microservicio principal de `shopping_cart`, desarrollado con `Spring Boot`. Aqui se construye toda la logica relacionada con el carrito de compras, incluyendo:

- gestion de productos agregados al carrito
- actualizacion de cantidades
- eliminacion de productos
- consulta del contenido del carrito
- calculo de totales
- conexion con la base de datos
- exposicion de endpoints para el frontend o para otros servicios

Dentro de esta carpeta tambien se maneja la estructura tipica de un proyecto Spring Boot:

- codigo fuente en `src/main/java`
- configuracion en `src/main/resources/application.yml`
- dependencias y construccion con `pom.xml`
- contenerizacion mediante `Dockerfile`

Como este proyecto tiene un solo microservicio, esta es la pieza central de la logica del sistema, mientras que el `API Gateway` es la puerta de entrada.

### `frontend/`

Esta carpeta contiene la interfaz visual del proyecto implementada en `Vue 3` con `Vite`. Su objetivo es permitir que el usuario interactue con el carrito de compras de forma sencilla, consultando productos agregados, actualizando cantidades, eliminando elementos y visualizando el total acumulado.

Actualmente el frontend incluye:

- cliente HTTP configurado para consumir el `gateway`
- componentes reutilizables del carrito
- estado global con `Pinia`
- pagina principal del carrito
- estilos base responsivos
- `Dockerfile` para ejecucion por `docker compose`

### `database/`

Esta carpeta esta destinada a la configuracion de la base de datos del proyecto. Aqui se incluyen:

- scripts de inicializacion
- creacion de tablas
- configuracion relacionada con la persistencia

Dado que el proyecto contempla la base de datos como una parte fundamental, esta carpeta ayuda a mantener separada la configuracion de persistencia respecto al backend y al frontend.

## 4. Observacion Importante

Aunque el modelo de referencia presentado en clase muestra varios microservicios, en este proyecto solo se implementa uno: el microservicio de `shopping_cart`. Sin embargo, se agrega un `API Gateway` para centralizar el acceso al backend y se mantiene una estructura ordenada de tipo monorepo para que el sistema sea mas entendible y para que en el futuro pueda ampliarse si se desea integrar nuevos servicios.

## 5. Beneficios de Esta Organizacion

- facilita la comprension general del proyecto
- separa claramente gateway, backend, frontend y base de datos
- permite documentar el desarrollo de forma ordenada
- mejora el mantenimiento del codigo
- hace mas sencillo el uso de Docker para levantar el entorno completo
- deja una base preparada para futuras ampliaciones

## 6. Conclusion

La estructura propuesta para `shopping_cart` adapta la idea de monorepo del curso a un caso mas simple y enfocado: un solo microservicio con `API Gateway`, frontend, base de datos y configuracion de despliegue. Esto permite mantener una organizacion profesional del proyecto sin perder claridad en el proceso de aprendizaje.

## 7. Estado Actual Implementado

Actualmente el proyecto ya no se encuentra solo en fase de propuesta. A nivel tecnico ya tiene implementado:

- `backend` con la `HU-001 - Crear carrito de compras`
- `backend` con la `HU-002 - Agregar producto al carrito`
- `backend` con la `HU-003 - Consultar carrito`
- `backend` con la `HU-004 - Actualizar cantidad de producto en el carrito`
- `backend` con la `HU-005 - Eliminar producto del carrito`
- `backend` con la `HU-006 - Calcular total del carrito`
- `frontend` con la `HU-007 - Visualizar y gestionar el carrito desde frontend por medio del API Gateway`
- `gateway` consumiendo el backend a traves de `POST /api/v1/carts`
- `gateway` consumiendo el backend a traves de `POST /api/v1/carts/{cartId}/items`
- `gateway` consumiendo el backend a traves de `PUT /api/v1/carts/{cartId}/items/{itemId}`
- `gateway` consumiendo el backend a traves de `DELETE /api/v1/carts/{cartId}/items/{itemId}`
- `gateway` consumiendo el backend a traves de `GET /api/v1/carts/{cartId}`
- `gateway` consumiendo el backend a traves de `GET /api/v1/carts/{cartId}/total`
- `docker-compose.yml` para levantar `frontend`, `postgres`, `backend` y `gateway`
- `docker-compose.yml` con imagenes nombradas para `frontend`, `backend` y `gateway`
- `docker-compose.yml` con `healthcheck` para coordinar el arranque entre servicios
- `database/init.sql` para crear las tablas `carts` y `cart_items`
- configuracion de PostgreSQL en `localhost:5020`

Estructura real actualmente usada:

```text
shopping_cart/
|-- README.md
|-- docker-compose.yml
|-- Doc/
|   |-- Changes/
|   |-- Diagramas/
|   `-- HU/
|-- backend/
|   |-- src/test/java/shopping_cart/backend/service/
|   |-- src/main/java/shopping_cart/backend/controller/
|   |-- src/main/java/shopping_cart/backend/dto/
|   |-- src/main/java/shopping_cart/backend/entity/
|   |-- src/main/java/shopping_cart/backend/exception/
|   |-- src/main/java/shopping_cart/backend/repository/
|   |-- src/main/java/shopping_cart/backend/service/
|   |-- src/main/resources/application.yaml
|   `-- Dockerfile
|-- gateway/
|   |-- src/test/java/shopping_cart/gateway/controller/
|   |-- src/main/java/shopping_cart/gateway/config/
|   |-- src/main/java/shopping_cart/gateway/controller/
|   |-- src/main/java/shopping_cart/gateway/dto/
|   |-- src/main/java/shopping_cart/gateway/service/
|   |-- src/main/resources/application.yaml
|   `-- Dockerfile
|-- frontend/
|   |-- src/api/
|   |-- src/assets/styles/
|   |-- src/components/cart/
|   |-- src/composables/
|   |-- src/layouts/
|   |-- src/pages/
|   |-- src/router/
|   |-- src/services/
|   |-- src/stores/
|   |-- src/utils/
|   |-- package.json
|   |-- vite.config.js
|   `-- Dockerfile
|-- database/
|   `-- init.sql
`-- proyecto listo para ejecutarse con Docker Compose
```

Endpoints actualmente disponibles a traves del `gateway`:

- `POST /api/v1/carts`
- `POST /api/v1/carts/{cartId}/items`
- `PUT /api/v1/carts/{cartId}/items/{itemId}`
- `DELETE /api/v1/carts/{cartId}/items/{itemId}`
- `GET /api/v1/carts/{cartId}`
- `GET /api/v1/carts/{cartId}/total`

Capacidades actuales del `backend`:

- crear o reutilizar un carrito activo por usuario
- agregar productos al carrito existente
- actualizar la cantidad acumulada si el producto ya estaba agregado
- actualizar explicitamente la cantidad de un item existente mediante `itemId`
- eliminar explicitamente un item existente mediante `itemId`
- consultar el carrito con sus items y el total acumulado
- consultar un resumen especializado del total del carrito con `totalItems` y `totalAmount`

Archivos representativos agregados o consolidados hasta la `HU-006`:

- `backend/src/main/java/shopping_cart/backend/dto/UpdateCartItemQuantityRequestDTO.java`
- `backend/src/main/java/shopping_cart/backend/dto/DeleteCartItemResponseDTO.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartTotalResponseDTO.java`
- `gateway/src/main/java/shopping_cart/gateway/dto/UpdateCartItemQuantityRequestDTO.java`
- `backend/src/main/java/shopping_cart/backend/controller/CartController.java`
- `backend/src/main/java/shopping_cart/backend/service/CartServiceImpl.java`
- `gateway/src/main/java/shopping_cart/gateway/controller/CartGatewayController.java`
- `gateway/src/main/java/shopping_cart/gateway/service/CartGatewayService.java`
- `backend/src/test/java/shopping_cart/backend/service/CartServiceImplTest.java`
- `gateway/src/test/java/shopping_cart/gateway/controller/CartGatewayControllerTest.java`

Archivos representativos agregados con la `HU-007`:

- `frontend/src/api/httpClient.js`
- `frontend/src/services/cartService.js`
- `frontend/src/stores/cartStore.js`
- `frontend/src/composables/useCart.js`
- `frontend/src/pages/CartPage.vue`
- `frontend/src/components/cart/CartHeader.vue`
- `frontend/src/components/cart/AddProductForm.vue`
- `frontend/src/components/cart/CartItemsTable.vue`
- `frontend/src/components/cart/CartSummary.vue`
- `frontend/src/assets/styles/main.css`
- `frontend/Dockerfile`
