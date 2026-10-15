# Puertos de Entrada (Input Ports) - NexusMarket

Este documento define los contratos de los Casos de Uso del sistema. El Agente Orquestador debe crear estas interfaces en Java puro dentro de `domain/ports/in/`. Cada interfaz representa lo que el exterior (ej. un Controlador REST) puede pedirle al núcleo del negocio.

## 1. PublicCatalogPort (Acceso Público)
Operaciones que cualquier usuario (incluso sin registrarse) puede realizar.
- `List<Product> getActiveProducts()`
- `Product getProductById(Long productId)`

## 2. BuyerCheckoutPort (Rol: BUYER)
Operaciones exclusivas para usuarios que realizan compras.
- `Order createOrder(Long buyerId, List<CartItem> items)`
- `Order payOrder(Long orderId, String paymentMethod)`
- `List<Order> getOrderHistory(Long buyerId)`

## 3. SellerManagementPort (Rol: SELLER)
Operaciones para vendedores que administran su propio inventario.
- `Product createProduct(Long sellerId, Product product)`
- `void updateProductStock(Long productId, Integer newStock, Long sellerId)`
- `void deactivateProduct(Long productId, Long sellerId)`

## 4. AdminPort (Rol: ADMIN)
Operaciones de administración global de la plataforma.
- `void disableUser(Long userId)`
- `void forceCancelOrder(Long orderId, String reason)`