# ReserveFlight
Módulo de Gestión de Reservas de un vuelo.

[![CI/CD Pipeline](https://github.com/Brahian2215/ReserveFlight/actions/workflows/build.yml/badge.svg)](https://github.com/Brahian2215/ReserveFlight/actions/workflows/build.yml)

## Descripción
Este módulo permite gestionar las reservas de vuelos de una aerolínea. 

Permite realizar las siguientes operaciones:

### Reservation
- Crear una reserva individual de un vuelo.
- Crear una reserva de un vuelo para varios pasajeros.
- Consultar una reserva de vuelo.
- Listar las reservas con su vuelo asociado.
- Modificar una reserva.
- Eliminar una reserva.

### Flight
- Crear un vuelo.
- Consultar un vuelo.
- Listar los vuelos.
- Modificar un vuelo sin reservas (exclude flightNumber).
- Modificar un vuelo con reservas (only flightNumber).
- Eliminar un vuelo sin reservas.

### Passenger
- Crear un pasajero.
- Consultar un pasajero.
- Listar los pasajeros.
- Modificar un pasajero.
- Eliminar un pasajero.

### ReservationPassenger
- Al crear una Reserva, se crean las ReservationPassenger (asocia Pasajeros con Reservas).
- Al modificar una Reserva, se modifican las ReservationPassenger.
- Al eliminar una Reserva, se eliminan las ReservationPassenger.
- Consultar una ReservationPassenger.
- Listar las ReservationPassenger.

## Tecnologías
- Spring Boot, Maven y Java 17.
- Spring GraphQL y GraphiQL.
- PostgresSQL
- JUnit, Mockito y JaCoCo.
- GitHub Actions, SonarCloud y Docker Compose.

## Arquitectura del Proyecto
La aplicación contempla los principios de diseño SOLID, DRY, KISS, YAGNI.

El proyecto está estructurado en capas, siguiendo el patrón de Clean Architecture. 

- **Persistence**: Entidades de la aplicación y repositorio.
- **Presentation**: Controladores y DTOs.
- **Service**: Lógica de negocio y excepciones.
- **Util**: Clases de utilería.
- **Config**: Configuración de la aplicación.
- **GraphQL**: Inputs Types para Mutations con GraphQL.

En la carpeta `src/main/java` se encuentran los paquetes de la aplicación y la clase `ReserveFlightApplication` que inicia la aplicación.

En la carpeta `src/main/resources` se encuentran los archivos de configuración de la aplicación y el archivo `schema.graphqls` que contiene el esquema de GraphQL.

En la carpeta `src/test/java` se encuentran los paquetes de las pruebas unitarias y de integración.

En la carpeta `target/site/jacoco/index.html` se encuentra el reporte de cobertura de código generado por JaCoCo.

En el archivo `pom.xml` se encuentran las dependencias del proyecto y los plugins de Maven.

En la carpeta `.github/workflows` se encuentra el archivo `build.yml` que contiene el flujo de trabajo de GitHub Actions.