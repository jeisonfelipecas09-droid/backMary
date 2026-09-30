package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.UsuarioCreateRequest;
import com.proyecto.backjava.dto.UsuarioResponse;
import com.proyecto.backjava.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(userService.listar());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioCreateRequest request) {
        return ResponseEntity.ok(userService.crear(request));
    }
}
