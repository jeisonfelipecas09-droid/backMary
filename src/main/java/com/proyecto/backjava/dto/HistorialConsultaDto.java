package com.proyecto.backjava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistorialConsultaDto {
    private String id;
    private String correspondenciaId;
    private String usuarioId;
    private String tipo;
    private String detalle;
    private LocalDateTime fecha;
}
