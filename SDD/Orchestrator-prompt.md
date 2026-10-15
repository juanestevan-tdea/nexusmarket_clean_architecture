# PROMPT DE ORQUESTACIÓN AGÉNTICA: DESARROLLO DE CÓDIGO PARALELIZADO - NEXUSMARKET

Este documento define el **Prompt del Agente Orquestador** encargado de implementar de forma automatizada el backend de **NexusMarket** (Plataforma E-Commerce), siguiendo estrictamente la especificación del **Software Design Document (SDD)** y la **Arquitectura Hexagonal**.

**Stack Tecnológico Estricto:** Java 17+, Spring Boot, Spring Security, Spring Data JPA (MySQL) y Spring Data MongoDB. 

---

## 1. IDENTIFICACIÓN Y ENTORNO LOCAL
- **Rol:** Lead Software Architect / Agente Orquestador.
- **Relational DB (SQL):** MySQL en el puerto **`3306`** (Base de datos: `nexusmarket_db`, para Usuarios, Órdenes y Pagos).
- **NoSQL DB:** MongoDB local en el puerto **`27017`** (Base de datos: `nexus_catalog_db`, para Catálogo de Productos y Auditoría).

---

## 2. INSTRUCCIONES EJECUTIVAS POR FASES

### FASE 0: Auto-generación de Infraestructura Local
1. Crear `docker-compose.yml` en la raíz para levantar MySQL (`3306`) y MongoDB (`27017`).
2. Crear `application.properties` con `spring.jpa.hibernate.ddl-auto=update` y las URIs de conexión a ambas bases de datos.

### FASE 1: Agente Dominio Core (Secuencial)
**Sub-Agente:** `domain-core-agent`
**Objetivo:** Construir la capa de Dominio pura (CERO dependencias de Spring o DBs).
- **Modelos:** `User` (Buyer, Seller, Admin), `Product`, `Order`, `CartItem`, `Payment`.
- **Value Objects/Enums:** `OrderStatus` (PENDING, PAID, SHIPPED, DELIVERED), `ProductCategory`, `Currency`.
- **Output Ports:** `UserRepositoryPort`, `ProductRepositoryPort`, `OrderRepositoryPort`, `CartRepositoryPort`.

### FASE 2: Desarrollo Paralelo de Adaptadores e Interfaces
Lanzar en paralelo 3 sub-agentes:
- **[2A] Persistencia Relacional:** Implementar `adapters/persistence/jpa/` (Entidades `@Entity`, Mappers, `JpaRepository`) para Usuarios y Órdenes. Implementar `UserRepositoryPort` y `OrderRepositoryPort`.
- **[2B] Persistencia NoSQL (Catálogo):** Implementar `adapters/persistence/mongodb/` (Documentos `@Document`, `MongoRepository`) para Productos. Implementar `ProductRepositoryPort`.
- **[2C] Input Ports:** Crear interfaces en `domain/ports/in/` (`PublicCatalogPort`, `BuyerCheckoutPort`, `SellerManagementPort`, `AdminPort`).

### FASE 3: Desarrollo Paralelo de Lógica de Negocio y API REST
Lanzar en paralelo 2 sub-agentes:
- **[3A] Servicios y Casos de Uso:** Implementar `domain/services/` (ej. cálculo de totales, reducción de inventario, reglas de checkout) e implementar los Casos de Uso en `adapters/useCases/` inyectando los servicios de dominio.
- **[3B] API REST:** Crear en `adapters/rest/` los Controladores (`ProductController`, `OrderController`, `CartController`), DTOs de Request/Response y Mappers. Validaciones con Jakarta Bean Validation.

### FASE 4: Seguridad JWT e Integración
- Implementar `infrastructure/security/JwtProvider` y `JwtAuthenticationFilter`.
- Configurar Spring Security para proteger endpoints según roles.

### FASE 5: Pruebas Unitarias Automatizadas
- Generar suite de pruebas (JUnit 5 + Mockito) exigiendo un 100% de cobertura en la capa de Dominio y Casos de Uso.