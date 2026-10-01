package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.RecorridoActualizarRequest;
import com.proyecto.backjava.dto.RecorridoConsultaDto;
import com.proyecto.backjava.dto.RecorridoCrearRequest;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.service.RecorridoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recorridos")
@RequiredArgsConstructor
public class RecorridoController {
    private final RecorridoService recorridoService;

    @GetMapping
    public ResponseEntity<List<RecorridoConsultaDto>> listar(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(recorridoService.listar(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecorridoConsultaDto> consultar(@PathVariable String id,
                                                          @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(recorridoService.consultar(id, usuario));
    }

    @PostMapping
    public ResponseEntity<RecorridoConsultaDto> crear(@Valid @RequestBody RecorridoCrearRequest request,
                                                      @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(recorridoService.crear(request, usuario));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecorridoConsultaDto> actualizar(@PathVariable String id,
                                                           @Valid @RequestBody RecorridoActualizarRequest request,
                                                           @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(recorridoService.actualizar(id, request, usuario));
    }
}
