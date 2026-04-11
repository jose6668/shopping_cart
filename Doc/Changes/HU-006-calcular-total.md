# HU-006 - Calcular total del carrito

## 1. Informacion general
- HU: `HU-006`
- Nombre: Calcular total del carrito
- Microservicio: `shopping-cart`
- Estado: Implementada en backend y gateway
- Rama de trabajo sugerida: `HU-006-back-dev`

## 2. Objetivo de la HU
Implementar en el backend de `shopping-cart` una forma consistente de obtener el total actualizado de un carrito de compras existente.

La HU base indica que el sistema debe:
- calcular el total general a partir de los subtotales de cada item
- actualizar el total al agregar, modificar o eliminar productos
- incluir en la respuesta el numero total de items y el monto total
- mantener consistencia con los datos persistidos
- permitir que el resultado sea consumido desde backend y mostrado en frontend

Esta HU se apoya directamente sobre las capacidades construidas en `HU-002`, `HU-003`, `HU-004` y `HU-005`, ya que el total depende de los items agregados, actualizados, eliminados y consultados desde el carrito.

## 3. Justificacion funcional
Un carrito de compras no queda funcionalmente completo si el usuario no puede conocer el valor acumulado de los productos seleccionados.

Esto genera una necesidad funcional clara porque:
- el usuario necesita visualizar el valor total antes de continuar con la compra
- el frontend requiere un dato consolidado para mostrar el resumen del carrito
- cada cambio en cantidades o items debe reflejarse inmediatamente en el monto acumulado
- el calculo debe hacerse con base en los subtotales persistidos de cada item
- el total no debe depender de calculos manuales o duplicados en cliente

Por lo tanto, esta HU no consiste solo en sumar valores.  
Tambien define como mantener la coherencia del total frente a operaciones previas del carrito y como exponer ese resultado de forma util para consumo posterior.

## 4. Justificacion del microservicio seleccionado
La HU debe resolverse en el microservicio `backend` del proyecto `shopping_cart` porque alli reside la informacion persistida del carrito y de sus items.

Segun la estructura actual del repositorio, el backend ya contiene:
- `CartController`
- `ICartService`
- `CartServiceImpl`
- `CartRepository`
- `CartItemRepository`
- `Cart`
- `CartItem`
- `CartDetailResponseDTO`

Esto permite implementar o consolidar la HU reutilizando la informacion ya guardada en base de datos y evitando que el calculo del total quede delegado al frontend.

## 5. Necesidad funcional observada
La HU indica que el usuario debe obtener el total actualizado de su carrito.

Eso obliga a definir en backend:
- como se identifica el carrito objetivo mediante `cartId`
- como se obtienen los items actualmente asociados al carrito
- como se calcula el total con base en los subtotales persistidos
- como se informa adicionalmente la cantidad de items del carrito
- como se garantiza que el monto consultado refleje el estado real despues de altas, cambios o eliminaciones

La necesidad principal es exponer un resumen economico confiable del carrito para que frontend no tenga que reconstruir el total por su cuenta.

## 6. Regla funcional principal
Cada vez que se consulte el total del carrito:

1. el cliente envia el `cartId`
2. el backend valida que `cartId` sea un valor positivo
3. el backend valida que el carrito exista
4. el backend obtiene los items actualmente asociados al carrito
5. el backend calcula el total sumando los subtotales de cada item
6. el backend calcula la cantidad total de items consultables del carrito
7. el sistema responde con el monto total actualizado y el resumen solicitado

## 7. Comportamiento esperado del backend
La HU debe permitir:
- consultar un carrito existente
- recuperar sus items persistidos
- sumar el campo `subtotal` de cada item
- devolver un valor total consistente
- reflejar inmediatamente los cambios realizados por operaciones previas sobre el carrito

Resultado esperado:
- si se agrega un producto, el total aumenta
- si se actualiza una cantidad, el total cambia segun el nuevo subtotal
- si se elimina un item, el total disminuye
- si el carrito no tiene items, el total debe ser `0`

## 8. Alcance funcional implementado
Se implemento un endpoint especializado para consultar el total actualizado del carrito sin depender de la respuesta completa del detalle.

La implementacion actual permite:
- consultar el total por `cartId`
- calcular `totalAmount` a partir de los subtotales persistidos
- calcular `totalItems` como suma total de cantidades del carrito
- mantener el detalle completo del carrito en `GET /api/v1/carts/{cartId}`
- exponer la nueva consulta tambien a traves del `gateway`

Con esto, la HU queda cubierta tanto funcional como tecnicamente segun el endpoint definido en la historia.

## 9. Endpoint implementado
### Consumo oficial por gateway
- Metodo: `GET`
- URL implementada: `http://localhost:8080/api/v1/carts/{cartId}/total`

### Endpoint interno del microservicio
- Metodo: `GET`
- URL implementada: `http://localhost:8081/api/v1/carts/{cartId}/total`

Adicionalmente, se mantiene disponible:
- `GET /api/v1/carts/{cartId}`

Esa ruta sigue retornando el detalle completo del carrito con el campo `total`, mientras que la nueva ruta entrega un resumen especifico para la HU.

## 10. Payload propuesto
Esta HU no requiere body de entrada.

La solicitud esperada se realiza unicamente mediante parametro de ruta:

```http
GET /api/v1/carts/5/total
```

## 11. Validaciones de negocio implementadas
El backend ya valida como minimo:

1. `cartId` es obligatorio y debe ser un valor numerico positivo
2. el carrito debe existir en base de datos
3. `totalAmount` se calcula a partir de los `subtotal` de los items persistidos
4. `totalItems` se calcula como suma total de `quantity` de los items del carrito
5. el total se recalcula al momento de consultar el endpoint
6. si el carrito esta vacio, la respuesta debe retornar `0` en total e items
7. si el carrito no existe, el sistema responde con error controlado

## 12. Modelo de datos aplicado
La HU no requiere una nueva entidad principal de persistencia.

El calculo observado se soporta sobre:
- `Cart`
- `CartItem`

Campos relevantes del item:
- `id`
- `cartId`
- `productId`
- `name`
- `quantity`
- `price`
- `subtotal`

Regla aplicada:
- cada `CartItem` conserva su `subtotal`
- el total general del carrito se obtiene sumando todos los subtotales asociados al `cartId`
- el resultado final depende del estado persistido de `cart_items`

## 13. Trazabilidad tecnica implementada
Tomando como base la estructura actual del backend, la logica relacionada con esta HU quedo distribuida asi:

- `controller/CartController.java`
  - expone `GET /api/v1/carts/{cartId}/total`
- `service/ICartService.java`
  - define el contrato `getCartTotal(Long cartId)`
- `service/CartServiceImpl.java`
  - consulta el carrito
  - obtiene los items asociados
  - suma `subtotal` de cada item
  - suma `quantity` de cada item
  - construye `CartTotalResponseDTO`
- `dto/CartTotalResponseDTO.java`
  - transporta `cartId`, `totalItems` y `totalAmount`
- `gateway/controller/CartGatewayController.java`
  - expone `GET /api/v1/carts/{cartId}/total` hacia el cliente
- `gateway/service/CartGatewayService.java`
  - reenvia la consulta desde gateway hacia backend
- `backend/src/test/.../CartServiceImplTest.java`
  - valida total monetario y total de items
- `gateway/src/test/.../CartGatewayControllerTest.java`
  - valida la exposicion del endpoint en gateway

## 14. Configuracion de base de datos propuesta
El backend ya se encuentra configurado para PostgreSQL y ya dispone de las tablas necesarias para soportar esta HU.

Configuracion de referencia observada en documentos previos:
- motor: `PostgreSQL`
- host: `localhost`
- puerto: `5020`
- base de datos: `shopping_cart_db`

URL de referencia:
- `jdbc:postgresql://localhost:5020/shopping_cart_db`

Para esta HU no seria necesario crear nuevas tablas.  
La logica depende de:
- `carts`
- `cart_items`

## 15. Contrato de respuesta implementado
Respuesta implementada:

```json
{
  "cartId": 5,
  "totalItems": 3,
  "totalAmount": 380000.00
}
```

La respuesta permite al frontend conocer:
- el carrito consultado
- la suma total de cantidades del carrito
- el monto total actualizado del carrito

## 16. Implementacion tecnica realizada
- `CartController`
  - endpoint implementado `GET /api/v1/carts/{cartId}/total`
- `ICartService`
  - contrato implementado para consultar el total del carrito
- `CartServiceImpl`
  - obtiene items del carrito
  - suma subtotales usando `BigDecimal`
  - suma cantidades para construir `totalItems`
  - reutiliza la misma base de calculo usada en el detalle del carrito
- `CartTotalResponseDTO`
  - representa la respuesta especializada del HU
- `gateway`
  - expone y reenvia la nueva consulta de total
- `pruebas`
  - se agregaron pruebas unitarias en backend y gateway

## 17. Criterios de aceptacion propuestos
1. El sistema debe calcular el total del carrito a partir de los subtotales de cada item.
2. El total debe actualizarse al agregar, modificar o eliminar productos.
3. La respuesta debe incluir el monto total del carrito.
4. La respuesta debe incluir el numero total de items del carrito.
5. El calculo debe realizarse con base en los datos persistidos.
6. El resultado debe poder consultarse desde backend.
7. El resultado debe poder consumirse desde gateway para uso del frontend.
8. Si el carrito no existe, el sistema debe responder con un error controlado.

## 18. Archivos relacionados
- `backend/src/main/java/shopping_cart/backend/controller/CartController.java`
- `backend/src/main/java/shopping_cart/backend/service/ICartService.java`
- `backend/src/main/java/shopping_cart/backend/service/CartServiceImpl.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartTotalResponseDTO.java`
- `backend/src/test/java/shopping_cart/backend/service/CartServiceImplTest.java`
- `gateway/src/main/java/shopping_cart/gateway/controller/CartGatewayController.java`
- `gateway/src/main/java/shopping_cart/gateway/service/CartGatewayService.java`
- `gateway/src/test/java/shopping_cart/gateway/controller/CartGatewayControllerTest.java`
- `Doc/Changes/HU-006-calcular-total.md`

## 19. Riesgos o validaciones previas
- Confirmar con frontend si `totalItems` debe mantenerse como suma total de unidades o si en algun punto se requerira tambien el numero de lineas.
- Validar si el frontend necesitara en el futuro impuestos, descuentos o costo de envio dentro del resumen monetario.
- Confirmar si el endpoint de total debe restringirse solo a carritos en estado `ACTIVE` o si tambien se permitira para historicos.

## 20. Estado de este documento
Este documento deja registrada la implementacion funcional y tecnica de la `HU-006 - Calcular total del carrito`, alineada con:
- la HU original del proyecto
- el formato de cambios ya usado en `HU-001`, `HU-002`, `HU-003`, `HU-004` y `HU-005`
- la estructura actual del backend y del gateway
- la logica ya implementada para consultar un resumen especializado de total
- la necesidad de mostrar el monto actualizado del carrito en backend, gateway y frontend
