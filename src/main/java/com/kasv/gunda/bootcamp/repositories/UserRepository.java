package com.kasv.gunda.bootcamp.repositories;

import com.kasv.gunda.bootcamp.models.ERole;
import com.kasv.gunda.bootcamp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User getUserById(Long id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
