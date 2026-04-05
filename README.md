# AS241S5_AEJ_09-be

## Descripción
Backend reactivo desarrollado con Spring WebFlux que consume dos APIs de 
Inteligencia Artificial a través de RapidAPI (Microsoft Copilot y Gemini Pro), 
captura sus respuestas y las almacena en una base de datos PostgreSQL en la nube (Neon).

---

## APIs de IA utilizadas (vía RapidAPI)

### 1. Microsoft Copilot 5
- **Proveedor:** RapidAPI - Copilot5
- **Host:** `copilot5.p.rapidapi.com`
- **Endpoint:** `POST /copilot`
- **Descripción:** Servicio de IA de Microsoft con acceso a internet en tiempo real.
  Soporta conversaciones continuas mediante `conversation_id` y respuestas
  en formato markdown o texto plano. Modos disponibles: `CHAT`, `REASONING`, `SMART`.

### 2. Gemini Pro AI
- **Proveedor:** RapidAPI - Gemini Pro AI
- **Host:** `gemini-pro-ai.p.rapidapi.com`
- **Endpoint:** `POST /`
- **Descripción:** Modelo de IA generativa de Google. Soporta conversaciones
  multi-turno con roles (user/model), alta capacidad de razonamiento
  y generación de texto estructurado en JSON.

---

## Herramientas y versiones

| Herramienta | Versión |
|---|---|
| Java | JDK 17 |
| Visual Studio Code | IntelliJ IDEA |
| Maven | Apache Maven 3.9.x |
| Spring Boot | 3.2.5 |
| Base de datos | PostgreSQL - Neon Cloud |
| Proveedor APIs | RapidAPI |

---

## Frameworks y tecnologías

- **Spring WebFlux** — Programación reactiva no bloqueante
- **Data R2DBC** — Acceso reactivo a base de datos SQL
- **Project Reactor** — Tipos reactivos Mono y Flux
- **R2DBC PostgreSQL** — Driver reactivo para PostgreSQL

---

## Dependencias Spring WebFlux + PostgreSQL (SQL)

Spring WebFlux | Data R2DBC | Project Reactor | R2DBC PostgreSQL
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>r2dbc-postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

---


## Dependencias Swagger para Spring WebFlux
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webflux-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

---

## Base de datos Cloud

**Neon PostgreSQL** — PostgreSQL serverless en la nube.

Tabla `query_record`:

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | BIGSERIAL | Clave primaria autoincremental |
| `api_source` | VARCHAR | Origen: `copilot` o `gemini` |
| `prompt` | TEXT | Consulta enviada por el usuario |
| `response` | TEXT | Respuesta recibida de la API |
| `created_at` | TIMESTAMP | Fecha y hora de la consulta |

---

## Configuración

Todas las credenciales están centralizadas en `src/main/resources/application.yml`:
```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://HOST/neondb?sslmode=require
    username: USUARIO
    password: PASSWORD

rapidapi:
  key: TU_API_KEY

copilot:
  api:
    url: https://copilot5.p.rapidapi.com/copilot
    host: copilot5.p.rapidapi.com

gemini:
  api:
    url: https://gemini-pro-ai.p.rapidapi.com/
    host: gemini-pro-ai.p.rapidapi.com
```

---

## Endpoints disponibles

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/copilot/ask` | Envía consulta a Copilot y guarda resultado |
| GET | `/api/copilot/history` | Lista todas las consultas a Copilot |
| POST | `/api/gemini/ask` | Envía consulta a Gemini y guarda resultado |
| GET | `/api/gemini/history` | Lista todas las consultas a Gemini |

---

## Estructura del proyecto
```
src/
└── main/
    ├── java/com/roberto/as241s5_aej_09_be/
    │   ├── config/
    │   │   └── WebClientConfig.java
    │   ├── controller/
    │   │   ├── CopilotController.java
    │   │   └── GeminiController.java
    │   ├── service/
    │   │   ├── CopilotService.java
    │   │   └── GeminiService.java
    │   ├── repository/
    │   │   └── QueryRepository.java
    │   └── model/
    │       └── QueryRecord.java
    └── resources/
        ├── application.yml
        └── schema.sql
```
