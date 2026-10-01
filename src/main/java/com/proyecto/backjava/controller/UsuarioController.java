package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.UsuarioCreateRequest;
import com.proyecto.backjava.dto.UsuarioConsultaDto;
import com.proyecto.backjava.dto.UsuarioUpdateRequest;
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
    public ResponseEntity<List<UsuarioConsultaDto>> listar() {
        return ResponseEntity.ok(userService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioConsultaDto> consultar(@PathVariable String id) {
        return ResponseEntity.ok(userService.consultar(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioConsultaDto> crear(@Valid @RequestBody UsuarioCreateRequest request) {
        return ResponseEntity.ok(userService.crear(request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioConsultaDto> actualizar(@PathVariable String id,
                                                          @Valid @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(userService.actualizar(id, request));
    }
}
