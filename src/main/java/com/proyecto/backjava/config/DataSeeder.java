package com.proyecto.backjava.config;

import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .id(UUID.randomUUID().toString())
                    .nombre("Administrador")
                    .apellidos("General")
                    .email("admin@correspondencia.local")
                    .passwordHash(passwordEncoder.encode("Admin123*"))
                    .rol(Roles.ADMINISTRADOR)
                    .activo(true)
                    .build();
            userRepository.save(admin);

            User correspondencia = User.builder()
                    .id(UUID.randomUUID().toString())
                    .nombre("Usuario")
                    .apellidos("Correspondencia")
                    .email("correspondencia@correspondencia.local")
                    .passwordHash(passwordEncoder.encode("Admin123*"))
                    .rol(Roles.CORRESPONDENCIA)
                    .activo(true)
                    .build();
            userRepository.save(correspondencia);

            User usuario = User.builder()
                    .id(UUID.randomUUID().toString())
                    .nombre("Usuario")
                    .apellidos("Operativo")
                    .email("usuario@correspondencia.local")
                    .passwordHash(passwordEncoder.encode("Admin123*"))
                    .rol(Roles.USUARIO)
                    .activo(true)
                    .build();
            userRepository.save(usuario);
        }
    }
}
