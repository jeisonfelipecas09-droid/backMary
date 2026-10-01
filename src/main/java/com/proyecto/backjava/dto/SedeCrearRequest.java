package com.proyecto.backjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SedeCrearRequest {
    @NotBlank
    @Size(max = 120)
    private String nombre;
    private Boolean activo = true;
}
