# HU-002 - Agregar producto al carrito

## 1. Informacion general
- HU: `HU-002`
- Nombre: Agregar producto al carrito
- Microservicio: `shopping-cart`
- Estado: Implementada en backend, gateway y base de datos
- Rama de trabajo sugerida: `HU-002-back-dev`

## 2. Objetivo de la HU
Implementar en el backend de `shopping-cart` un metodo que permita agregar un producto a un carrito de compras ya existente.

La HU base indica que el sistema debe:
- permitir agregar un producto al carrito
- registrar identificador del producto, nombre, cantidad y precio
- actualizar la cantidad si el producto ya existe en el carrito
- calcular correctamente el subtotal del item
- persistir la informacion en la base de datos

Esta implementacion debe apoyarse sobre la base ya creada en `HU-001`, donde el carrito ya existe como entidad principal y ahora necesita una estructura para manejar productos asociados.

## 3. Justificacion funcional
Actualmente el backend ya puede crear carritos, pero aun no dispone de la logica necesaria para agregar productos dentro de ellos.

Esto genera un vacio funcional porque:
- crear el carrito sin items no resuelve el flujo real de compra
- el usuario necesita construir una lista de productos seleccionados
- el sistema debe llevar control por producto, cantidad y precio
- se debe evitar duplicar lineas del mismo producto cuando la regla funcional indica actualizar cantidad
- el subtotal por item es necesario para futuras HU de consulta y calculo de total

Por lo tanto, esta HU no consiste solo en insertar un registro.  
Tambien define como se comporta el carrito cuando recibe productos repetidos y como se mantiene la coherencia de los montos parciales.

## 4. Justificacion del microservicio seleccionado
La HU debe implementarse en el microservicio `backend` del proyecto `shopping_cart` porque alli reside la logica principal del dominio carrito.

Segun la estructura actual del repositorio, este microservicio ya contiene:
- `CartController`
- `ICartService`
- `CartServiceImpl`
- `CartRepository`
- `Cart`
- DTOs y manejo de errores

Esto permite extender la arquitectura actual para incorporar una nueva entidad o modelo asociado al carrito, por ejemplo un item del carrito, sin romper la separacion en capas ya construida.

Ademas, el `gateway` debe participar para exponer el endpoint al cliente, y la base de datos debe almacenar la nueva relacion entre carrito y productos agregados.

## 5. Necesidad funcional observada
La HU indica que el usuario debe poder agregar productos al carrito antes de confirmar la compra.

Eso obliga a definir en backend:
- como se representa un item del carrito en la base de datos
- como se identifica el carrito destino
- que datos minimos llegan para agregar un producto
- como se comporta el sistema si el producto ya esta agregado
- como se calcula y actualiza el subtotal del item
- que respuesta se devuelve luego de agregar o actualizar el producto

La necesidad principal es que el backend mantenga un estado consistente del carrito a medida que se van acumulando productos.

## 6. Regla funcional principal
Cada vez que se solicite agregar un producto a un carrito:

1. el cliente envia el `cartId`
2. el cliente envia los datos del producto: identificador, nombre, cantidad y precio
3. el backend valida que el carrito exista y este activo
4. el backend valida que la cantidad sea mayor a `0`
5. el backend valida que el precio sea valido y no negativo
6. el backend busca si el producto ya existe dentro del carrito
7. si el producto no existe, crea un nuevo item
8. si el producto ya existe, actualiza la cantidad acumulada
9. el backend recalcula el subtotal del item como `cantidad * precio`
10. la informacion se persiste en PostgreSQL
11. el sistema retorna la informacion actualizada del item agregado al carrito

## 7. Comportamiento esperado del backend
El metodo propuesto debe:
- recibir una solicitud `POST`
- validar que el `cartId` exista
- validar que `productId` sea obligatorio
- validar que `name` sea obligatorio
- validar que `quantity` sea mayor a `0`
- validar que `price` sea mayor o igual a `0`
- crear el item cuando el producto aun no esta en el carrito
- actualizar la cantidad cuando el producto ya existe en el carrito
- recalcular el subtotal despues de cada operacion
- persistir el resultado en base de datos

Resultado esperado:
- el carrito puede almacenar multiples productos
- un mismo producto no necesita repetirse en varias filas si la regla final decide consolidarlo
- cada item conserva su subtotal actualizado
- el backend deja preparado el carrito para futuras HU de consulta, totalizacion o eliminacion de items

## 8. Alcance funcional propuesto
Se propone crear un endpoint especializado para agregar productos a un carrito existente.

El endpoint debe permitir:
- agregar un producto a un carrito por `cartId`
- registrar `productId`, `name`, `quantity` y `price`
- calcular y almacenar el subtotal del item
- actualizar la cantidad si el producto ya existe en el carrito
- persistir la informacion en PostgreSQL

No hace parte de esta HU:
- eliminar productos del carrito
- disminuir cantidades manualmente
- calcular el total global del carrito
- validar stock contra otro microservicio externo
- cerrar o confirmar la compra

## 9. Endpoint propuesto
### Consumo oficial por gateway
- Metodo: `POST`
- URL propuesta: `http://localhost:8080/api/v1/carts/{cartId}/items`

### Endpoint interno del microservicio
- Metodo: `POST`
- URL propuesta: `http://localhost:8081/api/v1/carts/{cartId}/items`

La ruta propuesta se alinea con la HU original, donde el producto se agrega como un recurso hijo del carrito.

## 10. Payload propuesto
```json
{
  "productId": 101,
  "name": "Mouse Logitech G203",
  "quantity": 2,
  "price": 85000
}
```

## 11. Validaciones de negocio propuestas
El backend debe validar como minimo:

1. `cartId` es obligatorio y debe existir
2. el carrito debe encontrarse en estado activo si esa regla se mantiene desde `HU-001`
3. `productId` es obligatorio
4. `name` es obligatorio y no debe llegar vacio
5. `quantity` es obligatoria y debe ser mayor a `0`
6. `price` es obligatorio y debe ser mayor o igual a `0`
7. si el producto ya existe en el carrito, no se debe crear un duplicado innecesario si la regla final es consolidar items
8. el subtotal debe recalcularse con base en la cantidad final del item
9. la persistencia debe mantener la relacion correcta entre carrito e items

## 12. Modelo de datos propuesto
Para esta HU se propone incorporar una entidad `CartItem` con una estructura inicial como:
- `id`
- `cartId`
- `productId`
- `name`
- `quantity`
- `price`
- `subtotal`
- `createdAt`
- `updatedAt`

Relaciones sugeridas:
- un `Cart` puede tener muchos `CartItem`
- un `CartItem` pertenece a un unico `Cart`

Regla sugerida:
- `subtotal` se calcula como `quantity * price`

Esto deja preparado el modelo para futuras HU como listar carrito, actualizar cantidades o calcular total general.

## 13. Trazabilidad tecnica propuesta
Tomando como base la estructura actual del backend, la implementacion deberia organizarse asi:

- `controller/CartController.java`
  - exponer `POST /api/v1/carts/{cartId}/items`
- `service/ICartService.java`
  - definir el contrato para agregar productos al carrito
- `service/CartServiceImpl.java`
  - aplicar validaciones
  - buscar carrito existente
  - crear o actualizar item
  - recalcular subtotal
- `repository/CartRepository.java`
  - apoyar consulta del carrito
- `repository/CartItemRepository.java`
  - consultar y persistir items por carrito y producto
- `entity/Cart.java`
  - mapear la relacion con items
- `entity/CartItem.java`
  - representar la tabla de items del carrito
- `dto/AddCartItemRequestDTO.java`
  - recibir `productId`, `name`, `quantity` y `price`
- `dto/CartItemResponseDTO.java`
  - retornar la informacion del item agregado o actualizado
- `exception/GlobalExceptionHandler.java`
  - centralizar respuestas de error

## 14. Configuracion de base de datos propuesta
El backend ya se encuentra configurado para PostgreSQL.

Segun la configuracion actual del proyecto, la conexion esta preparada sobre el puerto `5020`.

Configuracion de referencia observada:
- motor: `PostgreSQL`
- host: `localhost`
- puerto: `5020`
- base de datos: `shopping_cart_db`

URL de referencia:
- `jdbc:postgresql://localhost:5020/shopping_cart_db`

Para esta HU se requiere:
- crear o actualizar la tabla de items del carrito
- asegurar la llave foranea hacia la tabla de carritos
- mantener consistencia en auditoria basica si se usan fechas de creacion y actualizacion

## 15. Contrato de respuesta propuesto
Respuesta sugerida:

```json
{
  "id": 1,
  "cartId": 5,
  "productId": 101,
  "name": "Mouse Logitech G203",
  "quantity": 3,
  "price": 85000,
  "subtotal": 255000
}
```

Si el producto ya existia previamente en el carrito:
- la respuesta debe reflejar la nueva cantidad acumulada
- el subtotal debe corresponder a esa nueva cantidad final

Esto permite que el frontend o el gateway conozcan el estado actualizado del item inmediatamente despues de la operacion.

## 16. Implementacion tecnica sugerida
- `CartController`
  - crear endpoint `POST /api/v1/carts/{cartId}/items`
- `ICartService`
  - definir metodo para agregar un producto al carrito
- `CartServiceImpl`
  - validar `cartId`
  - validar request del item
  - consultar si el producto ya existe en el carrito
  - crear o actualizar el item
  - recalcular subtotal
- `CartItemRepository`
  - crear consulta por `cartId` y `productId`
- `Cart`
  - mapear relacion `one-to-many` con items
- `CartItem`
  - crear entidad JPA del item del carrito
- `AddCartItemRequestDTO` y `CartItemResponseDTO`
  - separar entrada y salida
- `GlobalExceptionHandler`
  - devolver errores controlados
- `application.yaml`
  - conservar configuracion actual de PostgreSQL
- `gateway`
  - exponer o enrutar la nueva operacion hacia backend

## 17. Criterios de aceptacion propuestos
1. Debe existir un endpoint backend para agregar productos a un carrito existente.
2. El endpoint debe recibir `productId`, `name`, `quantity` y `price`.
3. El sistema debe validar que el carrito exista.
4. La cantidad del producto debe ser mayor a `0`.
5. El precio del producto debe ser valido.
6. Si el producto no existe en el carrito, el sistema debe crear un nuevo item.
7. Si el producto ya existe en el carrito, el sistema debe actualizar la cantidad del item existente.
8. El subtotal del item debe calcularse correctamente.
9. La informacion debe persistirse en la base de datos.
10. La respuesta debe devolver el item agregado o actualizado.

## 18. Archivos candidatos a modificacion
- `backend/src/main/java/shopping_cart/backend/controller/CartController.java`
- `backend/src/main/java/shopping_cart/backend/service/ICartService.java`
- `backend/src/main/java/shopping_cart/backend/service/CartServiceImpl.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartRepository.java`
- `backend/src/main/java/shopping_cart/backend/repository/CartItemRepository.java`
- `backend/src/main/java/shopping_cart/backend/entity/Cart.java`
- `backend/src/main/java/shopping_cart/backend/entity/CartItem.java`
- `backend/src/main/java/shopping_cart/backend/dto/AddCartItemRequestDTO.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartItemResponseDTO.java`
- `backend/src/main/java/shopping_cart/backend/exception/GlobalExceptionHandler.java`
- `backend/src/main/resources/application.yaml`
- `gateway/src/main/resources/application.yaml`
- `database/init.sql`
- `backend/src/test/java/...`

## 19. Riesgos o validaciones previas
- Confirmar si la cantidad al repetir producto debe acumularse o reemplazarse.
- Confirmar si `price` llega desde frontend como fuente confiable o si luego debera consultarse desde un catalogo externo.
- Confirmar si `name` debe persistirse como snapshot del producto o si solo debe guardarse `productId`.
- Validar si el subtotal debe manejarse con `BigDecimal` para mayor precision monetaria.
- Confirmar si la unicidad funcional del item sera por `cartId + productId`.

## 20. Estado de este documento
Este documento deja trazada la implementacion funcional y tecnica de la `HU-002 - Agregar producto al carrito`, alineada con:
- la HU original del proyecto
- el formato de cambios ya usado en `HU-001`
- la estructura tipo Spring Boot actual del backend
- la configuracion existente de gateway y PostgreSQL
- la base necesaria para desarrollar futuras HU relacionadas con consulta, actualizacion y totalizacion del carrito
