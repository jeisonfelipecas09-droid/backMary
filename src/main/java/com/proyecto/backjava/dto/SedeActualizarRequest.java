package com.proyecto.backjava.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SedeActualizarRequest {
    @Size(max = 120)
    private String nombre;
    private Boolean activo;
}
