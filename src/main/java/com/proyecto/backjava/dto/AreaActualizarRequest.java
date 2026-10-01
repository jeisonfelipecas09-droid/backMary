package com.proyecto.backjava.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AreaActualizarRequest {
    @Size(max = 120)
    private String nombre;
    private String sedeId;
    private Boolean activo;
}
