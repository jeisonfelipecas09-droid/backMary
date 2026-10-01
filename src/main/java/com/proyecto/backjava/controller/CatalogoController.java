package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.CatalogoResponse;
import com.proyecto.backjava.dto.AreaActualizarRequest;
import com.proyecto.backjava.dto.AreaConsultaDto;
import com.proyecto.backjava.dto.AreaCrearRequest;
import com.proyecto.backjava.dto.SedeActualizarRequest;
import com.proyecto.backjava.dto.SedeConsultaDto;
import com.proyecto.backjava.dto.SedeCrearRequest;
import com.proyecto.backjava.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoController {
    private final CatalogService catalogService;

    @GetMapping("/catalogos")
    public ResponseEntity<CatalogoResponse> catalogos() {
        return ResponseEntity.ok(catalogService.listarCatalogos());
    }

    @GetMapping("/sedes")
    public ResponseEntity<List<SedeConsultaDto>> listarSedes() {
        return ResponseEntity.ok(catalogService.listarSedes());
    }

    @GetMapping("/sedes/{id}")
    public ResponseEntity<SedeConsultaDto> consultarSede(@PathVariable String id) {
        return ResponseEntity.ok(catalogService.consultarSede(id));
    }

    @PostMapping("/sedes")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<SedeConsultaDto> crearSede(@Valid @RequestBody SedeCrearRequest request) {
        return ResponseEntity.ok(catalogService.crearSede(request));
    }

    @PatchMapping("/sedes/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<SedeConsultaDto> actualizarSede(@PathVariable String id,
                                                           @Valid @RequestBody SedeActualizarRequest request) {
        return ResponseEntity.ok(catalogService.actualizarSede(id, request));
    }

    @GetMapping("/areas")
    public ResponseEntity<List<AreaConsultaDto>> listarAreas() {
        return ResponseEntity.ok(catalogService.listarAreas());
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<AreaConsultaDto> consultarArea(@PathVariable String id) {
        return ResponseEntity.ok(catalogService.consultarArea(id));
    }

    @PostMapping("/areas")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<AreaConsultaDto> crearArea(@Valid @RequestBody AreaCrearRequest request) {
        return ResponseEntity.ok(catalogService.crearArea(request));
    }

    @PatchMapping("/areas/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<AreaConsultaDto> actualizarArea(@PathVariable String id,
                                                           @Valid @RequestBody AreaActualizarRequest request) {
        return ResponseEntity.ok(catalogService.actualizarArea(id, request));
    }
}
