# Adaptadores de Persistencia (Output Ports) - NexusMarket

Este documento define cómo el sistema almacena y recupera la información. El Agente Orquestador debe crear las interfaces puras en `domain/ports/out/` y sus implementaciones con frameworks de base de datos en `adapters/persistence/`.

## 1. UserRepositoryPort (Relacional - MySQL)
- **Ubicación de la Interfaz:** `domain/ports/out/UserRepositoryPort.java`
- **Implementación:** Spring Data JPA en `adapters/persistence/jpa/`
- **Métodos requeridos:**
  - `Optional<User> findById(Long id)`
  - `Optional<User> findByEmail(String email)`
  - `User save(User user)`

## 2. ProductRepositoryPort (NoSQL - MongoDB)
- **Ubicación de la Interfaz:** `domain/ports/out/ProductRepositoryPort.java`
- **Implementación:** Spring Data MongoDB en `adapters/persistence/mongodb/`
- **Métodos requeridos:**
  - `Optional<Product> findById(Long id)`
  - `List<Product> findAllActive()`
  - `Product save(Product product)`
- **Regla de Arquitectura:** Se usa MongoDB para aislar el catálogo de productos, permitiendo que en el futuro los productos tengan especificaciones técnicas dinámicas (tallas, colores, etc.) sin alterar esquemas relacionales rígidos.

## 3. OrderRepositoryPort (Relacional - MySQL)
- **Ubicación de la Interfaz:** `domain/ports/out/OrderRepositoryPort.java`
- **Implementación:** Spring Data JPA en `adapters/persistence/jpa/`
- **Métodos requeridos:**
  - `Optional<Order> findById(Long id)`
  - `List<Order> findByBuyerId(Long buyerId)`
  - `Order save(Order order)`
- **Regla de Persistencia:** Al guardar una Orden en MySQL, el adaptador JPA debe encargarse de mapear y guardar en cascada todos sus `CartItem` asociados.