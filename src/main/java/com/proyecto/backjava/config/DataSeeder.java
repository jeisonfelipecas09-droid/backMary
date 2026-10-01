package com.proyecto.backjava.config;

import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.entity.EstadoUsuario;
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
                    .correoElectronico("admin@correspondencia.local")
                    .password(passwordEncoder.encode("Admin123*"))
                    .rol(Roles.ADMINISTRADOR)
                    .estado(EstadoUsuario.ACTIVO)
                    .build();
            userRepository.save(admin);

            User usuario = User.builder()
                    .id(UUID.randomUUID().toString())
                    .nombre("Usuario")
                    .apellidos("Operativo")
                    .correoElectronico("usuario@correspondencia.local")
                    .password(passwordEncoder.encode("Admin123*"))
                    .rol(Roles.USUARIO)
                    .estado(EstadoUsuario.ACTIVO)
                    .build();
            userRepository.save(usuario);
        }
    }
}
