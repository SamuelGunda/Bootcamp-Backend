package com.kasv.gunda.bootcamp.repositories;

import com.kasv.gunda.bootcamp.models.ERole;
import com.kasv.gunda.bootcamp.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
