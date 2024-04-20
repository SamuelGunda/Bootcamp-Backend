package com.kasv.gunda.bootcamp.repositories;

import com.kasv.gunda.bootcamp.entities.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnSavedUsers() {

        // Arrange
        User user = User.builder()
                .username("Braňo")
                .email("mail@test.com")
                .password("password")
                .build();

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_ExistsByUsername_ReturnsTrueIfUserExists() {
        // Arrange
        User user = User.builder()
                .username("Braňo")
                .email("mail@test.com")
                .password("password")
                .build();
        userRepository.save(user);

        // Act
        Boolean exists = userRepository.existsByUsername(user.getUsername());

        // Assert
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserRepository_FindByUsername_ReturnsUserIfExists() {
        // Arrange
        User user = User.builder()
                .username("Braňo")
                .email("mail@test.com")
                .password("password")
                .build();
        userRepository.save(user);

        // Act
        User foundUser = userRepository.findByUsername(user.getUsername());

        // Assert
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void UserRepository_FindIdByUsername_ReturnsUserIdIfExists() {
        // Arrange
        User user = User.builder()
                .username("Braňo")
                .email("mail@test.com")
                .password("password")
                .build();
        User savedUser = userRepository.save(user);

        // Act
        Integer foundId = userRepository.findIdByUsername(user.getUsername());

        // Assert
        Assertions.assertThat(foundId).isNotNull();
        Assertions.assertThat(foundId).isEqualTo(savedUser.getId());
    }

}
