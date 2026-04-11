# HU-007 - Visualizar y gestionar el carrito desde frontend por medio del API Gateway

## 1. Informacion general
- HU: `HU-007`
- Nombre: Visualizar y gestionar el carrito desde frontend por medio del API Gateway
- Componentes involucrados: `frontend`, `gateway`, `backend`
- Estado: Implementada en frontend Vue con integracion por gateway y despliegue por Docker Compose
- Rama de trabajo sugerida: `HU-007-front-dev`

## 2. Objetivo de la HU
Implementar un frontend en `Vue` que permita al usuario visualizar y gestionar su carrito de compras consumiendo exclusivamente las rutas expuestas por el `API Gateway`.

La HU base indica que el sistema debe:
- visualizar el contenido del carrito
- agregar productos al carrito
- actualizar cantidades de productos
- eliminar productos del carrito
- consultar el total consolidado
- reflejar en pantalla la informacion real retornada por backend

Adicionalmente, segun tu indicacion, esta HU debe plantearse con una arquitectura frontend modular inspirada en la referencia visual compartida, adaptada a `Vue`.

## 3. Justificacion funcional
Actualmente el proyecto ya cuenta con backend, gateway y una interfaz visual en `Vue` que permite al usuario operar sobre las capacidades del carrito de forma directa.

Esto genera un vacio funcional porque:
- las HU previas quedan limitadas a consumo tecnico por endpoint
- el usuario final necesita una interfaz clara para interactuar con su carrito
- el flujo de agregar, listar, actualizar y eliminar productos debe verse en tiempo real
- el resumen economico del carrito debe mostrarse de forma entendible
- el consumo debe centralizarse en el `API Gateway` y no contra el backend directamente

Por lo tanto, esta HU no consiste solo en construir una pantalla.  
Tambien define como el frontend se organiza, como se comunica con gateway y como representa visualmente el ciclo completo del carrito.

## 4. Justificacion del componente seleccionado
La HU se implementa principalmente en el componente `frontend` porque alli reside la experiencia visual del usuario.

Sin embargo, la solucion depende tambien de:
- `gateway`, que expone el punto unico de entrada para el frontend
- `backend`, que mantiene la logica de negocio y persistencia del carrito

El frontend debe desacoplarse del backend directo y apoyarse en `gateway` como capa de integracion para:
- centralizar rutas
- simplificar configuracion de consumo
- mantener una sola URL publica de acceso
- dejar preparada la aplicacion para futuras integraciones

## 5. Necesidad funcional observada en los layouts de referencia
Los layouts compartidos muestran una interfaz orientada a un flujo simple y entendible para gestionar el carrito.

Visualmente se identifica la necesidad de presentar:
- encabezado principal del carrito
- datos del usuario y del carrito activo
- formulario para agregar productos
- listado o resumen de productos agregados
- acciones directas para actualizar cantidades
- acciones directas para eliminar items
- subtotal y total consolidados

La interfaz de referencia evidencia dos variantes validas:
- una vista centrada en tabla de productos con formulario superior
- una vista dividida en dos paneles: formulario a la izquierda y resumen del carrito a la derecha

La HU debe dejar documentado que cualquiera de las dos distribuciones puede implementarse mientras conserve claridad, responsividad y alineacion con los endpoints disponibles.

## 6. Regla funcional principal
Cada vez que el usuario gestione su carrito desde frontend:

1. el frontend obtiene o identifica el `cartId` activo del usuario
2. el frontend consulta el detalle del carrito por medio del `gateway`
3. el usuario puede agregar un producto enviando nombre, precio y cantidad
4. el usuario puede modificar la cantidad de un item existente
5. el usuario puede eliminar un item del carrito
6. despues de cada operacion, el frontend refresca el estado del carrito
7. el total mostrado debe coincidir con la informacion retornada por backend a traves del `gateway`

## 7. Comportamiento esperado del frontend
La aplicacion frontend propuesta debe:
- construirse en `Vue`
- consumir unicamente rutas expuestas en `http://localhost:8080`
- representar el estado del carrito de forma clara y reactiva
- mostrar mensajes de carga, exito y error
- actualizar la interfaz despues de cada accion del usuario
- evitar logica de negocio duplicada cuando el dato ya es calculado por backend

Resultado esperado:
- el usuario puede gestionar su carrito desde una sola vista
- las acciones del formulario impactan inmediatamente la lista o resumen
- el frontend no se conecta directamente a `backend`
- la UI se mantiene consistente con los contratos ya definidos por las HU anteriores

## 8. Alcance funcional propuesto
La interfaz web implementada para el carrito cubre las siguientes capacidades:
- visualizar carrito por `cartId`
- mostrar productos agregados
- agregar nuevos productos al carrito
- actualizar cantidad de items existentes
- eliminar productos del carrito
- consultar total del carrito
- manejar estados de carga y errores de integracion

No hace parte de esta HU:
- autenticacion de usuarios
- checkout o pago
- persistencia offline
- historial de compras
- administracion de catalogo externo de productos

## 9. Endpoints a consumir desde frontend
### Consumo oficial por gateway
- Crear carrito: `POST http://localhost:8080/api/v1/carts`
- Consultar carrito: `GET http://localhost:8080/api/v1/carts/{cartId}`
- Agregar producto: `POST http://localhost:8080/api/v1/carts/{cartId}/items`
- Actualizar cantidad: `PUT http://localhost:8080/api/v1/carts/{cartId}/items/{itemId}`
- Eliminar producto: `DELETE http://localhost:8080/api/v1/carts/{cartId}/items/{itemId}`
- Consultar total: `GET http://localhost:8080/api/v1/carts/{cartId}/total`

### Dependencia interna
El `gateway` redirige las solicitudes hacia el microservicio `backend` del proyecto `shopping_cart`, por lo que el frontend no debe consumir `http://localhost:8081` de manera directa.

## 10. Payloads de referencia para frontend
### Crear carrito
```json
{
  "userId": 123
}
```

### Agregar producto
```json
{
  "productId": 10,
  "name": "Mouse",
  "price": 85000,
  "quantity": 2
}
```

### Actualizar cantidad
```json
{
  "quantity": 3
}
```

Estos contratos deben alinearse finalmente con las implementaciones ya existentes en backend y gateway.

## 11. Validaciones funcionales propuestas para frontend
El frontend debe validar como minimo:

1. `userId` y `cartId` deben existir para operar el flujo completo
2. `name` del producto no debe enviarse vacio si hace parte del contrato final
3. `price` debe ser numerico y mayor a `0`
4. `quantity` debe ser numerica y mayor a `0`
5. no se debe permitir enviar formularios incompletos
6. las acciones de actualizar y eliminar deben operar sobre items validos del carrito
7. la interfaz debe reflejar errores controlados retornados por gateway
8. el total mostrado debe obtenerse del backend y no de un calculo aislado en cliente como fuente unica

## 12. Arquitectura frontend implementada en Vue
Siguiendo tu indicacion y la imagen de arquitectura compartida, se implemento un frontend en `Vue 3` con `Vite`, organizado en capas funcionales similares a la referencia, pero adaptadas al ecosistema Vue.

Estructura implementada:
- `frontend/public`
- `frontend/src/api`
- `frontend/src/assets`
- `frontend/src/components`
- `frontend/src/layouts`
- `frontend/src/pages`
- `frontend/src/composables`
- `frontend/src/services`
- `frontend/src/stores`
- `frontend/src/utils`
- `frontend/src/router`
- `frontend/src/App.vue`

Distribucion aplicada:
- `api`
  - configuracion de cliente HTTP
  - interceptores
  - manejo base de URL del gateway
- `assets`
  - estilos, imagenes, iconos y recursos estaticos
- `components`
  - componentes reutilizables del carrito como formulario, tabla, item-card, resumen y botones de accion
- `layouts`
  - estructura visual base de la pagina del carrito
- `pages`
  - pantalla principal `CartPage.vue`
- `composables`
  - logica reutilizable del carrito, cargas y formularios
- `stores`
  - estado global usando `Pinia`
- `services`
  - funciones de consumo hacia endpoints del gateway
- `utils`
  - formateo de moneda, validaciones simples y mapeos de datos
- `router`
  - rutas del frontend si luego se expande a multiples vistas

## 13. Trazabilidad tecnica implementada
Tomando como base la arquitectura definida, la implementacion quedo distribuida asi:

- `frontend/src/api/httpClient.js`
  - configura `axios` con la URL base del `gateway`
  - centraliza el manejo base de errores HTTP
- `frontend/src/services/cartService.js`
  - encapsula llamadas HTTP del carrito
- `frontend/src/stores/cartStore.js`
  - centraliza el estado reactivo del carrito y sus acciones
- `frontend/src/composables/useCart.js`
  - abstrae logica reutilizable para la pagina
- `frontend/src/components/cart/CartHeader.vue`
  - muestra titulo, usuario y `cartId`
- `frontend/src/components/cart/AddProductForm.vue`
  - formulario para agregar productos
- `frontend/src/components/cart/CartItemsTable.vue`
  - tabla o lista de productos del carrito
- `frontend/src/components/cart/CartSummary.vue`
  - muestra subtotales, total y acciones finales
- `frontend/src/pages/CartPage.vue`
  - ensambla la vista principal del HU
- `frontend/src/layouts/MainLayout.vue`
  - contenedor visual general de la pagina
- `frontend/Dockerfile`
  - construye el frontend con Node
  - publica la aplicacion compilada con Nginx
- `docker-compose.yml`
  - incorpora el servicio `frontend` en `5173`

## 14. Propuesta de experiencia visual basada en la referencia
La pantalla principal debe conservar una linea visual limpia y centrada en productividad.

Lineamientos observados en los layouts:
- encabezado superior con titulo `Carrito de Compras`
- tarjetas blancas o claras con bordes suaves
- formulario de alta de producto en bloque independiente
- tabla o panel de resumen claramente separado
- botones primarios en azul para acciones principales
- botones de eliminacion en rojo para acciones destructivas
- informacion monetaria visible y destacada en el resumen

Regla de implementacion visual:
- en escritorio puede usarse una distribucion de dos columnas
- en pantallas pequenas debe colapsar a una columna
- la vista debe mantenerse clara incluso con multiples items

## 15. Contrato de respuesta esperado para representar en UI
La UI debe quedar preparada para mostrar una respuesta como:

```json
{
  "cartId": 456,
  "userId": 123,
  "items": [
    {
      "itemId": 1,
      "productId": 10,
      "name": "Mouse",
      "price": 85000,
      "quantity": 2,
      "subtotal": 170000
    },
    {
      "itemId": 2,
      "productId": 11,
      "name": "Teclado",
      "price": 120000,
      "quantity": 1,
      "subtotal": 120000
    }
  ],
  "total": 290000
}
```

Y para el resumen especializado:

```json
{
  "cartId": 456,
  "totalItems": 3,
  "totalAmount": 290000
}
```

## 16. Implementacion tecnica realizada
- se creo el proyecto frontend con `Vue 3`
- se uso `Vite` como base de construccion
- se configuro `axios` para consumo HTTP
- se implemento `Pinia` para estado global del carrito
- se crearon servicios dedicados para consumo del `gateway`
- se modelo una pagina principal del carrito con componentes reutilizables
- se implemento una capa visual responsiva alineada con el layout de referencia
- se parametrizo la URL del `gateway` mediante variables de entorno
- se agrego `Dockerfile` para ejecutar el frontend mediante `docker compose`

## 17. Criterios de aceptacion propuestos
1. Debe existir un frontend implementado en `Vue`.
2. El frontend debe permitir visualizar el contenido del carrito.
3. El frontend debe permitir agregar productos al carrito.
4. El frontend debe permitir actualizar cantidades de productos existentes.
5. El frontend debe permitir eliminar productos del carrito.
6. Todas las solicitudes del frontend deben pasar por el `API Gateway`.
7. El frontend no debe consumir directamente el backend.
8. La informacion mostrada en la interfaz debe coincidir con la respuesta retornada por backend a traves del gateway.
9. La vista debe presentar subtotal y total del carrito.
10. La interfaz debe seguir una arquitectura modular compatible con la estructura propuesta para `Vue`.
11. La vista debe adaptarse correctamente a escritorio y pantallas pequenas.

## 18. Archivos creados o modificados
- `frontend/package.json`
- `frontend/package-lock.json`
- `frontend/vite.config.js`
- `frontend/index.html`
- `frontend/.env`
- `frontend/.dockerignore`
- `frontend/Dockerfile`
- `frontend/src/main.js`
- `frontend/src/App.vue`
- `frontend/src/router/index.js`
- `frontend/src/api/httpClient.js`
- `frontend/src/services/cartService.js`
- `frontend/src/stores/cartStore.js`
- `frontend/src/composables/useCart.js`
- `frontend/src/layouts/MainLayout.vue`
- `frontend/src/pages/CartPage.vue`
- `frontend/src/components/cart/AddProductForm.vue`
- `frontend/src/components/cart/CartItemsTable.vue`
- `frontend/src/components/cart/CartSummary.vue`
- `frontend/src/components/cart/CartHeader.vue`
- `frontend/src/assets/styles/main.css`
- `docker-compose.yml`
- `README.md`
- `Doc/Changes/HU-007-frontend-gateway.md`

## 19. Riesgos o validaciones previas
- Confirmar si el flujo iniciara creando carrito automaticamente o si el `cartId` llegara desde otro proceso previo.
- Validar el contrato exacto de los endpoints de agregar y actualizar item para no documentar campos que luego cambien.
- Confirmar si el frontend manejara `productId` manual o si consumira despues un catalogo de productos externo.
- Definir si el resumen del carrito debe calcularse desde `GET /carts/{cartId}` o desde `GET /carts/{cartId}/total` como fuente principal.
- Validar si se usara solo `Vue + Pinia` o si se incorporara alguna libreria de componentes adicional.

## 20. Estado de este documento
Este documento deja registrada la implementacion funcional y tecnica de la `HU-007 - Visualizar y gestionar el carrito desde frontend por medio del API Gateway`, alineada con:
- la HU original del proyecto
- el formato documental usado en los archivos de `Changes`
- el estilo de documentacion observado en el ejemplo `HU-016`
- la necesidad de usar `Vue` como framework frontend
- la arquitectura modular inspirada en la referencia visual compartida
- la integracion centralizada a traves del `API Gateway`
- la ejecucion completa del entorno mediante `Docker Compose`
