package com.proyecto.backjava.service;

import com.proyecto.backjava.dto.AuthResponse;
import com.proyecto.backjava.dto.LoginRequest;
import com.proyecto.backjava.dto.UsuarioResponse;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos"));

        if (!Boolean.TRUE.equals(user.getActivo()) || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, mapToResponse(user));
    }

    public UsuarioResponse me(User user) {
        return mapToResponse(user);
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
