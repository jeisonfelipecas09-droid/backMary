package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.CorrespondenciaCreateRequest;
import com.proyecto.backjava.dto.TransferenciaRequest;
import com.proyecto.backjava.entity.Correspondencia;
import com.proyecto.backjava.entity.HistorialEvento;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.service.CorrespondenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CorrespondenciaController {
    private final CorrespondenciaService correspondenciaService;

    @PostMapping("/correspondencias")
    public ResponseEntity<Correspondencia> crear(@AuthenticationPrincipal User usuario,
                                                 @Valid @RequestBody CorrespondenciaCreateRequest request) {
        return ResponseEntity.ok(correspondenciaService.crear(usuario, request));
    }

    @GetMapping("/inventario")
    public ResponseEntity<List<Correspondencia>> inventario(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.inventario(usuario));
    }

    @PatchMapping("/inventario/{id}/recibir")
    public ResponseEntity<Correspondencia> recibir(@PathVariable String id,
                                                  @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.recibir(id, usuario));
    }

    @PostMapping("/inventario/{id}/transferir")
    public ResponseEntity<Correspondencia> transferir(@PathVariable String id,
                                                    @AuthenticationPrincipal User usuario,
                                                    @Valid @RequestBody TransferenciaRequest request) {
        return ResponseEntity.ok(correspondenciaService.transferir(id, usuario, request));
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','CORRESPONDENCIA','USUARIO')")
    public ResponseEntity<List<HistorialEvento>> historial(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.historial(usuario));
    }
}
