package com.tdea.nexusmarket.application.domain.ports.out;

import com.tdea.nexusmarket.application.domain.models.User;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);
}
