# HU-008 - Crear la base de datos del proyecto shopping cart

## 1. Informacion general
- HU: `HU-008`
- Nombre: Crear la base de datos del proyecto shopping cart
- Componente: `database`
- Estado: Implementada con estructura versionada en `Liquibase`
- Rama de trabajo sugerida: `HU-008-db-dev`

## 2. Objetivo de la HU
Crear y estructurar la base de datos del proyecto `shopping cart` siguiendo como guia la arquitectura del repositorio `shopping-cart-db`, pero adaptada al dominio actual del carrito.

La HU base indica que el sistema debe:
- crear la base del proyecto de base de datos
- definir la estructura inicial del esquema
- incluir las tablas principales `carts` y `cart_items`
- soportar la relacion entre carritos y productos agregados
- dejar la base preparada para integracion con el backend `shopping_cart`

La implementacion final no se limito a un archivo SQL plano.  
Se dejo un componente `database` organizado por capas SQL y controlado por `Liquibase`.

## 3. Justificacion funcional
Actualmente el proyecto necesita una base de datos persistente, versionada y trazable para soportar las HU del carrito.

Esto genera una necesidad funcional porque:
- las operaciones del carrito requieren persistencia real en PostgreSQL
- la estructura de datos debe evolucionar sin perder control de cambios
- el equipo necesita separar cambios estructurales, cambios de datos y permisos
- la aplicacion requiere usuarios de base de datos con permisos diferenciados
- las futuras HU deben apoyarse en una base formal y no en scripts aislados

Por lo tanto, esta HU no solo crea tablas.  
Tambien establece la base de administracion del esquema del proyecto.

## 4. Justificacion del componente seleccionado
La HU se implementa en el componente `database` porque alli se centraliza la evolucion de la persistencia del sistema.

Este componente queda responsable de:
- versionar el esquema relacional con `Liquibase`
- organizar la base por capas `DDL`, `DML`, `DCL` y `TCL`
- definir tablas, indices y permisos
- mantener rollback por `changeSet`
- ofrecer un flujo propio de despliegue con `Docker`

La base de datos queda desacoplada del backend como componente tecnico propio, aunque sigue alineada con las entidades persistentes del proyecto.

## 5. Necesidad funcional observada
La HU exige una base de datos lista para el dominio carrito.

Eso obligo a definir:
- una tabla `carts` para el carrito principal
- una tabla `cart_items` para los productos del carrito
- una relacion `1:N` entre carrito e items
- validaciones de integridad sobre cantidades, precios y subtotales
- indices para consultas frecuentes
- usuarios de base de datos para administracion y operacion

Adicionalmente, al tomar como guia la referencia `shopping-cart-db`, fue necesario adoptar una estructura versionada y no solo una carpeta de scripts sueltos.

## 6. Regla funcional principal
Cada vez que el sistema necesite persistir informacion del carrito:

1. debe existir una tabla principal `carts`
2. debe existir una tabla dependiente `cart_items`
3. cada item debe referenciar un carrito valido
4. un mismo usuario solo puede tener un carrito `ACTIVE`
5. un mismo producto no debe repetirse dos veces dentro del mismo carrito
6. los permisos de base de datos deben quedar diferenciados por rol
7. el esquema debe poder desplegarse y revertirse de forma controlada con `Liquibase`

## 7. Comportamiento esperado de la base de datos
La implementacion realizada debe:
- permitir crear carritos
- permitir asociar multiples items a un carrito
- almacenar producto, cantidad, precio y subtotal del item
- soportar integridad referencial entre `carts` y `cart_items`
- dejar indices para mejorar consultas del backend
- definir usuarios de base de datos para administracion y operacion
- dejar rollback separado por cada `changeSet` activo

Resultado esperado:
- el backend puede persistir el dominio carrito en PostgreSQL
- el esquema queda versionado y trazable
- el componente database queda listo para crecer con futuras migraciones

## 8. Alcance funcional implementado
La implementacion de esta HU incluye:
- estructura de proyecto DB inspirada en `shopping-cart-db`
- `Liquibase` como mecanismo principal de versionamiento
- `DDL` para tablas e indices
- `DCL` para roles y grants
- `rollbacks` para los `changeSet` activos
- `docker-compose` propio del componente database
- documentacion tecnica del componente

No hace parte de esta HU:
- datos semilla en `DML`
- vistas materializadas
- funciones
- procedimientos
- triggers
- politicas avanzadas

## 9. Estructura implementada del proyecto database
Tomando como guia la ruta de referencia `D:\escritorio\U\SEMESTRE 8\Sistemas Distribuidos\CORTE 1\Proyecto\shopping-cart-db`, el componente `database` de este proyecto quedo organizado asi:

- `database/changelog-master.yaml`
- `database/01_ddl`
- `database/02_dml`
- `database/03_dcl`
- `database/04_tcl`
- `database/05_rollbacks`
- `database/docker`
- `database/docs`
- `database/scripts`
- `database/docker-compose.yml`
- `database/liquibase.properties.example`
- `database/.env.example`
- `database/README.md`

La estructura activa actual usa:
- `01_ddl/03_tables`
- `01_ddl/09_indexes`
- `03_dcl/00_roles`
- `03_dcl/01_grants`

## 10. Modelo de datos implementado
Para esta HU se implemento una estructura relacional compuesta por:

### Tabla `carts`
- `id`
- `user_id`
- `status`
- `created_at`
- `updated_at`

### Tabla `cart_items`
- `id`
- `cart_id`
- `product_id`
- `name`
- `quantity`
- `price`
- `subtotal`
- `created_at`
- `updated_at`

Reglas implementadas:
- un `cart` puede tener muchos `cart_items`
- cada `cart_item` pertenece a un solo `cart`
- `status` de carrito se restringe a `ACTIVE`, `CHECKED_OUT`, `CANCELLED`

## 11. Validaciones de estructura implementadas
La base de datos valida como minimo:

1. `carts.id` es unico
2. `cart_items.id` es unico
3. `cart_items.cart_id` referencia un carrito existente
4. `quantity` debe ser mayor a `0`
5. `price` debe ser mayor o igual a `0`
6. `subtotal` debe ser mayor o igual a `0`
7. no pueden existir items huerfanos
8. un usuario no puede tener dos carritos `ACTIVE`
9. un mismo producto no puede duplicarse dentro del mismo carrito

## 12. Relacion principal entre tablas
La estructura central de esta HU mantiene una relacion `uno a muchos`:

- un carrito puede contener varios items
- un item pertenece a un solo carrito

Esto deja soportadas las HU funcionales ya implementadas en backend:
- crear carrito
- agregar producto
- consultar carrito
- actualizar cantidad
- eliminar item
- calcular total

## 13. Trazabilidad tecnica implementada
La implementacion del componente `database` quedo organizada asi:

- `database/changelog-master.yaml`
  - orquesta la activacion de capas principales
- `database/01_ddl/changelog.yaml`
  - orquesta cambios estructurales
- `database/01_ddl/03_tables/001_create_cart_tables.sql`
  - crea `carts` y `cart_items`
- `database/01_ddl/09_indexes/001_create_cart_indexes.sql`
  - crea indices del dominio carrito
- `database/03_dcl/00_roles/001_create_app_roles.sql`
  - crea `shopping_cart_admin` y `shopping_cart_employee`
- `database/03_dcl/01_grants/001_grant_app_permissions.sql`
  - define permisos sobre tablas y secuencias
- `database/05_rollbacks/...`
  - contiene reversas por `changeSet`
- `database/docker-compose.yml`
  - levanta PostgreSQL y runner de `Liquibase`
- `database/docker/liquibase/Dockerfile`
  - incorpora el driver de PostgreSQL
- `database/README.md`
  - documenta el uso del componente

## 14. Motor de base de datos implementado
La HU se implemento con:
- motor: `PostgreSQL`
- base de datos: `shopping_cart_db`
- versionamiento: `Liquibase`
- soporte de despliegue: `Docker Compose`

Configuracion asociada:
- `database/docker-compose.yml` usa PostgreSQL aislado para el componente DB
- `database/liquibase.properties.example` permite ejecucion local de `Liquibase`
- `backend/src/main/resources/application.yaml` sigue apuntando a PostgreSQL para consumo de aplicacion

## 15. Scripts y changeSets implementados
El esquema activo queda representado por:

```yaml
databaseChangeLog:
  - include: 01_ddl/changelog.yaml
  - include: 02_dml/changelog.yaml
  - include: 03_dcl/changelog.yaml
  - include: 04_tcl/changelog.yaml
```

Y los cambios activos principales son:
- `001-create-cart-tables`
- `002-create-cart-indexes`
- `003-create-app-roles`
- `004-grant-app-permissions`

Esto deja el componente preparado para crecer por migraciones y no por sobreescritura manual del esquema.

## 16. Implementacion tecnica realizada
- se reorganizo `database` con arquitectura tipo `shopping-cart-db`
- se agrego `Liquibase` como contrato de despliegue de base de datos
- se migraron tablas e indices del carrito a `changeSet`
- se agregaron usuarios `shopping_cart_admin` y `shopping_cart_employee`
- se agregaron grants diferenciados
- se documentaron rollbacks por capa
- se agrego `docker-compose` propio del componente database
- se conservo `database/init.sql` como compatibilidad con el monorepo principal

## 17. Criterios de aceptacion cubiertos
1. Existe un componente de base de datos organizado para el proyecto.
2. Existen las tablas `carts` y `cart_items`.
3. La relacion entre carrito e items queda implementada.
4. La estructura permite almacenar productos asociados a un carrito.
5. La base queda lista para integracion con el backend `shopping_cart`.
6. La solucion usa una base relacional `PostgreSQL`.
7. El componente queda estructurado con `Liquibase` y rollbacks.
8. Existen usuarios de base de datos con permisos diferenciados.

## 18. Archivos creados o modificados
- `database/changelog-master.yaml`
- `database/01_ddl/changelog.yaml`
- `database/01_ddl/03_tables/001_create_cart_tables.sql`
- `database/01_ddl/03_tables/changelog.yaml`
- `database/01_ddl/09_indexes/001_create_cart_indexes.sql`
- `database/01_ddl/09_indexes/changelog.yaml`
- `database/03_dcl/changelog.yaml`
- `database/03_dcl/00_roles/001_create_app_roles.sql`
- `database/03_dcl/00_roles/changelog.yaml`
- `database/03_dcl/01_grants/001_grant_app_permissions.sql`
- `database/03_dcl/01_grants/changelog.yaml`
- `database/05_rollbacks/...`
- `database/docker-compose.yml`
- `database/docker/liquibase/Dockerfile`
- `database/liquibase.properties.example`
- `database/.env.example`
- `database/README.md`
- `database/init.sql`
- `Doc/Changes/HU-008-crear-base-datos.md`

## 19. Riesgos o validaciones previas
- falta ejecutar `liquibase update` real contra una base limpia para validar runtime completo
- `backend` aun usa estrategia propia de JPA y todavia no ha sido ajustado para depender exclusivamente de `Liquibase`
- existen residuos legacy en `database/scripts` que deben eliminarse para evitar doble contrato de estructura
- se debe decidir mas adelante si `init.sql` se elimina cuando el monorepo quede 100% alineado con `Liquibase`

## 20. Estado de este documento
Este documento deja registrada la implementacion de la `HU-008 - Crear la base de datos del proyecto shopping cart`, alineada con:
- la HU original del proyecto
- la estructura de referencia `shopping-cart-db`
- el uso de `Liquibase` como contrato principal
- la separacion por capas `DDL`, `DML`, `DCL`, `TCL`
- la persistencia relacional del dominio carrito
- la preparacion del componente para futuras migraciones y controles de rollback
