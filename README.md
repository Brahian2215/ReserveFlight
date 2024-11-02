# ReserveFlight
Módulo de Gestión de Reservas de un vuelo.

[![CI/CD Pipeline](https://github.com/Brahian2215/ReserveFlight/actions/workflows/build.yml/badge.svg)](https://github.com/Brahian2215/ReserveFlight/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Brahian2215_ReserveFlight&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Brahian2215_ReserveFlight)

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