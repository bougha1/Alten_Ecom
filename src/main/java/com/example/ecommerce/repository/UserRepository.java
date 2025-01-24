package com.example.ecommerce.repository;

import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trouver un utilisateur par son email
    Optional<User> findByEmail(String email);

    // Trouver un utilisateur par son nom d'utilisateur
    Optional<User> findByUsername(String username);
}
