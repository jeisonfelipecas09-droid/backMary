package com.proyecto.backjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArchivoCrearRequest {
    @NotBlank
    private String correspondenciaId;
    @NotBlank
    @Size(max = 255)
    private String nombre;
    @NotBlank
    @Size(max = 500)
    private String url;
    @Size(max = 100)
    private String tipo;
}
