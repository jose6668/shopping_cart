# HU-001 - Crear carrito de compras

## 1. Informacion general
- HU: `HU-001`
- Nombre: Crear carrito de compras
- Microservicio: `shopping-cart`
- Estado: Implementada en backend, gateway y base de datos
- Rama de trabajo sugerida: `HU-001--back-dev`

## 2. Objetivo de la HU
Implementar en el backend de `shopping-cart` un metodo que permita crear un carrito de compras asociado a un usuario.

La HU base indica que el sistema debe:
- crear un carrito para un usuario
- asociarlo a un `userId`
- almacenarlo en base de datos
- retornar la informacion basica del carrito creado
- evitar duplicados innecesarios cuando el usuario ya tenga un carrito activo

Esta implementacion debe prepararse siguiendo una estructura de capas tipo Spring Boot como la mostrada en las imagenes de referencia:
- `controller`
- `service`
- `repository`
- `entity`
- `dto`
- `exception`
- `resources`

## 3. Justificacion funcional
Actualmente el backend no tiene implementada la logica para crear carritos.

Esto genera un vacio funcional porque:
- el carrito es la base de todas las HU siguientes
- sin carrito no se pueden agregar productos ni consultar contenido
- el sistema necesita persistir el estado inicial de compra por usuario
- se debe evitar que un mismo usuario genere multiples carritos activos sin control

Por lo tanto, esta HU no solo crea un registro en base de datos.  
Tambien define la regla inicial de existencia del carrito dentro del sistema.

## 4. Justificacion del microservicio seleccionado
La HU debe implementarse en el microservicio `backend` del proyecto `shopping_cart` porque alli residira la logica principal del dominio carrito.

Segun la estructura actual del repositorio y las imagenes de referencia, este microservicio sera responsable de:
- exponer endpoints REST
- aplicar reglas de negocio
- persistir entidades con JPA
- conectarse a PostgreSQL
- responder al gateway y al frontend

Ademas, la HU encaja directamente con la arquitectura en capas observada:
- `CartController` para la capa de presentacion
- `ICartService` y `CartServiceImpl` para la capa de negocio
- `ICartRepository` para la capa de datos
- `Cart` como entidad JPA
- DTOs para request y response
- excepciones para validaciones de negocio

## 5. Necesidad funcional observada
La HU indica que el sistema debe crear un carrito vinculado a un usuario y no duplicar carritos activos.

Eso obliga a definir en backend:
- como se representa un carrito en la base de datos
- que atributo identifica al usuario propietario
- que se considera un carrito activo
- que respuesta devuelve el sistema al crear el carrito
- que hacer si el usuario ya posee un carrito activo

La necesidad principal es que el backend establezca un punto de partida consistente para la vida del carrito.

## 6. Regla funcional principal
Cada vez que se solicite crear un carrito:

1. el cliente envia un `userId`
2. el backend valida que el `userId` sea obligatorio y valido
3. el backend consulta si ya existe un carrito activo para ese usuario
4. si no existe, crea un nuevo carrito
5. el carrito se persiste en PostgreSQL
6. el sistema retorna la informacion basica del carrito creado
7. si ya existe un carrito activo, el sistema evita crear un duplicado innecesario

## 7. Comportamiento esperado del backend
El metodo propuesto debe:
- recibir una solicitud `POST`
- validar que `userId` no llegue nulo o vacio
- verificar si el usuario ya tiene un carrito activo
- crear un nuevo carrito solo cuando corresponda
- almacenar fecha de creacion y fecha de actualizacion
- retornar una respuesta con los datos basicos del carrito

Resultado esperado:
- cada usuario puede iniciar su proceso de compra con un carrito persistido
- el backend mantiene control sobre la unicidad funcional del carrito activo
- el frontend o gateway obtiene una respuesta clara para continuar con las siguientes HU

## 8. Alcance funcional propuesto
Se propone crear un endpoint especializado para registrar la creacion del carrito.

El endpoint debe permitir:
- crear un carrito para un usuario
- asociar el carrito al `userId`
- retornar el carrito creado
- evitar crear un segundo carrito activo para el mismo usuario
- persistir la informacion en PostgreSQL

No hace parte de esta HU:
- agregar productos al carrito
- actualizar cantidades
- eliminar productos
- calcular el total
- cerrar el carrito como compra confirmada

## 9. Endpoint propuesto
### Consumo oficial por gateway
- Metodo: `POST`
- URL propuesta: `http://localhost:8080/api/v1/carts`

### Endpoint interno del microservicio
- Metodo: `POST`
- URL propuesta: `http://localhost:8081/api/v1/carts`

La ruta propuesta se alinea con el contrato funcional descrito en la HU original.

## 10. Payload propuesto
```json
{
  "userId": 15
}
```

## 11. Validaciones de negocio propuestas
El backend debe validar como minimo:

1. `userId` es obligatorio
2. `userId` debe ser un valor numerico positivo
3. no debe existir ya un carrito activo para ese usuario si la regla final de negocio exige unicidad estricta
4. si ya existe un carrito activo, el sistema debe responder de forma controlada
5. la persistencia del carrito debe registrar fechas de auditoria basicas

## 12. Modelo de datos propuesto
Para esta HU se propone una entidad `Cart` con una estructura inicial como:
- `id`
- `userId`
- `status`
- `createdAt`
- `updatedAt`

Regla sugerida:
- `status` puede iniciar en `ACTIVE`

Esto deja preparado el modelo para futuras HU como agregar productos, consultar carrito o calcular total.

## 13. Trazabilidad tecnica propuesta
Tomando como base las imagenes de referencia, la implementacion backend deberia organizarse asi:

- `controller/CartController.java`
  - expone `POST /api/v1/carts`
- `service/ICartService.java`
  - define el contrato de creacion
- `service/CartServiceImpl.java`
  - aplica validaciones y persistencia
- `repository/ICartRepository.java`
  - consulta existencia de carrito activo por `userId`
- `entity/Cart.java`
  - representa la tabla `carts`
- `dto/CartRequestDTO.java`
  - recibe `userId`
- `dto/CartResponseDTO.java`
  - retorna la informacion basica del carrito
- `exception/`
  - maneja reglas de negocio y respuestas controladas

## 14. Configuracion de base de datos propuesta
El backend debe conectarse a PostgreSQL.

Segun tu indicacion, la conexion debe quedar preparada para PostgreSQL en el puerto `5020`.

Configuracion objetivo sugerida en `application.yml`:
- motor: `PostgreSQL`
- host: `localhost`
- puerto: `5020`
- base de datos sugerida: `shopping_cart_db`

URL de referencia:
- `jdbc:postgresql://localhost:5020/shopping_cart_db`

Tambien se debera configurar:
- `username`
- `password`
- `driver-class-name: org.postgresql.Driver`
- `spring.jpa.hibernate.ddl-auto`
- dialecto de PostgreSQL

## 15. Contrato de respuesta propuesto
Respuesta sugerida:

```json
{
  "id": 1,
  "userId": 15,
  "status": "ACTIVE",
  "createdAt": "2026-04-10T17:00:00",
  "updatedAt": "2026-04-10T17:00:00"
}
```

Si ya existe un carrito activo para el usuario, se puede manejar una de estas dos estrategias:
- retornar el carrito existente
- rechazar la solicitud con una respuesta controlada

Para esta HU se recomienda definir primero la regla exacta antes de codificar, aunque la descripcion funcional sugiere evitar duplicados innecesarios.

## 16. Implementacion tecnica sugerida
- `CartController`
  - crear endpoint `POST /api/v1/carts`
- `ICartService`
  - definir metodo de creacion de carrito
- `CartServiceImpl`
  - validar `userId`
  - consultar carrito activo existente
  - crear y persistir el carrito
- `ICartRepository`
  - crear consulta por `userId` y `status`
- `Cart`
  - mapear entidad JPA
- `CartRequestDTO` y `CartResponseDTO`
  - separar entrada y salida
- `GlobalExceptionHandler`
  - centralizar respuestas de error
- `application.yml`
  - configurar PostgreSQL sobre el puerto `5020`

## 17. Criterios de aceptacion propuestos
1. Debe existir un endpoint backend para crear un carrito de compras.
2. El endpoint debe recibir un `userId`.
3. El carrito debe quedar asociado al usuario enviado.
4. El carrito debe almacenarse en PostgreSQL.
5. La respuesta debe retornar la informacion basica del carrito creado.
6. Si el usuario ya tiene un carrito activo, el sistema debe evitar duplicados innecesarios.
7. La implementacion debe seguir una estructura por capas compatible con Spring Boot.

## 18. Archivos candidatos a modificacion
- `backend/src/main/java/shopping_cart/backend/controller/CartController.java`
- `backend/src/main/java/shopping_cart/backend/service/ICartService.java`
- `backend/src/main/java/shopping_cart/backend/service/CartServiceImpl.java`
- `backend/src/main/java/shopping_cart/backend/repository/ICartRepository.java`
- `backend/src/main/java/shopping_cart/backend/entity/Cart.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartRequestDTO.java`
- `backend/src/main/java/shopping_cart/backend/dto/CartResponseDTO.java`
- `backend/src/main/java/shopping_cart/backend/exception/GlobalExceptionHandler.java`
- `backend/src/main/resources/application.yml`
- `backend/src/test/java/...`

## 19. Riesgos o validaciones previas
- Confirmar si la regla final sera permitir un solo carrito activo por usuario o reutilizar el ya existente.
- Confirmar si `userId` sera solo un identificador numerico externo o si luego tendra validacion contra otro microservicio.
- Confirmar si el estado inicial del carrito sera `ACTIVE` o algun otro valor de negocio.
- Confirmar si las fechas se manejaran con `LocalDateTime`.
- Validar que el archivo de configuracion del backend quede en `application.yml` y no en otro formato.

## 20. Estado de este documento
Este documento deja definida la propuesta funcional y tecnica inicial para implementar la `HU-001 - Crear carrito de compras`, alineada con:
- la HU original del proyecto
- la estructura tipo Spring Boot mostrada en las imagenes
- la necesidad de persistencia en PostgreSQL usando el puerto `5020`
- la base necesaria para desarrollar las siguientes HU del carrito
