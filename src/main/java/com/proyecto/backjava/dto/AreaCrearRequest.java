package com.proyecto.backjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AreaCrearRequest {
    @NotBlank
    @Size(max = 120)
    private String nombre;
    @NotBlank
    private String sedeId;
    private Boolean activo = true;
}
