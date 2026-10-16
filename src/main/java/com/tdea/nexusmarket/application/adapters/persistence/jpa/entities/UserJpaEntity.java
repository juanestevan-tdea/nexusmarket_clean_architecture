package com.tdea.nexusmarket.application.adapters.persistence.jpa.entities;

import com.tdea.nexusmarket.application.domain.models.User;
import com.tdea.nexusmarket.application.domain.enums.Role;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String role;

    @Column(nullable = false)
    private boolean isActive;

    public UserJpaEntity() {
    }

    public UserJpaEntity(Long id, String email, String password, String role, boolean isActive) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
    }

    public User toDomain() {
        User user = new User(id, email, password, Role.valueOf(role));
        user.setActive(isActive);
        return user;
    }

    public static UserJpaEntity fromDomain(User user) {
        return new UserJpaEntity(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.getRole().name(),
            user.isActive()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserJpaEntity)) return false;
        UserJpaEntity that = (UserJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
