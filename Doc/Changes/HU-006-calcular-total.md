# HU-006 - Calcular total del carrito

## 1. Informacion general
- HU: `HU-006`
- Nombre: Calcular total del carrito
- Microservicio: `shopping-cart`
- Estado: Parcialmente cubierta en backend y gateway mediante consulta del carrito
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

## 8. Alcance funcional observado
Actualmente el repositorio ya resuelve gran parte de esta HU mediante la consulta general del carrito.

La implementacion observada permite:
- consultar un carrito por `cartId`
- obtener la lista completa de items
- devolver el campo `total` dentro de `CartDetailResponseDTO`
- recalcular implicitamente el total cada vez que se consulta el carrito
- exponer la misma respuesta a traves del `gateway`

No se observa aun en el codigo actual:
- un endpoint dedicado `GET /api/v1/carts/{cartId}/total`
- un DTO especializado solo para total e items

Por eso, la HU queda funcionalmente cubierta en el flujo de consulta del carrito, aunque no exactamente con el contrato puntual descrito en la HU original.

## 9. Endpoint observado y endpoint propuesto
### Consumo actual por gateway
- Metodo: `GET`
- URL observada: `http://localhost:8080/api/v1/carts/{cartId}`

### Endpoint actual del microservicio
- Metodo: `GET`
- URL observada: `http://localhost:8081/api/v1/carts/{cartId}`

### Endpoint propuesto por la HU
- Metodo: `GET`
- URL propuesta: `http://localhost:8080/api/v1/carts/{cartId}/total`

La implementacion actual entrega el total dentro del detalle completo del carrito, por lo que la HU ya tiene soporte funcional aunque todavia no mediante una ruta exclusiva para total.

## 10. Payload propuesto
Esta HU no requiere body de entrada.

La solicitud esperada se realiza unicamente mediante parametro de ruta:

```http
GET /api/v1/carts/5/total
```

En la implementacion actual observada, la consulta funcional equivalente es:

```http
GET /api/v1/carts/5
```

## 11. Validaciones de negocio observadas
El backend ya valida como minimo:

1. `cartId` es obligatorio y debe ser un valor numerico positivo
2. el carrito debe existir en base de datos
3. el total se calcula a partir de los `subtotal` de los items persistidos
4. el total se recalcula al momento de consultar el carrito
5. si el carrito no existe, el sistema responde con error controlado

Validaciones deseables para un endpoint especializado de total:
6. la respuesta debe incluir tambien el numero total de items
7. el total debe devolverse con precision monetaria consistente
8. el contrato debe ser estable para consumo desde frontend

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

## 13. Trazabilidad tecnica observada
Tomando como base la estructura actual del backend, la logica relacionada con esta HU ya se encuentra distribuida asi:

- `controller/CartController.java`
  - expone `GET /api/v1/carts/{cartId}`
- `service/ICartService.java`
  - define el contrato `getCartById(Long cartId)`
- `service/CartServiceImpl.java`
  - consulta el carrito
  - obtiene los items asociados
  - suma `subtotal` de cada item
  - construye `CartDetailResponseDTO` con el campo `total`
- `dto/CartDetailResponseDTO.java`
  - transporta el total calculado del carrito
- `gateway/controller/CartGatewayController.java`
  - expone la consulta del carrito hacia el cliente
- `gateway/service/CartGatewayService.java`
  - reenvia la consulta desde gateway hacia backend

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

## 15. Contrato de respuesta observado y contrato propuesto
Respuesta actualmente observada dentro de la consulta del carrito:

```json
{
  "id": 5,
  "userId": 12,
  "status": "ACTIVE",
  "createdAt": "2026-04-11T13:10:00",
  "updatedAt": "2026-04-11T13:20:00",
  "items": [
    {
      "id": 1,
      "productId": 101,
      "name": "Producto A",
      "quantity": 2,
      "price": 15000.00,
      "subtotal": 30000.00
    }
  ],
  "total": 30000.00
}
```

Contrato especializado propuesto por la HU:

```json
{
  "cartId": 5,
  "totalItems": 1,
  "totalAmount": 30000.00
}
```

La observacion principal es que el total ya existe en la respuesta del carrito, pero todavia no bajo un DTO exclusivo orientado solo al resumen monetario.

## 16. Implementacion tecnica observada
- `CartController`
  - endpoint implementado `GET /api/v1/carts/{cartId}`
- `ICartService`
  - contrato implementado para consultar detalle del carrito
- `CartServiceImpl`
  - obtiene items del carrito
  - suma subtotales usando `BigDecimal`
  - devuelve el total dentro de `CartDetailResponseDTO`
- `CartDetailResponseDTO`
  - representa el detalle completo del carrito con el campo `total`
- `gateway`
  - expone y reenvia la consulta del carrito

Si se desea cerrar completamente la HU segun el contrato original, haria falta:
- crear un DTO especializado para total
- implementar `GET /api/v1/carts/{cartId}/total` en backend
- exponer la misma ruta en gateway

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
- `backend/src/main/java/shopping_cart/backend/dto/CartDetailResponseDTO.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartDetailItemResponseDTO.java`
- `gateway/src/main/java/shopping_cart/gateway/controller/CartGatewayController.java`
- `gateway/src/main/java/shopping_cart/gateway/service/CartGatewayService.java`
- `Doc/Changes/HU-006-calcular-total.md`

## 19. Riesgos o validaciones previas
- Confirmar si la HU debe darse por satisfecha con el campo `total` dentro de `GET /api/v1/carts/{cartId}` o si es obligatorio crear `GET /api/v1/carts/{cartId}/total`.
- Confirmar si `numero total de items` significa cantidad de lineas del carrito o suma total de unidades.
- Validar si el frontend necesita solo `totalAmount` o tambien un desglose adicional del resumen.
- Confirmar el formato monetario final esperado para la respuesta al cliente.

## 20. Estado de este documento
Este documento deja registrada la situacion actual de la `HU-006 - Calcular total del carrito`, alineada con:
- la HU original del proyecto
- el formato de cambios ya usado en `HU-001`, `HU-002`, `HU-003`, `HU-004` y `HU-005`
- la estructura actual del backend y del gateway
- la logica ya implementada en la consulta detallada del carrito
- la necesidad de decidir si se mantiene el enfoque actual o si se agrega un endpoint especializado para el total
