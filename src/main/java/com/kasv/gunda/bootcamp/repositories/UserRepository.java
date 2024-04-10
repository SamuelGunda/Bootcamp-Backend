package com.kasv.gunda.bootcamp.repositories;

import com.kasv.gunda.bootcamp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Integer findIdByUsername(@Param("username") String username);

    @Query("SELECT u.password FROM User u WHERE u.username = :username")
    String findPasswordByUsername(@Param("username") String username);

    @Query("UPDATE User u SET u.password = :newPassword WHERE u.username = :username")
    void changePasswordByUsername(String username, String newPassword);
}
