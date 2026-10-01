package com.proyecto.backjava.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ArchivoActualizarRequest {
    @Size(max = 255)
    private String nombre;
    @Size(max = 500)
    private String url;
    @Size(max = 100)
    private String tipo;
}
