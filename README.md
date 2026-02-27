# CRM Studio

Aplicación web de gestión de clientes, proyectos y tareas. Desarrollada con Spring Boot 3, Thymeleaf, Spring Security y MySQL.

## Funcionalidad

- **Autenticación** — registro e inicio de sesión con contraseñas hasheadas con BCrypt. Todas las rutas requieren autenticación.
- **Clientes** — CRUD completo: crear, ver, editar y eliminar clientes. El detalle de cada cliente muestra sus proyectos asociados.
- **Proyectos** — CRUD completo con seguimiento de estado (En progreso, En espera, Finalizado, Cancelado). Los proyectos están vinculados a un cliente. El detalle muestra las tareas asociadas.
- **Tareas** — CRUD completo con seguimiento de estado. Las tareas están vinculadas a un proyecto.
- **Dashboard** — resumen de totales de clientes, proyectos y tareas con actividad reciente.
- **API REST** — todas las entidades también están expuestas bajo `/api/*` para consumo externo.

## Cómo levantar el proyecto

### Requisitos

- Java 21
- Maven 3.9+
- MySQL 8+

### Configuración de la base de datos

Crear la base de datos antes de iniciar:

```sql
CREATE DATABASE crm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Configuración

Las credenciales de la base de datos se configuran en `src/main/resources/application-prod.properties`. Por defecto conecta a `localhost:3306/crm` con usuario `root` sin contraseña. Ajustar según el entorno.

### Iniciar

```bash
mvn spring-boot:run
```

La aplicación inicia en `http://localhost:8080`. Se redirige automáticamente al login.

Para iniciar con un perfil específico:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Tests

Los tests corren contra una base de datos H2 en memoria a través del perfil `test`:

```bash
mvn test
```

## Migración

El proyecto fue originalmente una aplicación de escritorio en JavaFX con JDBC plano y H2. Fue migrado a Spring Boot 3 como aplicación web completa. La migración implicó reemplazar JDBC por Spring Data JPA con Hibernate, incorporar Spring Security con BCrypt, reemplazar la interfaz JavaFX por plantillas Thymeleaf renderizadas en el servidor, y agregar una capa de API REST. El modelo de dominio, DTOs, validadores y lógica de negocio fueron preservados y adaptados al modelo de componentes de Spring.
