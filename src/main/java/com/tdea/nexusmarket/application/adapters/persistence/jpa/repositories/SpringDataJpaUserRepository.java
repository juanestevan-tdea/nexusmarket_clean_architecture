package com.tdea.nexusmarket.application.adapters.persistence.jpa.repositories;

import com.tdea.nexusmarket.application.adapters.persistence.jpa.entities.UserJpaEntity;
import com.tdea.nexusmarket.application.domain.models.User;
import com.tdea.nexusmarket.application.domain.ports.out.UserRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class SpringDataJpaUserRepository implements UserRepositoryPort {

    private final JpaRepository<UserJpaEntity, Long> jpaRepository;

    public SpringDataJpaUserRepository(JpaRepository<UserJpaEntity, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(UserJpaEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findAll().stream()
            .filter(entity -> email.equals(entity.getEmail()))
            .findFirst()
            .map(UserJpaEntity::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaEntity.fromDomain(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }
}
