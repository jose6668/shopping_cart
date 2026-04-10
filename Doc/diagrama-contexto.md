# Diagrama de Contexto del Proyecto

Este documento presenta el diagrama de contexto del proyecto `shopping_cart`, adaptado a la arquitectura definida para este mini proyecto. A diferencia del ejemplo mostrado en clase, en nuestro caso no se trabajara con varios microservicios, sino con un unico microservicio principal encargado de la gestion del carrito de compras.

## 1. Objetivo del Diagrama de Contexto

El diagrama de contexto permite visualizar de forma general como se relacionan los componentes principales del sistema. En este proyecto, ayuda a comprender como interactuan:

- El usuario
- El frontend
- El microservicio `shopping_cart`
- La base de datos
- Docker como entorno de ejecucion

## 2. Diagrama de Contexto (ASCII)

```text
+-------------------+
|      USUARIO      |
|-------------------|
| - Agrega productos|
| - Consulta carrito|
| - Actualiza items |
| - Elimina items   |
+---------+---------+
          |
          | HTTP
          v
+----------------------------+
|          FRONTEND          |
|----------------------------|
| - Interfaz de usuario      |
| - Visualiza el carrito     |
| - Envio de solicitudes API |
+-------------+--------------+
              |
              | REST API / JSON
              v
+---------------------------------------+
|   SHOPPING CART MICROSERVICE          |
|      (Spring Boot Backend)            |
|---------------------------------------|
| - Agregar producto al carrito         |
| - Listar productos del carrito        |
| - Actualizar cantidad                 |
| - Eliminar producto                   |
| - Calcular total                      |
| - Reglas de negocio                   |
+------------------+--------------------+
                   |
                   | SQL / JPA
                   v
+----------------------------+
|        BASE DE DATOS       |
|----------------------------|
| - Carritos                 |
| - Items del carrito        |
| - Persistencia de datos    |
+----------------------------+

+----------------------------+
|           DOCKER           |
|----------------------------|
| - Contenedor frontend      |
| - Contenedor backend       |
| - Contenedor base de datos |
+----------------------------+
```

## 3. Explicacion del Contexto

### `Usuario`

Es la persona que interactua con el sistema. Desde la interfaz podra gestionar el carrito de compras, agregando productos, consultando su contenido, actualizando cantidades o eliminando elementos.

### `Frontend`

Representa la capa visual del proyecto. Su funcion es permitir la interaccion del usuario con el sistema y enviar las solicitudes necesarias al backend por medio de peticiones HTTP.

### `Shopping Cart Microservice`

Es el nucleo del proyecto y estara desarrollado con `Spring Boot`. Este microservicio procesara la logica del carrito de compras, recibira solicitudes del frontend y gestionara la informacion que sera almacenada en la base de datos.

### `Base de Datos`

Se encargara de persistir la informacion del carrito y de los productos asociados a este. Su funcion sera garantizar que los datos del sistema se mantengan disponibles y organizados.

### `Docker`

Permitira ejecutar todos los componentes del proyecto de forma controlada y consistente. A traves de contenedores se podra levantar el frontend, el backend y la base de datos como un entorno unificado.

## 4. Flujo General del Sistema

El flujo general del proyecto sera el siguiente:

1. El usuario interactua con el frontend.
2. El frontend envia solicitudes al microservicio `shopping_cart`.
3. El backend procesa la logica de negocio.
4. El microservicio consulta o actualiza la base de datos.
5. La respuesta regresa al frontend para mostrarse al usuario.
6. Docker facilita la ejecucion integrada de todos los componentes.

## 5. Conclusion

Este diagrama de contexto resume la relacion principal entre los actores y componentes del proyecto `shopping_cart`. Su objetivo es servir como base conceptual para entender como se conectan las distintas partes del sistema antes de entrar al desarrollo tecnico de cada modulo.
