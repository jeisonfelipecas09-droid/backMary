package com.proyecto.backjava.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CorrespondenciaActualizarRequest {
    @Size(max = 255)
    private String nombreDocumento;
    @Size(max = 255)
    private String destino;
    @Size(max = 160)
    private String personaEntrega;
    private String observacion;
}
