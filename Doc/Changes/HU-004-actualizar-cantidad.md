# HU-004 - Actualizar cantidad de producto en el carrito

## 1. Informacion general
- HU: `HU-004`
- Nombre: Actualizar cantidad de producto en el carrito
- Microservicio: `shopping-cart`
- Estado: Propuesta funcional para implementacion en backend y gateway
- Rama de trabajo sugerida: `HU-004-back-dev`

## 2. Objetivo de la HU
Implementar en el backend de `shopping-cart` un metodo que permita modificar la cantidad de un producto ya agregado dentro de un carrito de compras existente.

La HU base indica que el sistema debe:
- permitir actualizar la cantidad de un producto existente en el carrito
- validar que la nueva cantidad sea mayor que `0`
- recalcular automaticamente el subtotal del item
- actualizar correctamente el total general del carrito
- persistir el cambio en base de datos

Esta implementacion debe apoyarse sobre la base ya construida en `HU-002` y `HU-003`, donde el sistema ya puede agregar productos al carrito y consultar su contenido, pero aun no permite ajustar cantidades de forma explicita.

## 3. Justificacion funcional
Actualmente el backend ya puede crear carritos, agregar items y consultar el detalle del carrito, pero aun no dispone de un metodo especializado para actualizar la cantidad de un item existente.

Esto genera un vacio funcional porque:
- el usuario necesita corregir o ajustar la cantidad antes de finalizar su compra
- no siempre resulta conveniente eliminar el producto y volverlo a agregar
- el frontend requiere una operacion puntual para editar cantidades
- el subtotal del item debe mantenerse consistente con la nueva cantidad
- el total del carrito debe reflejar el cambio inmediatamente

Por lo tanto, esta HU no consiste solo en cambiar un numero en la base de datos.  
Tambien define como se valida la cantidad, como se recalculan montos y como se conserva la coherencia del carrito luego de la modificacion.

## 4. Justificacion del microservicio seleccionado
La HU debe implementarse en el microservicio `backend` del proyecto `shopping_cart` porque alli reside la logica principal del dominio carrito y ya existen los componentes base necesarios.

Segun la estructura actual del repositorio, el backend ya contiene:
- `CartController`
- `ICartService`
- `CartServiceImpl`
- `CartRepository`
- `CartItemRepository`
- `Cart`
- `CartItem`
- DTOs y manejo de errores

Esto permite extender la arquitectura actual para:
- ubicar un carrito existente
- ubicar un item especifico dentro del carrito
- modificar su cantidad
- recalcular el subtotal persistido
- exponer la operacion tambien a traves del `gateway`

## 5. Necesidad funcional observada
La HU indica que el usuario debe poder modificar la cantidad de un producto ya agregado al carrito.

Eso obliga a definir en backend:
- como se identifica el carrito destino mediante `cartId`
- como se identifica el item especifico mediante `itemId`
- como se valida que el item pertenezca realmente al carrito indicado
- que sucede si la nueva cantidad llega en `0` o en un valor negativo
- como se recalcula el subtotal del item
- como se refleja el nuevo total del carrito en futuras consultas

La necesidad principal es que el backend entregue una operacion de edicion segura, coherente y reutilizable para las siguientes etapas del flujo de compra.

## 6. Regla funcional principal
Cada vez que se solicite actualizar la cantidad de un item del carrito:

1. el cliente envia el `cartId`
2. el cliente envia el `itemId`
3. el cliente envia la nueva `quantity`
4. el backend valida que `cartId` e `itemId` sean valores positivos
5. el backend valida que el carrito exista
6. el backend valida que el item exista y pertenezca al carrito indicado
7. el backend valida que la nueva cantidad sea mayor a `0`
8. el backend recalcula el subtotal del item como `quantity * price`
9. el cambio se persiste en la base de datos
10. el sistema retorna el item actualizado o una vista actualizada del carrito

## 7. Comportamiento esperado del backend
El metodo propuesto debe:
- recibir una solicitud `PUT`
- validar que el `cartId` exista
- validar que el `itemId` exista
- validar que el item este asociado al carrito indicado
- validar que `quantity` sea mayor a `0`
- actualizar solo la cantidad del item objetivo
- recalcular el subtotal usando el precio ya persistido del item
- conservar sin cambios los demas items del carrito
- persistir la modificacion en PostgreSQL

Resultado esperado:
- el usuario puede ajustar cantidades sin recrear el item
- el subtotal del item queda consistente con la nueva cantidad
- el total del carrito se mantiene correcto para las consultas posteriores
- el backend deja preparado el flujo para futuras HU de eliminacion de items o checkout

## 8. Alcance funcional propuesto
Se propone crear un endpoint especializado para actualizar la cantidad de un item existente dentro de un carrito.

El endpoint debe permitir:
- identificar un carrito por `cartId`
- identificar un item del carrito por `itemId`
- recibir la nueva `quantity`
- recalcular el subtotal del item
- persistir el cambio en base de datos
- reflejar el nuevo estado del carrito cuando se consulte nuevamente

No hace parte de esta HU:
- agregar productos nuevos al carrito
- eliminar productos del carrito
- cambiar precio o nombre del producto
- validar stock contra un catalogo externo
- cerrar o confirmar la compra

## 9. Endpoint propuesto
### Consumo oficial por gateway
- Metodo: `PUT`
- URL propuesta: `http://localhost:8080/api/v1/carts/{cartId}/items/{itemId}`

### Endpoint interno del microservicio
- Metodo: `PUT`
- URL propuesta: `http://localhost:8081/api/v1/carts/{cartId}/items/{itemId}`

La ruta propuesta se alinea con la HU original y con la convencion REST ya usada en los endpoints de creacion de carrito, consulta y adicion de items.

## 10. Payload propuesto
```json
{
  "quantity": 4
}
```

## 11. Validaciones de negocio propuestas
El backend debe validar como minimo:

1. `cartId` es obligatorio y debe ser un valor numerico positivo
2. `itemId` es obligatorio y debe ser un valor numerico positivo
3. el carrito debe existir en base de datos
4. el item debe existir en base de datos
5. el item debe pertenecer al carrito indicado
6. `quantity` es obligatoria y debe ser mayor a `0`
7. el precio persistido del item debe conservarse como base para recalcular `subtotal`
8. el subtotal actualizado debe corresponder a `quantity * price`
9. el cambio debe quedar persistido correctamente en `cart_items`

## 12. Modelo de datos propuesto
Para esta HU no se requiere crear una nueva entidad, sino extender el comportamiento de la entidad `CartItem` ya existente.

Campos relevantes del item:
- `id`
- `cartId`
- `productId`
- `name`
- `quantity`
- `price`
- `subtotal`
- `createdAt`
- `updatedAt`

Regla sugerida:
- al modificar `quantity`, se debe recalcular `subtotal`
- el precio del item debe mantenerse como referencia del producto ya agregado
- `updatedAt` debe reflejar el momento de la modificacion

Esto deja preparado el modelo para futuras HU como eliminacion de items o recalculo de resumen de compra.

## 13. Trazabilidad tecnica propuesta
Tomando como base la estructura actual del backend, la implementacion deberia organizarse asi:

- `controller/CartController.java`
  - exponer `PUT /api/v1/carts/{cartId}/items/{itemId}`
- `service/ICartService.java`
  - definir el contrato para actualizar cantidad de un item
- `service/CartServiceImpl.java`
  - validar carrito e item
  - validar nueva cantidad
  - recalcular subtotal
  - persistir cambios
- `repository/CartRepository.java`
  - apoyar validacion de existencia del carrito
- `repository/CartItemRepository.java`
  - consultar item por `id`
  - validar asociacion con el carrito
- `entity/CartItem.java`
  - mantener mapeo de cantidad, precio y subtotal
- `dto/UpdateCartItemQuantityRequestDTO.java`
  - recibir la nueva `quantity`
- `dto/CartItemResponseDTO.java` o DTO especializado
  - retornar la informacion actualizada del item
- `exception/GlobalExceptionHandler.java`
  - centralizar respuestas de error

## 14. Configuracion de base de datos propuesta
El backend ya se encuentra configurado para PostgreSQL y ya dispone de las tablas necesarias para soportar esta HU.

Configuracion de referencia observada en los documentos previos:
- motor: `PostgreSQL`
- host: `localhost`
- puerto: `5020`
- base de datos: `shopping_cart_db`

URL de referencia:
- `jdbc:postgresql://localhost:5020/shopping_cart_db`

Para esta HU no seria necesario crear nuevas tablas si la actualizacion se realiza sobre:
- `carts`
- `cart_items`

La logica principal recaeria en validaciones, actualizacion del item y recalculo de montos.

## 15. Contrato de respuesta propuesto
Respuesta sugerida:

```json
{
  "id": 2,
  "cartId": 5,
  "productId": 101,
  "name": "Mouse Logitech G203",
  "quantity": 4,
  "price": 85000.00,
  "subtotal": 340000.00
}
```

Alternativamente, si luego se desea optimizar para frontend, la respuesta podria evolucionar hacia un contrato que incluya tambien el total del carrito.

En la implementacion minima propuesta:
- la respuesta debe reflejar la nueva cantidad del item
- el subtotal debe corresponder a la cantidad actualizada
- la consulta posterior del carrito debe reflejar el total general recalculado

## 16. Implementacion tecnica sugerida
- `CartController`
  - crear endpoint `PUT /api/v1/carts/{cartId}/items/{itemId}`
- `ICartService`
  - definir metodo para actualizar cantidad de item
- `CartServiceImpl`
  - validar `cartId`
  - validar `itemId`
  - buscar item existente
  - comprobar pertenencia al carrito
  - actualizar cantidad
  - recalcular subtotal
- `CartItemRepository`
  - agregar o reutilizar consulta para localizar el item dentro del carrito
- `UpdateCartItemQuantityRequestDTO`
  - representar el payload de entrada
- `CartItemResponseDTO`
  - reutilizar o adaptar respuesta de item actualizado
- `GlobalExceptionHandler`
  - devolver errores controlados
- `gateway`
  - exponer o enrutar la nueva operacion hacia backend

## 17. Criterios de aceptacion propuestos
1. Debe existir un endpoint backend para actualizar la cantidad de un producto existente en el carrito.
2. El endpoint debe recibir `cartId`, `itemId` y la nueva `quantity`.
3. El sistema debe validar que el carrito exista.
4. El sistema debe validar que el item exista y pertenezca al carrito indicado.
5. La nueva cantidad debe ser mayor a `0`.
6. El subtotal del item debe recalcularse automaticamente.
7. El cambio debe quedar persistido en la base de datos.
8. La consulta posterior del carrito debe reflejar el total general actualizado.
9. La operacion debe quedar expuesta tambien a traves del gateway.

## 18. Archivos candidatos a modificacion
- `backend/src/main/java/shopping_cart/backend/controller/CartController.java`
- `backend/src/main/java/shopping_cart/backend/service/ICartService.java`
- `backend/src/main/java/shopping_cart/backend/service/CartServiceImpl.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartRepository.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartItemRepository.java`
- `backend/src/main/java/shopping_cart/backend/entity/CartItem.java`
- `backend/src/main/java/shopping_cart/backend/dto/UpdateCartItemQuantityRequestDTO.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartItemResponseDTO.java`
- `backend/src/main/java/shopping_cart/backend/exception/GlobalExceptionHandler.java`
- `gateway/src/main/java/shopping_cart/gateway/controller/CartGatewayController.java`
- `gateway/src/main/java/shopping_cart/gateway/service/CartGatewayService.java`
- `gateway/src/main/java/shopping_cart/gateway/dto/UpdateCartItemQuantityRequestDTO.java`
- `backend/src/test/java/...`
- `gateway/src/test/java/...`

## 19. Riesgos o validaciones previas
- Confirmar si al enviar `quantity = 0` el sistema debe rechazar la solicitud o interpretar la accion como eliminacion del item.
- Confirmar si la actualizacion debe permitirse solo sobre carritos en estado `ACTIVE`.
- Validar si la respuesta final devolvera solo el item actualizado o el detalle completo del carrito.
- Confirmar si el `itemId` es el identificador interno del registro `cart_items` o si en algun punto se desea operar por `productId`.
- Validar si el gateway mantendra el contrato como texto crudo o si luego evolucionara a DTOs tipados.

## 20. Estado de este documento
Este documento deja definida la propuesta funcional y tecnica inicial para implementar la `HU-004 - Actualizar cantidad de producto en el carrito`, alineada con:
- la HU original del proyecto
- el formato de cambios ya usado en `HU-001`, `HU-002` y `HU-003`
- la estructura actual del backend y del gateway
- el modelo de datos ya existente en `carts` y `cart_items`
- la necesidad de permitir ajustes de cantidad antes de continuar con el flujo de compra
