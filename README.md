# CRM Application

Sistema de gestión de relaciones con clientes (CRM) que permite administrar clientes, proyectos y tareas asociadas. El proyecto está siendo migrado de una aplicación JavaFX de escritorio a una arquitectura moderna basada en Spring Boot con REST API.

## Descripción

Aplicación empresarial para la gestión integral de:
- Clientes y contactos
- Proyectos asociados a clientes
- Tareas vinculadas a proyectos
- Usuarios del sistema

## Arquitectura

El proyecto implementa una arquitectura en capas:

- **Controller**: Manejo de peticiones HTTP y respuestas
- **Service**: Lógica de negocio y reglas de validación
- **Repository**: Acceso a datos mediante JPA
- **Model**: Entidades de dominio
- **DTO**: Objetos de transferencia de datos
- **Mapper**: Conversión entre Models y DTOs
- **Validation**: Validadores personalizados

## Migración a Spring Boot

El proyecto ha sido migrado desde una implementación con JDBC manual a Spring Boot 3.2.2, incorporando:
- Spring Data JPA para la capa de persistencia
- Hibernate como proveedor JPA
- Base de datos H2 (desarrollo)
- API REST (en desarrollo)
- Arquitectura de microservicios

La migración se está realizando de forma incremental, manteniendo la funcionalidad existente mientras se modernizan las capas del sistema.

## Requisitos

- Java 21 o superior
- Maven 3.9+
- Puerto 8080 disponible

## Instalación y Ejecución

### Clonar el repositorio
```bash
git clone https://github.com/1337B/mini-crm.git
cd crm
```

### Compilar el proyecto
```bash
mvn clean compile
```

### Ejecutar la aplicación

**Opción 1: Mediante Maven**
```bash
mvn spring-boot:run
```

**Opción 2: JAR ejecutable**
```bash
mvn package
java -jar target/crm-1.0-SNAPSHOT.jar
```

### Perfiles de ejecución

**Desarrollo**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Producción**
```bash
java -jar target/crm-1.0-SNAPSHOT.jar --spring.profiles.active=prod
```

## Acceso a la Aplicación

Una vez iniciada la aplicación, estará disponible en:

- **API REST**: `http://localhost:8080/api`
- **Consola H2**: `http://localhost:8080/api/h2-console`

### Configuración de la Base de Datos H2

- **JDBC URL**: `jdbc:h2:file:./database/crm`
- **Usuario**: `sa`
- **Contraseña**: (vacía)

## Tecnologías

### Backend
- **Java 21**
- **Spring Boot 3.2.2**
  - Spring Web (REST API)
  - Spring Data JPA
  - Spring Validation
- **Hibernate 6.4.1** (ORM)
- **H2 Database** (base de datos embebida)
- **Maven** (gestión de dependencias)

### Testing
- JUnit 5
- Mockito
- Spring Boot Test

## Estructura del Proyecto

```
crm/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/cbielaszczuk/crm/
│   │   │       ├── CrmApplication.java
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       ├── mapper/
│   │   │       ├── validation/
│   │   │       ├── config/
│   │   │       └── util/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/
├── pom.xml
└── database/
```

## Configuración

La aplicación utiliza perfiles de Spring para diferentes entornos:

### application.properties (Por defecto)
- Puerto: 8080
- Context path: `/api`
- Base de datos: H2 en archivo (`./database/crm`)
- Consola H2: Habilitada
- DDL auto: `update`

### application-dev.properties
Configuración para desarrollo con logs detallados y SQL visible en consola.

### application-prod.properties
Configuración para producción con logs optimizados y consola H2 deshabilitada.

## Comandos Útiles

```bash
# Limpiar y compilar
mvn clean compile

# Ejecutar tests
mvn test

# Generar JAR
mvn package

# Saltar tests al empaquetar
mvn package -DskipTests

# Ver dependencias
mvn dependency:tree

# Actualizar dependencias
mvn dependency:resolve -U
```

## Desarrollo

### Agregar nuevas dependencias
Editar `pom.xml` y ejecutar:
```bash
mvn dependency:resolve
```

### Acceder a la base de datos
Durante el desarrollo, acceder a la consola H2 en `http://localhost:8080/api/h2-console` para inspeccionar datos.

### Hot Reload
Spring Boot DevTools está configurado para reinicio automático durante el desarrollo.

## Contribución

Este proyecto es parte de un ejercicio académico de Programación Avanzada.

## Autor

Cristhian Bielaszczuk

