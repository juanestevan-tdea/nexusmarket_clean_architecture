package com.tdea.nexusmarket.application.infrastructure.security;

import com.tdea.nexusmarket.application.adapters.persistence.jpa.repositories.SpringDataJpaUserRepository;
import com.tdea.nexusmarket.application.adapters.persistence.mongodb.repositories.SpringDataMongoProductRepository;
import com.tdea.nexusmarket.application.domain.ports.in.BuyerCheckoutPort;
import com.tdea.nexusmarket.application.domain.ports.in.PublicCatalogPort;
import com.tdea.nexusmarket.application.domain.ports.in.SellerManagementPort;
import com.tdea.nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import com.tdea.nexusmarket.application.domain.models.CartItem;
import com.tdea.nexusmarket.application.domain.models.Order;
import com.tdea.nexusmarket.application.domain.models.Product;
import com.tdea.nexusmarket.application.domain.services.OrderCheckoutService;
import com.tdea.nexusmarket.application.domain.services.ProductCatalogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ------------------------------------------------------------------
    // Beans de seguridad
    // ------------------------------------------------------------------

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/catalog/**").permitAll()
                .requestMatchers("/api/checkout/**").authenticated()
                .requestMatchers("/api/seller/**").hasRole("SELLER")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build();
        UserDetails seller = User.withUsername("seller")
            .password(passwordEncoder().encode("seller123"))
            .roles("SELLER")
            .build();
        UserDetails buyer = User.withUsername("buyer")
            .password(passwordEncoder().encode("buyer123"))
            .roles("BUYER")
            .build();
        return new InMemoryUserDetailsManager(admin, seller, buyer);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    // ------------------------------------------------------------------
    // Beans de puertos de entrada (implementaciones de dominio)
    // ------------------------------------------------------------------

    @Bean
    public PublicCatalogPort publicCatalogPort(SpringDataMongoProductRepository productRepository) {
        return new ProductCatalogService(productRepository);
    }

    @Bean
    public BuyerCheckoutPort buyerCheckoutPort(SpringDataMongoProductRepository productRepository) {
        OrderRepositoryPort inMemoryOrderRepository = new InMemoryOrderRepositoryPort();
        return new OrderCheckoutService(inMemoryOrderRepository, productRepository);
    }

    @Bean
    public SellerManagementPort sellerManagementPort(SpringDataMongoProductRepository productRepository) {
        return new SellerManagementPortImpl(productRepository);
    }

    // ------------------------------------------------------------------
    // Implementaciones auxiliares en memoria (para completar el wiring)
    // ------------------------------------------------------------------

    /**
     * Implementación en memoria de OrderRepositoryPort para que el contexto
     * de Spring pueda inicializarse sin una implementación JPA concreta.
     */
    public static class InMemoryOrderRepositoryPort implements OrderRepositoryPort {

        private final Map<Long, Order> store = new HashMap<>();
        private Long sequence = 1L;

        @Override
        public Optional<Order> findById(Long id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<Order> findByBuyerId(Long buyerId) {
            List<Order> result = new ArrayList<>();
            for (Order order : store.values()) {
                if (buyerId.equals(order.getBuyerId())) {
                    result.add(order);
                }
            }
            return result;
        }

        @Override
        public Order save(Order order) {
            if (order.getId() == null) {
                order.setId(sequence++);
            }
            store.put(order.getId(), order);
            return order;
        }
    }

    /**
     * Implementación de SellerManagementPort sobre ProductRepositoryPort.
     */
    public static class SellerManagementPortImpl implements SellerManagementPort {

        private final SpringDataMongoProductRepository productRepository;

        public SellerManagementPortImpl(SpringDataMongoProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @Override
        public Product createProduct(Long sellerId, Product product) {
            product.setSellerId(sellerId);
            product.setStatus(com.tdea.nexusmarket.application.domain.enums.ProductStatus.ACTIVE);
            return productRepository.save(product);
        }

        @Override
        public void updateProductStock(Long productId, Integer newStock, Long sellerId) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productId));
            product.setStock(newStock);
            productRepository.save(product);
        }

        @Override
        public void deactivateProduct(Long productId, Long sellerId) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productId));
            product.setStatus(com.tdea.nexusmarket.application.domain.enums.ProductStatus.INACTIVE);
            productRepository.save(product);
        }
    }
}
