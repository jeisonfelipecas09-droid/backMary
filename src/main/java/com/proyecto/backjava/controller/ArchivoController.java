package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.ArchivoActualizarRequest;
import com.proyecto.backjava.dto.ArchivoConsultaDto;
import com.proyecto.backjava.dto.ArchivoCrearRequest;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.service.ArchivoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {
    private final ArchivoService archivoService;

    @GetMapping
    public ResponseEntity<List<ArchivoConsultaDto>> listar(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(archivoService.listar(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArchivoConsultaDto> consultar(@PathVariable String id,
                                                        @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(archivoService.consultar(id, usuario));
    }

    @PostMapping
    public ResponseEntity<ArchivoConsultaDto> crear(@Valid @RequestBody ArchivoCrearRequest request,
                                                    @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(archivoService.crear(request, usuario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ArchivoConsultaDto> actualizar(@PathVariable String id,
                                                         @Valid @RequestBody ArchivoActualizarRequest request,
                                                         @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(archivoService.actualizar(id, request, usuario));
    }
}
