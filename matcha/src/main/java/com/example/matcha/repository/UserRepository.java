package com.example.matcha.repository;

import com.example.matcha.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String s);

    Optional<User> findUserById(Long id);
}
