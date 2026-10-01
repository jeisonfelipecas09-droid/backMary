package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.CorrespondenciaActualizarRequest;
import com.proyecto.backjava.dto.CorrespondenciaConsultaDto;
import com.proyecto.backjava.dto.CorrespondenciaCreateRequest;
import com.proyecto.backjava.dto.HistorialConsultaDto;
import com.proyecto.backjava.dto.TransferenciaConsultaDto;
import com.proyecto.backjava.dto.TransferenciaRequest;
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
    public ResponseEntity<CorrespondenciaConsultaDto> crear(@AuthenticationPrincipal User usuario,
                                                             @Valid @RequestBody CorrespondenciaCreateRequest request) {
        return ResponseEntity.ok(correspondenciaService.crear(usuario, request));
    }

    @GetMapping("/correspondencias/{id}")
    public ResponseEntity<CorrespondenciaConsultaDto> consultar(@PathVariable String id,
                                                                 @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.consultar(id, usuario));
    }

    @PatchMapping("/correspondencias/{id}")
    public ResponseEntity<CorrespondenciaConsultaDto> actualizar(@PathVariable String id,
                                                                 @AuthenticationPrincipal User usuario,
                                                                 @Valid @RequestBody CorrespondenciaActualizarRequest request) {
        return ResponseEntity.ok(correspondenciaService.actualizar(id, request, usuario));
    }

    @GetMapping("/inventario")
    public ResponseEntity<List<CorrespondenciaConsultaDto>> inventario(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.inventario(usuario));
    }

    @PatchMapping("/inventario/{id}/recibir")
    public ResponseEntity<CorrespondenciaConsultaDto> recibir(@PathVariable String id,
                                                              @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.recibir(id, usuario));
    }

    @PostMapping("/inventario/{id}/transferir")
    public ResponseEntity<CorrespondenciaConsultaDto> transferir(@PathVariable String id,
                                                                 @AuthenticationPrincipal User usuario,
                                                                 @Valid @RequestBody TransferenciaRequest request) {
        return ResponseEntity.ok(correspondenciaService.transferir(id, usuario, request));
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    public ResponseEntity<List<HistorialConsultaDto>> historial(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.historial(usuario));
    }

    @GetMapping("/historial/{id}")
    public ResponseEntity<HistorialConsultaDto> consultarEvento(@PathVariable String id,
                                                                @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.consultarEvento(id, usuario));
    }

    @GetMapping("/transferencias")
    public ResponseEntity<List<TransferenciaConsultaDto>> transferencias(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.transferencias(usuario));
    }

    @GetMapping("/transferencias/{id}")
    public ResponseEntity<TransferenciaConsultaDto> consultarTransferencia(@PathVariable String id,
                                                                          @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(correspondenciaService.consultarTransferencia(id, usuario));
    }
}
