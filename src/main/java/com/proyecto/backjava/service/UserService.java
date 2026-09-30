package com.proyecto.backjava.service;

import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.dto.UsuarioCreateRequest;
import com.proyecto.backjava.dto.UsuarioResponse;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponse> listar() {
        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public UsuarioResponse crear(UsuarioCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya existe");
        }

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .email(request.getEmail().trim().toLowerCase())
                .telefono(request.getTelefono())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .rol(validarRol(request.getRol()))
                .activo(request.getActivo() == null || request.getActivo())
                .build();

        return mapToResponse(userRepository.save(user));
    }

    private String validarRol(String rol) {
        if (rol == null) {
            return Roles.USUARIO;
        }
        String normalized = rol.trim().toUpperCase();
        if (normalized.equals(Roles.ADMINISTRADOR) || normalized.equals(Roles.CORRESPONDENCIA) || normalized.equals(Roles.USUARIO)) {
            return normalized;
        }
        return Roles.USUARIO;
    }

    private UsuarioResponse mapToResponse(User user) {
        return UsuarioResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .email(user.getEmail())
                .telefono(user.getTelefono())
                .rol(user.getRol())
                .activo(user.getActivo())
                .build();
    }
}
