package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCorreoElectronico(String correoElectronico);
}
