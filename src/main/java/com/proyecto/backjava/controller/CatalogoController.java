package com.proyecto.backjava.controller;

import com.proyecto.backjava.dto.CatalogoResponse;
import com.proyecto.backjava.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoController {
    private final CatalogService catalogService;

    @GetMapping("/catalogos")
    public ResponseEntity<CatalogoResponse> catalogos() {
        return ResponseEntity.ok(catalogService.listarCatalogos());
    }
}
