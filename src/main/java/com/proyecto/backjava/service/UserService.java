package com.proyecto.backjava.service;

import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.dto.UsuarioConsultaDto;
import com.proyecto.backjava.dto.UsuarioCreateRequest;
import com.proyecto.backjava.dto.UsuarioUpdateRequest;
import com.proyecto.backjava.entity.EstadoUsuario;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;                                                                                      

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioConsultaDto> listar() {
        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public UsuarioConsultaDto consultar(String id) {
        return mapToResponse(buscarUsuario(id));
    }

    public UsuarioConsultaDto crear(UsuarioCreateRequest request) {
        if (userRepository.findByCorreoElectronico(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya existe");
        }

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .correoElectronico(request.getEmail().trim().toLowerCase())
                .telefono(request.getTelefono())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(validarRol(request.getRol()))
                .estado(request.getEstado() == null ? EstadoUsuario.ACTIVO : request.getEstado())
                .build();

        return mapToResponse(userRepository.save(user));
    }

    @Transactional
    public UsuarioConsultaDto actualizar(String id, UsuarioUpdateRequest request) {
        User user = buscarUsuario(id);

        if (request.getNombre() != null) {
            user.setNombre(request.getNombre().trim());
        }
        if (request.getApellidos() != null) {
            user.setApellidos(request.getApellidos().trim());
        }
        if (request.getEmail() != null) {
            String correo = request.getEmail().trim().toLowerCase();
            userRepository.findByCorreoElectronico(correo)
                    .filter(otroUsuario -> !otroUsuario.getId().equals(user.getId()))
                    .ifPresent(otroUsuario -> {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya existe");
                    });
            user.setCorreoElectronico(correo);
        }
        if (request.getTelefono() != null) {
            user.setTelefono(request.getTelefono().trim());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRol() != null) {
            user.setRol(validarRol(request.getRol()));
        }
        if (request.getEstado() != null) {
            user.setEstado(request.getEstado());
        }

        return mapToResponse(userRepository.save(user));
    }

    private User buscarUsuario(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private String validarRol(String rol) {
        if (rol == null) {
            return Roles.USUARIO;
        }
        String normalized = rol.trim().toUpperCase();
        if (normalized.equals(Roles.ADMINISTRADOR) || normalized.equals(Roles.USUARIO)) {
            return normalized;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol debe ser ADMINISTRADOR o USUARIO");
    }

    private UsuarioConsultaDto mapToResponse(User user) {
        return UsuarioConsultaDto.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .email(user.getCorreoElectronico())
                .telefono(user.getTelefono())
                .rol(user.getRol())
                .estado(user.getEstado())
                .fechaCreacion(user.getFechaCreacion())
                .fechaActualizacion(user.getFechaActualizacion())
                .build();
    }
}
