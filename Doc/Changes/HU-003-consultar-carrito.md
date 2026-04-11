# HU-003 - Consultar carrito

## 1. Informacion general
- HU: `HU-003`
- Nombre: Consultar carrito
- Microservicio: `shopping-cart`
- Estado: Propuesta funcional y tecnica para implementacion en backend y gateway
- Rama de trabajo sugerida: `HU-003-back-dev`

## 2. Objetivo de la HU
Implementar en el backend de `shopping-cart` un metodo que permita consultar el contenido de un carrito de compras existente.

La HU base indica que el sistema debe:
- permitir consultar un carrito por su identificador
- retornar los productos agregados al carrito
- mostrar cantidades, precio unitario y subtotal por item
- calcular y retornar el total acumulado del carrito
- responder de forma controlada cuando el carrito no exista

Esta implementacion debe apoyarse sobre la base ya construida en `HU-001` y `HU-002`, donde el sistema ya puede crear carritos y agregar productos, pero aun necesita una vista consolidada del estado actual del carrito.

## 3. Justificacion funcional
Actualmente el backend ya puede crear carritos y agregar items, pero aun no dispone de un metodo especializado para consultar el contenido completo de un carrito.

Esto genera un vacio funcional porque:
- el usuario necesita visualizar el resumen actual de su seleccion de productos
- el frontend requiere una respuesta consolidada para mostrar el carrito
- no basta con conocer que el carrito existe, tambien se necesita listar sus items
- el total acumulado depende de sumar correctamente los subtotales registrados
- las siguientes HU del flujo de compra dependen de poder inspeccionar el estado actual del carrito

Por lo tanto, esta HU no consiste solo en buscar un registro por `id`.  
Tambien define como se entrega al cliente una vista completa y util del carrito con sus productos y montos.

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
- recuperar un carrito existente
- consultar sus items asociados
- construir un DTO de respuesta mas completo
- exponer el resultado al `gateway`

## 5. Necesidad funcional observada
La HU indica que el usuario debe poder consultar el carrito y ver un resumen de su contenido.

Eso obliga a definir en backend:
- como se recupera el carrito por `cartId`
- como se obtienen los items asociados
- como se representa la lista de productos en la respuesta
- como se calcula el total global del carrito
- que sucede si el carrito no existe
- si se permite consultar carritos en cualquier estado o solo carritos activos

La necesidad principal es que el backend entregue una representacion coherente y reutilizable del carrito completo.

## 6. Regla funcional principal
Cada vez que se solicite consultar un carrito:

1. el cliente envia el `cartId`
2. el backend valida que el `cartId` sea un valor positivo
3. el backend consulta si existe un carrito con ese identificador
4. si el carrito no existe, responde con un error controlado
5. si el carrito existe, obtiene sus items asociados
6. el backend construye la lista de productos con cantidad, precio y subtotal
7. el backend calcula el total acumulado sumando los subtotales de los items
8. el sistema retorna una respuesta consolidada del carrito

## 7. Comportamiento esperado del backend
El metodo propuesto debe:
- recibir una solicitud `GET`
- validar que el `cartId` exista
- recuperar la informacion basica del carrito
- recuperar todos los items asociados al carrito
- retornar la lista de productos agregados
- incluir `quantity`, `price` y `subtotal` por item
- calcular el total del carrito a partir de los subtotales persistidos
- responder correctamente si el carrito existe pero aun no tiene items

Resultado esperado:
- el cliente puede visualizar el estado actual del carrito en una sola respuesta
- el backend entrega un contrato claro para frontend y gateway
- el total del carrito se mantiene consistente con los subtotales de `cart_items`
- la consulta queda lista para soportar futuras HU de checkout o pago

## 8. Alcance funcional propuesto
Se propone crear un endpoint especializado para consultar un carrito existente.

El endpoint debe permitir:
- consultar un carrito por `cartId`
- retornar datos basicos del carrito
- retornar la coleccion de items asociados
- mostrar cantidad, precio y subtotal por producto
- calcular el total global del carrito
- responder aunque el carrito no tenga productos, devolviendo la lista vacia y total en `0`

No hace parte de esta HU:
- agregar productos al carrito
- modificar cantidades
- eliminar productos del carrito
- cerrar el carrito como compra confirmada
- validar stock contra un catalogo externo

## 9. Endpoint propuesto
### Consumo oficial por gateway
- Metodo: `GET`
- URL propuesta: `http://localhost:8080/api/v1/carts/{cartId}`

### Endpoint interno del microservicio
- Metodo: `GET`
- URL propuesta: `http://localhost:8081/api/v1/carts/{cartId}`

La ruta propuesta se alinea directamente con la HU original y con la convension REST ya usada en `HU-001` y `HU-002`.

## 10. Payload propuesto
Esta HU no requiere body de entrada.

La solicitud esperada se realiza unicamente mediante el parametro de ruta:

```http
GET /api/v1/carts/5
```

## 11. Validaciones de negocio propuestas
El backend debe validar como minimo:

1. `cartId` es obligatorio y debe ser un valor numerico positivo
2. el carrito debe existir en base de datos
3. la respuesta debe construirse con la informacion real almacenada en `carts` y `cart_items`
4. si no existen items asociados, la consulta debe responder exitosamente con una lista vacia
5. el total del carrito debe corresponder a la suma de los subtotales de sus items
6. la respuesta no debe omitir campos relevantes para mostrar el resumen del carrito

## 12. Modelo de respuesta propuesto
Para esta HU se propone construir una respuesta compuesta por:
- datos del carrito
- lista de items
- total general

Estructura sugerida:
- `id`
- `userId`
- `status`
- `createdAt`
- `updatedAt`
- `items`
- `total`

Cada item deberia incluir:
- `id`
- `productId`
- `name`
- `quantity`
- `price`
- `subtotal`

Esto deja preparado el contrato para futuras HU como actualizar cantidades, eliminar productos o confirmar la compra.

## 13. Trazabilidad tecnica propuesta
Tomando como base la estructura actual del backend, la implementacion deberia organizarse asi:

- `controller/CartController.java`
  - exponer `GET /api/v1/carts/{cartId}`
- `service/ICartService.java`
  - definir el contrato para consultar un carrito
- `service/CartServiceImpl.java`
  - buscar carrito existente
  - recuperar items asociados
  - calcular total
  - mapear la respuesta consolidada
- `repository/CartRepository.java`
  - consultar carrito por `id`
- `repository/CartItemRepository.java`
  - listar items por `cartId`
- `dto/CartDetailResponseDTO.java`
  - representar la respuesta completa del carrito
- `dto/CartItemDetailDTO.java`
  - representar cada item dentro de la consulta
- `exception/GlobalExceptionHandler.java`
  - devolver error controlado si el carrito no existe

## 14. Configuracion de base de datos propuesta
El backend ya se encuentra configurado para PostgreSQL y ya dispone de las tablas necesarias para soportar esta HU.

Configuracion de referencia observada:
- motor: `PostgreSQL`
- host: `localhost`
- puerto: `5020`
- base de datos: `shopping_cart_db`

URL de referencia:
- `jdbc:postgresql://localhost:5020/shopping_cart_db`

Para esta HU no seria necesario crear nuevas tablas si la respuesta se construye usando:
- `carts`
- `cart_items`

La logica principal recaeria en consultas y mapeo de respuesta.

## 15. Contrato de respuesta propuesto
Respuesta sugerida:

```json
{
  "id": 5,
  "userId": 15,
  "status": "ACTIVE",
  "createdAt": "2026-04-11T11:00:00",
  "updatedAt": "2026-04-11T11:15:00",
  "items": [
    {
      "id": 1,
      "productId": 101,
      "name": "Mouse Logitech G203",
      "quantity": 2,
      "price": 85000.00,
      "subtotal": 170000.00
    },
    {
      "id": 2,
      "productId": 202,
      "name": "Teclado Redragon Kumara",
      "quantity": 1,
      "price": 190000.00,
      "subtotal": 190000.00
    }
  ],
  "total": 360000.00
}
```

Si el carrito no tiene productos:
- `items` debe devolverse como lista vacia
- `total` debe devolverse como `0.00`

Esto permite que el frontend muestre tanto carritos con contenido como carritos vacios sin necesidad de consultas adicionales.

## 16. Implementacion tecnica sugerida
- `CartController`
  - crear endpoint `GET /api/v1/carts/{cartId}`
- `ICartService`
  - definir metodo para consultar un carrito por `id`
- `CartServiceImpl`
  - validar existencia del carrito
  - consultar items relacionados
  - sumar subtotales para obtener el total
  - mapear la respuesta consolidada
- `CartItemRepository`
  - crear consulta para listar items por `cartId`
- `CartDetailResponseDTO` y `CartItemDetailDTO`
  - separar la respuesta de detalle respecto a los DTO usados en creacion y adicion de items
- `GlobalExceptionHandler`
  - responder con error controlado cuando el carrito no exista
- `gateway`
  - exponer o enrutar la nueva operacion hacia backend

## 17. Criterios de aceptacion propuestos
1. Debe existir un endpoint backend para consultar un carrito por su identificador.
2. El endpoint debe responder con los datos basicos del carrito.
3. La respuesta debe incluir los productos agregados al carrito.
4. Cada item debe mostrar `quantity`, `price` y `subtotal`.
5. La respuesta debe incluir el total acumulado del carrito.
6. Si el carrito no existe, el sistema debe responder con un mensaje adecuado.
7. Si el carrito existe pero no tiene items, el sistema debe responder con lista vacia y total en cero.
8. La informacion retornada debe coincidir con los datos almacenados en la base de datos.
9. La operacion debe quedar expuesta tambien a traves del gateway.

## 18. Archivos candidatos a modificacion
- `backend/src/main/java/shopping_cart/backend/controller/CartController.java`
- `backend/src/main/java/shopping_cart/backend/service/ICartService.java`
- `backend/src/main/java/shopping_cart/backend/service/CartServiceImpl.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartItemRepository.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartRepository.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartDetailResponseDTO.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartItemDetailDTO.java`
- `backend/src/main/java/shopping_cart/backend/exception/GlobalExceptionHandler.java`
- `gateway/src/main/java/shopping_cart/gateway/controller/CartGatewayController.java`
- `gateway/src/main/java/shopping_cart/gateway/service/CartGatewayService.java`
- `gateway/src/main/resources/application.yaml`
- `backend/src/test/java/...`
- `gateway/src/test/java/...`

## 19. Riesgos o validaciones previas
- Confirmar si la consulta debe permitir carritos en cualquier estado o solo carritos `ACTIVE`.
- Confirmar si el total debe calcularse dinamicamente en cada consulta o si en algun momento se persistira a nivel de carrito.
- Validar si el orden de los items en la respuesta debe seguir `createdAt`, `id` o el orden natural de insercion.
- Confirmar si el contrato de respuesta final debe reutilizar `CartResponseDTO` o si conviene un DTO especializado para detalle.
- Validar si el gateway devolvera la respuesta tal cual del backend o si luego incorporara transformaciones adicionales.

## 20. Estado de este documento
Este documento deja trazada la propuesta funcional y tecnica de la `HU-003 - Consultar carrito`, alineada con:
- la HU original del proyecto
- el formato de cambios ya usado en `HU-001` y `HU-002`
- la estructura actual del backend y del gateway
- el modelo de datos ya existente en `carts` y `cart_items`
- la necesidad de exponer una vista consolidada del carrito para continuar con el flujo de compra
