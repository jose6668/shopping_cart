# HU-005 - Eliminar producto del carrito

## 1. Informacion general
- HU: `HU-005`
- Nombre: Eliminar producto del carrito
- Microservicio: `shopping-cart`
- Estado: Propuesta funcional para implementacion en backend y gateway
- Rama de trabajo sugerida: `HU-005-back-dev`

## 2. Objetivo de la HU
Implementar en el backend de `shopping-cart` un metodo que permita eliminar un producto previamente agregado dentro de un carrito de compras existente.

La HU base indica que el sistema debe:
- permitir eliminar un producto del carrito
- dejar de mostrar el item eliminado al consultar el carrito
- recalcular automaticamente el total general del carrito
- responder de forma controlada si el producto no existe dentro del carrito
- reflejar la eliminacion en base de datos

Esta implementacion debe apoyarse sobre la base ya construida en `HU-002`, `HU-003` y `HU-004`, donde el sistema ya puede agregar productos, consultar el carrito y actualizar cantidades, pero aun no dispone de una operacion explicita para retirar items.

## 3. Justificacion funcional
Actualmente el backend ya puede crear carritos, agregar items, consultar su contenido y actualizar cantidades, pero aun no dispone de un metodo especializado para eliminar un item existente.

Esto genera un vacio funcional porque:
- el usuario necesita retirar productos que ya no desea comprar
- no siempre resulta adecuado dejar la cantidad en `1` o ajustar manualmente otros valores
- el frontend requiere una operacion puntual para quitar items del resumen del carrito
- la consulta posterior del carrito debe mostrar solo los productos vigentes
- el total acumulado debe mantenerse consistente despues de la eliminacion

Por lo tanto, esta HU no consiste solo en borrar un registro de `cart_items`.  
Tambien define como validar la pertenencia del item al carrito, como responder si no existe y como mantener la coherencia del resumen del carrito despues de la eliminacion.

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
- validar que el item pertenezca realmente al carrito indicado
- eliminar el item persistido
- exponer la operacion tambien a traves del `gateway`

## 5. Necesidad funcional observada
La HU indica que el usuario debe poder eliminar un producto previamente agregado al carrito.

Eso obliga a definir en backend:
- como se identifica el carrito destino mediante `cartId`
- como se identifica el item especifico mediante `itemId`
- como se valida que el item pertenezca realmente al carrito indicado
- que sucede si el item no existe
- como se refleja la eliminacion en futuras consultas del carrito
- como se garantiza que el total del carrito quede recalculado implicitamente al consultar los items restantes

La necesidad principal es que el backend entregue una operacion de eliminacion segura, coherente y reutilizable para las siguientes etapas del flujo de compra.

## 6. Regla funcional principal
Cada vez que se solicite eliminar un item del carrito:

1. el cliente envia el `cartId`
2. el cliente envia el `itemId`
3. el backend valida que `cartId` e `itemId` sean valores positivos
4. el backend valida que el carrito exista
5. el backend valida que el item exista y pertenezca al carrito indicado
6. el backend elimina el item de la base de datos
7. la consulta posterior del carrito ya no debe incluir el item eliminado
8. el total del carrito debe corresponder unicamente a la suma de los items restantes
9. el sistema retorna una respuesta exitosa controlada

## 7. Comportamiento esperado del backend
El metodo propuesto debe:
- recibir una solicitud `DELETE`
- validar que el `cartId` exista
- validar que el `itemId` exista
- validar que el item este asociado al carrito indicado
- eliminar solo el item objetivo
- conservar sin cambios los demas items del carrito
- persistir la eliminacion en PostgreSQL
- responder adecuadamente si el item no existe o no pertenece al carrito

Resultado esperado:
- el usuario puede retirar productos del carrito sin afectar otros items
- el item eliminado deja de aparecer en consultas posteriores
- el total del carrito se mantiene consistente con los items restantes
- el backend deja preparado el flujo para futuras HU de checkout o confirmacion de compra

## 8. Alcance funcional propuesto
Se propone crear un endpoint especializado para eliminar un item existente dentro de un carrito.

El endpoint debe permitir:
- identificar un carrito por `cartId`
- identificar un item del carrito por `itemId`
- validar la existencia del carrito
- validar la pertenencia del item al carrito
- eliminar el item de base de datos
- reflejar el nuevo estado del carrito cuando se consulte nuevamente

No hace parte de esta HU:
- agregar productos nuevos al carrito
- modificar cantidades de productos
- cambiar precio o nombre del producto
- vaciar completamente el carrito en una sola operacion
- cerrar o confirmar la compra

## 9. Endpoint propuesto
### Consumo oficial por gateway
- Metodo: `DELETE`
- URL propuesta: `http://localhost:8080/api/v1/carts/{cartId}/items/{itemId}`

### Endpoint interno del microservicio
- Metodo: `DELETE`
- URL propuesta: `http://localhost:8081/api/v1/carts/{cartId}/items/{itemId}`

La ruta propuesta se alinea con la HU original y con la convencion REST ya usada en las operaciones de adicion y actualizacion de items.

## 10. Payload propuesto
Esta HU no requiere body de entrada.

La solicitud esperada se realiza unicamente mediante parametros de ruta:

```http
DELETE /api/v1/carts/5/items/2
```

## 11. Validaciones de negocio propuestas
El backend debe validar como minimo:

1. `cartId` es obligatorio y debe ser un valor numerico positivo
2. `itemId` es obligatorio y debe ser un valor numerico positivo
3. el carrito debe existir en base de datos
4. el item debe existir en base de datos
5. el item debe pertenecer al carrito indicado
6. la eliminacion debe afectar unicamente el registro objetivo en `cart_items`
7. la consulta posterior del carrito no debe incluir el item eliminado
8. el total del carrito debe recalcularse a partir de los subtotales de los items restantes
9. si el item no existe en el carrito, el sistema debe responder con un error controlado

## 12. Modelo de datos propuesto
Para esta HU no se requiere crear una nueva entidad, sino extender el comportamiento sobre la entidad `CartItem` ya existente.

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
- al eliminar el item, el registro debe dejar de existir en `cart_items`
- el carrito debe conservar sus demas items sin alteraciones
- el total del carrito debe construirse despues con base en los items que permanezcan asociados

Esto deja preparado el modelo para futuras HU como limpieza completa del carrito o confirmacion de compra.

## 13. Trazabilidad tecnica propuesta
Tomando como base la estructura actual del backend, la implementacion deberia organizarse asi:

- `controller/CartController.java`
  - exponer `DELETE /api/v1/carts/{cartId}/items/{itemId}`
- `service/ICartService.java`
  - definir el contrato para eliminar un item del carrito
- `service/CartServiceImpl.java`
  - validar carrito e item
  - validar pertenencia del item al carrito
  - eliminar el registro
- `repository/CartRepository.java`
  - apoyar validacion de existencia del carrito
- `repository/CartItemRepository.java`
  - consultar item por `id`
  - validar asociacion con el carrito
  - eliminar item persistido
- `exception/GlobalExceptionHandler.java`
  - centralizar respuestas de error
- `gateway/controller/CartGatewayController.java`
  - exponer la operacion hacia el cliente
- `gateway/service/CartGatewayService.java`
  - reenviar la solicitud `DELETE` hacia backend

## 14. Configuracion de base de datos propuesta
El backend ya se encuentra configurado para PostgreSQL y ya dispone de las tablas necesarias para soportar esta HU.

Configuracion de referencia observada en los documentos previos:
- motor: `PostgreSQL`
- host: `localhost`
- puerto: `5020`
- base de datos: `shopping_cart_db`

URL de referencia:
- `jdbc:postgresql://localhost:5020/shopping_cart_db`

Para esta HU no seria necesario crear nuevas tablas si la eliminacion se realiza sobre:
- `carts`
- `cart_items`

La logica principal recaeria en validaciones, eliminacion del item y coherencia de las consultas posteriores.

## 15. Contrato de respuesta propuesto
Respuesta sugerida:

```json
{
  "message": "Item eliminado correctamente del carrito",
  "cartId": 5,
  "itemId": 2
}
```

Alternativamente, la implementacion tambien podria responder con `204 No Content` si se desea una operacion mas estrictamente REST.

En la implementacion minima propuesta:
- la respuesta debe confirmar que la eliminacion fue realizada
- la consulta posterior del carrito debe reflejar el total general actualizado
- el item eliminado no debe volver a aparecer en la lista de productos

## 16. Implementacion tecnica sugerida
- `CartController`
  - endpoint nuevo `DELETE /api/v1/carts/{cartId}/items/{itemId}`
- `ICartService`
  - metodo nuevo para eliminar item de carrito
- `CartServiceImpl`
  - valida `cartId`
  - valida `itemId`
  - busca item existente
  - comprueba pertenencia al carrito
  - elimina el item
- `CartItemRepository`
  - reutiliza la consulta por `id` del item y la operacion de borrado
- `GlobalExceptionHandler`
  - devuelve errores controlados
- `gateway`
  - expone y enruta la nueva operacion hacia backend

## 17. Criterios de aceptacion propuestos
1. Debe existir un endpoint backend para eliminar un producto existente del carrito.
2. El endpoint debe recibir `cartId` e `itemId`.
3. El sistema debe validar que el carrito exista.
4. El sistema debe validar que el item exista y pertenezca al carrito indicado.
5. El item eliminado debe dejar de aparecer en la consulta del carrito.
6. El total del carrito debe reflejar correctamente la eliminacion.
7. Si el producto no existe en el carrito, el sistema debe responder con un mensaje adecuado.
8. La eliminacion debe quedar reflejada en la base de datos.
9. La operacion debe quedar expuesta tambien a traves del gateway.

## 18. Archivos candidatos a modificacion
- `backend/src/main/java/shopping_cart/backend/controller/CartController.java`
- `backend/src/main/java/shopping_cart/backend/service/ICartService.java`
- `backend/src/main/java/shopping_cart/backend/service/CartServiceImpl.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartRepository.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartItemRepository.java`
- `backend/src/main/java/shopping_cart/backend/exception/GlobalExceptionHandler.java`
- `gateway/src/main/java/shopping_cart/gateway/controller/CartGatewayController.java`
- `gateway/src/main/java/shopping_cart/gateway/service/CartGatewayService.java`
- `backend/src/test/java/...`
- `gateway/src/test/java/...`

## 19. Riesgos o validaciones previas
- Confirmar si al eliminar el ultimo item el carrito debe permanecer en estado `ACTIVE` y vacio, o si debe cambiar de estado.
- Confirmar si la respuesta final debe ser un mensaje simple, el detalle actualizado del carrito o `204 No Content`.
- Validar si el `itemId` es el identificador interno del registro `cart_items` o si en algun punto se desea operar por `productId`.
- Confirmar si la eliminacion debe permitirse sobre cualquier carrito existente o solo sobre carritos en estado `ACTIVE`.
- Validar si el gateway mantendra el contrato como texto crudo o si luego evolucionara a DTOs tipados.

## 20. Estado de este documento
Este documento deja registrada la propuesta funcional y tecnica de la `HU-005 - Eliminar producto del carrito`, alineada con:
- la HU original del proyecto
- el formato de cambios ya usado en `HU-001`, `HU-002`, `HU-003` y `HU-004`
- la estructura actual del backend y del gateway
- el modelo de datos ya existente en `carts` y `cart_items`
- la necesidad de permitir retirar productos del carrito antes de continuar con el flujo de compra

Actualmente el repositorio no evidencia aun una implementacion explicita del endpoint `DELETE` para esta HU, por lo que este documento queda como base de trabajo para su desarrollo.
