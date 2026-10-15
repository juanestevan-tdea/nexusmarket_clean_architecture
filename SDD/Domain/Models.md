# Modelos de Dominio - NexusMarket

Este documento define las entidades principales, sus atributos y las reglas de negocio (invariantes) que el Agente Orquestador debe implementar en Java puro dentro de `domain/models/`. No se deben usar anotaciones de frameworks (ni JPA, ni Spring) en estos modelos.

## 1. User (Usuario)
- **Atributos:**
  - `id` (Long o UUID)
  - `email` (String, único, formato de correo válido)
  - `password` (String, debe almacenarse encriptada)
  - `role` (Enum: `BUYER`, `SELLER`, `ADMIN`)
  - `isActive` (Boolean)
- **Reglas de Negocio (Invariantes):**
  - Un usuario recién creado siempre tiene `isActive = true`.

## 2. Product (Producto)
- **Atributos:**
  - `id` (Long o UUID)
  - `sellerId` (Referencia al ID del User que lo vende)
  - `name` (String, no puede estar vacío ni ser nulo)
  - `description` (String)
  - `price` (BigDecimal, para precisión financiera)
  - `stock` (Integer)
  - `status` (Enum: `ACTIVE`, `INACTIVE`)
- **Reglas de Negocio (Invariantes):**
  - El `price` debe ser estrictamente mayor a 0.
  - El `stock` no puede ser negativo (`>= 0`).
  - Solo los usuarios con rol `SELLER` o `ADMIN` pueden crear productos.

## 3. Order (Orden de Compra)
- **Atributos:**
  - `id` (Long o UUID)
  - `buyerId` (Referencia al ID del User que compra)
  - `items` (List<CartItem>)
  - `totalAmount` (BigDecimal)
  - `status` (Enum: `PENDING`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELLED`)
  - `createdAt` (LocalDateTime)
- **Reglas de Negocio (Invariantes):**
  - El `totalAmount` debe ser exactamente la suma del subtotal de todos los `items`.
  - Una orden no puede instanciarse si la lista de `items` está vacía.
  - El estado inicial de una orden recién creada siempre es `PENDING`.

## 4. CartItem (Ítem del Carrito/Orden)
- **Atributos:**
  - `productId` (Referencia al ID del Producto)
  - `quantity` (Integer)
  - `unitPrice` (BigDecimal, congelado al precio del momento de la compra)
- **Reglas de Negocio (Invariantes):**
  - La `quantity` debe ser estrictamente mayor a 0.
  - El subtotal del ítem se calcula dinámicamente como `quantity * unitPrice`.