# Estructura General del Proyecto

Este documento centraliza la documentacion de la estructura propuesta para el proyecto `shopping_cart`. A diferencia del ejemplo de arquitectura monorepo mostrado en clase, en este caso el proyecto estara compuesto por un solo microservicio principal, acompañado por su frontend, la base de datos y la configuracion necesaria para Docker. Para el desarrollo del backend se utilizara el framework `Spring Boot`.

## 1. Proposito de la Estructura

La finalidad de esta estructura es organizar claramente cada parte del sistema para que el desarrollo sea mas facil de entender, mantener y escalar. Aunque se trabajara con un unico microservicio, se conservara una organizacion tipo monorepo para separar responsabilidades y facilitar el trabajo por modulos.

## 2. Estructura Propuesta del Proyecto

```text
shopping_cart/
├── README.md
├── docker-compose.yml
├── .github/
│   └── workflows/
│       └── ci.yml
├── Doc/
│   ├── introduccion.md
│   └── estructura-proyecto.md
├── backend/                         (Shopping Cart Microservice - Spring Boot)
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │           └── application.yml
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                        (User Interface)
│   ├── src/
│   ├── package.json
│   └── Dockerfile
└── database/                        (Database configuration)
    ├── init/
    │   └── init.sql
    └── README.md
```

## 3. Descripcion de Cada Parte

### `README.md`

Archivo principal del proyecto. Aqui se incluira una vista general del sistema, instrucciones de ejecucion, tecnologias utilizadas y pasos de instalacion.

### `docker-compose.yml`

Permitira levantar todos los servicios necesarios del proyecto en conjunto, por ejemplo:

- Backend
- Frontend
- Base de datos

Esto facilitara las pruebas locales y el despliegue del entorno completo.

### `.github/workflows/`

Contendra los archivos de integracion continua del proyecto. En esta carpeta se podran definir validaciones automaticas como pruebas, construccion del proyecto o revisiones basicas de calidad.

### `Doc/`

Carpeta destinada a reunir la documentacion del proyecto. Aqui se almacenaran los archivos explicativos de cada fase del desarrollo, con el objetivo de que cualquier persona pueda entender como se construyo el microservicio paso a paso.

### `backend/`

Esta carpeta contendra el microservicio principal de `shopping_cart`, desarrollado con `Spring Boot`. Aqui se construira toda la logica relacionada con el carrito de compras, incluyendo:

- Gestion de productos agregados al carrito
- Actualizacion de cantidades
- Eliminacion de productos
- Consulta del contenido del carrito
- Calculo de totales
- Conexion con la base de datos
- Exposicion de endpoints para el frontend o para otros servicios

Dentro de esta carpeta tambien se manejara la estructura tipica de un proyecto Spring Boot, como:

- Codigo fuente en `src/main/java`
- Configuracion en `src/main/resources/application.yml`
- Dependencias y construccion con `pom.xml`
- Contenerizacion mediante `Dockerfile`

Como este proyecto tendra un solo microservicio, esta sera la pieza central del sistema.

### `frontend/`

Esta carpeta contendra la interfaz visual del proyecto. Su objetivo sera permitir que el usuario pueda interactuar con el carrito de compras de forma sencilla, consultando productos agregados, actualizando cantidades, eliminando elementos y visualizando el total acumulado.

### `database/`

Esta carpeta estara destinada a la configuracion de la base de datos del proyecto. Aqui podran incluirse:

- Scripts de inicializacion
- Creacion de tablas
- Datos de prueba si son necesarios
- Documentacion relacionada con la persistencia

Dado que el proyecto tambien contempla la base de datos como una parte fundamental, esta carpeta ayudara a mantener separada la configuracion de persistencia respecto al backend y al frontend.

## 4. Observacion Importante

Aunque el modelo de referencia presentado en clase muestra varios microservicios, en nuestro proyecto solo se implementara uno: el microservicio de `shopping_cart`. Sin embargo, se mantendra una estructura ordenada de tipo monorepo para que el sistema sea mas entendible y para que en el futuro pueda ampliarse si se desea integrar nuevos servicios.

## 5. Beneficios de Esta Organizacion

- Facilita la comprension general del proyecto.
- Separa claramente backend, frontend y base de datos.
- Permite documentar el desarrollo de forma ordenada.
- Mejora el mantenimiento del codigo.
- Hace mas sencillo el uso de Docker para levantar el entorno completo.
- Deja una base preparada para futuras ampliaciones.

## 6. Conclusion

La estructura propuesta para `shopping_cart` busca adaptar la idea de monorepo del curso a un caso mas simple y enfocado: un solo microservicio con su frontend, su base de datos y su configuracion de despliegue. Esto permite mantener una organizacion profesional del proyecto sin perder claridad en el proceso de aprendizaje.
