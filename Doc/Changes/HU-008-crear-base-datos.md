# HU-008 - Crear la base de datos del proyecto shopping cart

## 1. Informacion general
- HU: `HU-008`
- Nombre: Crear la base de datos del proyecto shopping cart
- Componente: `database`
- Estado: Propuesta funcional para implementacion de estructura relacional inicial
- Rama de trabajo sugerida: `HU-008-db-dev`

## 2. Objetivo de la HU
Crear y estructurar la base de datos del proyecto `shopping cart` en la ruta `D:\escritorio\U\SEMESTRE 8\Sistemas Distribuidos\CORTE 1\Proyecto\shopping-cart-db`.

La HU base indica que el sistema debe:
- crear la base del proyecto de base de datos en la ruta indicada
- definir una estructura inicial para scripts de creacion
- incluir las tablas principales `cart` y `cart_item`
- soportar la relacion entre carritos y productos agregados
- dejar la base preparada para integracion posterior con el backend `shopping_cart`

La finalidad de esta HU es dejar una base relacional organizada y extensible para soportar la persistencia del dominio carrito.

## 3. Justificacion funcional
Actualmente el proyecto requiere una capa de persistencia clara para almacenar la informacion del carrito y de sus productos.

Esto genera una necesidad funcional porque:
- las HU del carrito requieren persistencia real y no solo manejo temporal en memoria
- el sistema debe conservar la relacion entre un carrito y multiples items
- el backend necesita una estructura base consistente para consultar, crear y actualizar datos
- el calculo de totales depende de informacion almacenada de forma confiable
- futuras HU necesitan partir de un modelo de datos ya definido

Por lo tanto, esta HU no consiste solamente en crear tablas.  
Tambien establece la base estructural para el comportamiento completo del carrito de compras.

## 4. Justificacion del componente seleccionado
La HU debe implementarse en el componente `database` porque alli residira la persistencia principal del proyecto `shopping_cart`.

Este componente sera responsable de:
- definir el esquema relacional inicial
- modelar la entidad de carrito
- modelar la entidad de items del carrito
- establecer claves primarias y foraneas
- dejar scripts reutilizables para despliegue o inicializacion

La base de datos sera el soporte comun para las operaciones que luego ejecutara el backend.

## 5. Necesidad funcional observada
La HU indica que deben existir al menos las tablas `cart` y `cart_item`.

Eso obliga a definir como minimo:
- como se identifica un carrito
- como se asocia un carrito a un usuario
- como se almacenan los productos agregados
- como se relaciona cada item con su carrito padre
- que campos permiten calcular subtotales y totales posteriormente

La necesidad principal es contar con una estructura inicial simple, clara y alineada con las HU ya definidas del carrito.

## 6. Regla funcional principal
Cada vez que el sistema necesite persistir informacion del carrito:

1. debe existir una tabla principal `cart`
2. debe existir una tabla dependiente `cart_item`
3. cada carrito debe identificarse de forma unica
4. cada item debe pertenecer a un carrito valido
5. la relacion entre ambas tablas debe mantenerse por clave foranea
6. la estructura debe permitir registrar productos, cantidades y precios
7. la base debe quedar lista para ser consumida por el backend sin rehacer el modelo

## 7. Comportamiento esperado de la base de datos
La estructura propuesta debe:
- permitir crear carritos
- permitir asociar multiples items a un carrito
- almacenar informacion minima necesaria del producto dentro del item
- soportar operaciones de consulta, insercion, actualizacion y eliminacion
- mantener integridad referencial entre `cart` y `cart_item`
- servir como base para el calculo posterior del total del carrito

Resultado esperado:
- un carrito puede existir sin perder su trazabilidad basica
- un carrito puede contener multiples productos
- los items no pueden existir sin un carrito asociado
- la estructura queda preparada para futuras ampliaciones del dominio

## 8. Alcance funcional propuesto
Se propone crear la base inicial del proyecto de datos con:
- estructura de carpetas para scripts
- script de creacion de tablas principales
- claves primarias para `cart` y `cart_item`
- clave foranea de `cart_item` hacia `cart`
- campos minimos para soportar las HU del carrito

No hace parte de esta HU:
- procedimientos almacenados avanzados
- datos de prueba complejos
- auditoria avanzada
- migraciones historicas de versiones previas
- integracion completa con backend dentro de este mismo HU

## 9. Estructura propuesta del proyecto database
La ruta objetivo indicada por la HU es:

- `D:\escritorio\U\SEMESTRE 8\Sistemas Distribuidos\CORTE 1\Proyecto\shopping-cart-db`

Estructura sugerida:
- `shopping-cart-db/scripts`
- `shopping-cart-db/scripts/schema`
- `shopping-cart-db/scripts/data`
- `shopping-cart-db/README.md`

La carpeta `schema` debe contener el script principal de creacion de tablas y la carpeta `data` puede reservarse para semillas futuras si luego son necesarias.

## 10. Modelo de datos propuesto
Para esta HU se propone una estructura relacional inicial compuesta por:

### Tabla `cart`
- `id`
- `user_id`
- `status`
- `created_at`
- `updated_at`

### Tabla `cart_item`
- `id`
- `cart_id`
- `product_id`
- `name`
- `price`
- `quantity`
- `subtotal`
- `created_at`
- `updated_at`

Regla sugerida:
- un `cart` puede tener muchos `cart_item`
- cada `cart_item` pertenece a un solo `cart`

## 11. Validaciones de estructura propuestas
La base de datos debe validar como minimo:

1. `cart.id` debe ser unico
2. `cart_item.id` debe ser unico
3. `cart_item.cart_id` debe referenciar un carrito existente
4. `quantity` debe ser mayor a `0`
5. `price` debe ser mayor o igual a `0`
6. `subtotal` debe ser mayor o igual a `0`
7. no deben existir items huerfanos sin carrito asociado
8. las columnas clave no deben permitir valores nulos cuando afecten la integridad del modelo

## 12. Relacion principal entre tablas
La estructura central de esta HU se basa en una relacion `uno a muchos`:

- un carrito puede contener varios items
- un item pertenece a un solo carrito

Ejemplo funcional:
- `cart`
  - `id: 1`
  - `user_id: 15`
- `cart_item`
  - item 1 asociado a `cart_id: 1`
  - item 2 asociado a `cart_id: 1`
  - item 3 asociado a `cart_id: 1`

Esto deja preparada la persistencia para las HU de agregar productos, consultar carrito, actualizar cantidades, eliminar items y calcular total.

## 13. Trazabilidad tecnica propuesta
Tomando como base la HU y el estilo del proyecto, la implementacion del componente database deberia organizarse asi:

- `scripts/schema/001_create_cart_table.sql`
  - crea la tabla principal `cart`
- `scripts/schema/002_create_cart_item_table.sql`
  - crea la tabla `cart_item`
- `scripts/schema/003_constraints_indexes.sql`
  - define llaves foraneas e indices basicos
- `README.md`
  - documenta como crear la base y ejecutar scripts

Si se decide consolidar todo en un solo archivo inicial, tambien es valido dejar un script unico de esquema mientras conserve claridad y separacion logica.

## 14. Motor de base de datos propuesto
La HU original define una base de datos de tipo relacional.

Para mantener consistencia con el resto del proyecto, se sugiere trabajar con `PostgreSQL`.

Configuracion objetivo sugerida:
- motor: `PostgreSQL`
- base de datos sugerida: `shopping_cart_db`
- tablas principales: `cart`, `cart_item`
- soporte para claves foraneas e indices

La definicion final del puerto, credenciales y estrategia de despliegue puede alinearse despues con el backend cuando se haga la integracion.

## 15. Script base esperado
La base debe quedar preparada para soportar una creacion inicial como:

```sql
CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE cart_item (
    id BIGSERIAL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(12,2) NOT NULL,
    quantity INTEGER NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_cart_item_cart
        FOREIGN KEY (cart_id) REFERENCES cart(id)
);
```

Este ejemplo deja representada la estructura minima necesaria para iniciar el proyecto de base de datos.

## 16. Implementacion tecnica sugerida
- crear el proyecto `shopping-cart-db` en la ruta indicada
- definir la carpeta `scripts`
- crear scripts de esquema para `cart`
- crear scripts de esquema para `cart_item`
- agregar restricciones de integridad referencial
- agregar indices basicos sobre columnas de relacion y consulta frecuente
- documentar en `README.md` la forma de inicializar el esquema

## 17. Criterios de aceptacion propuestos
1. Debe existir el proyecto de base de datos en la ruta indicada por la HU.
2. Debe existir una estructura inicial de scripts para creacion del esquema.
3. Debe existir la tabla `cart`.
4. Debe existir la tabla `cart_item`.
5. La tabla `cart_item` debe estar relacionada con `cart`.
6. La estructura debe permitir almacenar productos asociados a un carrito.
7. La base de datos debe quedar lista para integracion posterior con el backend `shopping_cart`.
8. El esquema debe corresponder a una base de datos relacional.

## 18. Archivos candidatos a creacion
- `D:\escritorio\U\SEMESTRE 8\Sistemas Distribuidos\CORTE 1\Proyecto\shopping-cart-db\README.md`
- `D:\escritorio\U\SEMESTRE 8\Sistemas Distribuidos\CORTE 1\Proyecto\shopping-cart-db\scripts\schema\001_create_cart_table.sql`
- `D:\escritorio\U\SEMESTRE 8\Sistemas Distribuidos\CORTE 1\Proyecto\shopping-cart-db\scripts\schema\002_create_cart_item_table.sql`
- `D:\escritorio\U\SEMESTRE 8\Sistemas Distribuidos\CORTE 1\Proyecto\shopping-cart-db\scripts\schema\003_constraints_indexes.sql`
- `Doc/Changes/HU-008-crear-base-datos.md`

## 19. Riesgos o validaciones previas
- Confirmar si el motor final sera `PostgreSQL` u otro motor relacional.
- Validar si `subtotal` se almacenara fisicamente o se calculara en consultas posteriores.
- Confirmar si el campo `status` del carrito se necesita desde esta HU o puede dejarse para una etapa siguiente.
- Definir si los timestamps se manejaran con zona horaria o sin zona horaria.
- Validar si el proyecto `shopping-cart-db` tendra scripts separados por version o un unico script inicial.

## 20. Estado de este documento
Este documento deja definida la propuesta funcional y tecnica inicial para implementar la `HU-008 - Crear la base de datos del proyecto shopping cart`, alineada con:
- la HU original del proyecto
- el formato documental usado en los archivos de `Changes`
- la necesidad de una base relacional para persistir carritos e items
- la preparacion del esquema para integracion posterior con el backend `shopping_cart`
