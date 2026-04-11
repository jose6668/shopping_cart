# Estructura General del Proyecto

Este documento centraliza la documentacion de la estructura propuesta para el proyecto `shopping_cart`. A diferencia del ejemplo de arquitectura monorepo mostrado en clase, en este caso el proyecto estara compuesto por un solo microservicio principal, un `API Gateway`, su frontend, la base de datos y la configuracion necesaria para Docker. Para el desarrollo del backend se utilizara el framework `Spring Boot`.

## 1. Proposito de la Estructura

La finalidad de esta estructura es organizar claramente cada parte del sistema para que el desarrollo sea mas facil de entender, mantener y escalar. Aunque se trabajara con un unico microservicio, se conservara una organizacion tipo monorepo para separar responsabilidades y facilitar el trabajo por modulos.

## 2. Estructura Propuesta del Proyecto

```text
shopping_cart/
├── README.md
├── docker-compose.yml
├── Doc/
│   ├── introduccion.md
│   ├── estructura-proyecto.md
│   └── diagrama-contexto.md
├── gateway/                         (API Gateway - Spring Boot)
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.yml
│   ├── pom.xml
│   └── Dockerfile
├── backend/                         (Shopping Cart Microservice - Spring Boot)
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.yml
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                        (User Interface)
│   ├── src/
│   ├── package.json
│   └── Dockerfile
└── database/                        (Database configuration)
    ├── init/
    │   └── init.sql
    └── README.md
```

## 3. Descripcion de Cada Parte

### `README.md`

Archivo principal del proyecto. Aqui se incluira una vista general del sistema, instrucciones de ejecucion, tecnologias utilizadas y pasos de instalacion.

### `docker-compose.yml`

Permitira levantar todos los servicios necesarios del proyecto en conjunto, por ejemplo:

- API Gateway
- Backend
- Frontend
- Base de datos

Esto facilitara las pruebas locales y el despliegue del entorno completo.

### `gateway/`

Esta carpeta contendra el `API Gateway` del proyecto, tambien desarrollado con `Spring Boot`. Su funcion sera actuar como punto de entrada principal para las solicitudes del frontend, centralizando el acceso al microservicio `shopping_cart`.

Entre sus responsabilidades estaran:

- Recibir las solicitudes entrantes del frontend
- Redirigir las peticiones hacia el microservicio correspondiente
- Facilitar una arquitectura mas ordenada y escalable
- Servir como punto central de acceso para futuras ampliaciones del sistema

Dentro de esta carpeta tambien se manejara una estructura tipica de Spring Boot:

- Codigo fuente en `src/main/java`
- Configuracion en `src/main/resources/application.yml`
- Dependencias y construccion con `pom.xml`
- Contenerizacion mediante `Dockerfile`

### `Doc/`

Carpeta destinada a reunir la documentacion del proyecto. Aqui se almacenaran los archivos explicativos de cada fase del desarrollo, con el objetivo de que cualquier persona pueda entender como se construyo el microservicio paso a paso.

### `backend/`

Esta carpeta contendra el microservicio principal de `shopping_cart`, desarrollado con `Spring Boot`. Aqui se construira toda la logica relacionada con el carrito de compras, incluyendo:

- Gestion de productos agregados al carrito
- Actualizacion de cantidades
- Eliminacion de productos
- Consulta del contenido del carrito
- Calculo de totales
- Conexion con la base de datos
- Exposicion de endpoints para el frontend o para otros servicios

Dentro de esta carpeta tambien se manejara la estructura tipica de un proyecto Spring Boot, como:

- Codigo fuente en `src/main/java`
- Configuracion en `src/main/resources/application.yml`
- Dependencias y construccion con `pom.xml`
- Contenerizacion mediante `Dockerfile`

Como este proyecto tendra un solo microservicio, esta sera la pieza central de la logica del sistema, mientras que el `API Gateway` sera la puerta de entrada.

### `frontend/`

Esta carpeta contendra la interfaz visual del proyecto. Su objetivo sera permitir que el usuario pueda interactuar con el carrito de compras de forma sencilla, consultando productos agregados, actualizando cantidades, eliminando elementos y visualizando el total acumulado.

### `database/`

Esta carpeta estara destinada a la configuracion de la base de datos del proyecto. Aqui podran incluirse:

- Scripts de inicializacion
- Creacion de tablas
- Datos de prueba si son necesarios
- Documentacion relacionada con la persistencia

Dado que el proyecto tambien contempla la base de datos como una parte fundamental, esta carpeta ayudara a mantener separada la configuracion de persistencia respecto al backend y al frontend.

## 4. Observacion Importante

Aunque el modelo de referencia presentado en clase muestra varios microservicios, en nuestro proyecto solo se implementara uno: el microservicio de `shopping_cart`. Sin embargo, se agregara un `API Gateway` para centralizar el acceso al backend y se mantendra una estructura ordenada de tipo monorepo para que el sistema sea mas entendible y para que en el futuro pueda ampliarse si se desea integrar nuevos servicios.

## 5. Beneficios de Esta Organizacion

- Facilita la comprension general del proyecto.
- Separa claramente gateway, backend, frontend y base de datos.
- Permite documentar el desarrollo de forma ordenada.
- Mejora el mantenimiento del codigo.
- Hace mas sencillo el uso de Docker para levantar el entorno completo.
- Deja una base preparada para futuras ampliaciones.

## 6. Conclusion

La estructura propuesta para `shopping_cart` busca adaptar la idea de monorepo del curso a un caso mas simple y enfocado: un solo microservicio con `API Gateway`, frontend, base de datos y configuracion de despliegue. Esto permite mantener una organizacion profesional del proyecto sin perder claridad en el proceso de aprendizaje.

## 7. Estado Actual Implementado

Actualmente el proyecto ya no se encuentra solo en fase de propuesta. A nivel tecnico ya tiene implementado:

- `backend` con la `HU-001 - Crear carrito de compras`
- `backend` con la `HU-002 - Agregar producto al carrito`
- `backend` con la `HU-003 - Consultar carrito`
- `backend` con la `HU-004 - Actualizar cantidad de producto en el carrito`
- `backend` con la `HU-005 - Eliminar producto del carrito`
- `backend` con la `HU-006 - Calcular total del carrito`
- `gateway` consumiendo el backend a traves de `POST /api/v1/carts`
- `gateway` consumiendo el backend a traves de `POST /api/v1/carts/{cartId}/items`
- `gateway` consumiendo el backend a traves de `PUT /api/v1/carts/{cartId}/items/{itemId}`
- `gateway` consumiendo el backend a traves de `DELETE /api/v1/carts/{cartId}/items/{itemId}`
- `gateway` consumiendo el backend a traves de `GET /api/v1/carts/{cartId}`
- `gateway` consumiendo el backend a traves de `GET /api/v1/carts/{cartId}/total`
- `docker-compose.yml` para levantar `postgres`, `backend` y `gateway`
- `docker-compose.yml` con imagenes nombradas para `backend` y `gateway`
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
|-- database/
|   `-- init.sql
`-- sin carpeta `frontend` implementada actualmente
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
