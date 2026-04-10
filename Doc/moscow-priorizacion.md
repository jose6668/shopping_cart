# Priorizacion MoSCoW del Proyecto

Este documento presenta la priorizacion `MoSCoW` del proyecto `shopping_cart`. Esta tecnica permite clasificar los requerimientos segun su nivel de importancia dentro del desarrollo, facilitando la planeacion por etapas y ayudando a identificar que elementos son obligatorios, cuales son recomendables, cuales podrian agregarse si hay tiempo y cuales quedaran fuera del alcance actual.

## 1. Objetivo de la Priorizacion

El objetivo de esta priorizacion es organizar el desarrollo del proyecto de forma realista, definiendo claramente que funcionalidades son indispensables para que el sistema cumpla su proposito principal y cuales pueden dejarse para etapas posteriores.

## 2. MoSCoW del Proyecto Shopping Cart

### Must Have

Son los elementos esenciales del proyecto. Sin ellos, el sistema no cumple con su objetivo principal.

- Microservicio `shopping_cart` desarrollado con `Spring Boot`
- API REST basica para gestionar el carrito de compras
- Funcionalidad para agregar productos al carrito
- Funcionalidad para listar los productos del carrito
- Funcionalidad para actualizar la cantidad de productos
- Funcionalidad para eliminar productos del carrito
- Calculo del total del carrito
- Conexion del backend con la base de datos
- Estructura inicial de la base de datos para carritos e items
- API Gateway para centralizar el acceso al microservicio
- Frontend basico para visualizar y gestionar el carrito
- Configuracion inicial con Docker para ejecutar los componentes principales

### Should Have

Son requerimientos importantes, pero no criticos para la primera version funcional del proyecto. Pueden desarrollarse despues de completar lo esencial.

- Validaciones de datos en las solicitudes del backend
- Manejo estandarizado de errores
- Mejor organizacion de endpoints a traves del API Gateway
- Persistencia mas detallada de informacion del carrito
- Mejoras visuales en el frontend
- Configuracion mas completa de Docker Compose
- Documentacion tecnica mas detallada del backend y la base de datos

### Could Have

Son funcionalidades deseables si el tiempo del proyecto lo permite. Mejoran la experiencia o la calidad del sistema, pero no son obligatorias.

- Interfaz frontend mas atractiva y responsiva
- Filtros o busqueda dentro del carrito
- Resumen visual mas detallado del carrito
- Registro simple de actividad del carrito
- Pruebas automatizadas basicas
- Variables de entorno mejor organizadas para despliegue local

### Won't Have (this time)

Son elementos que no se implementaran en esta etapa del proyecto, pero que pueden documentarse como posibles mejoras futuras.

- Sistema de autenticacion y autorizacion de usuarios
- Integracion con pasarela de pagos
- Despliegue en nube publica
- Arquitectura con multiples microservicios adicionales
- Mensajeria asincrona con colas o brokers
- Monitoreo avanzado y dashboard de metricas
- Integracion continua y despliegue continuo

## 3. Conclusion

La priorizacion `MoSCoW` permite enfocar el desarrollo del proyecto `shopping_cart` en lo verdaderamente necesario para construir una primera version funcional, organizada y coherente con los objetivos del curso. De esta manera, se asegura que el esfuerzo inicial se concentre en el backend, el frontend, la base de datos, el API Gateway y Docker, dejando las mejoras avanzadas para futuras etapas.
