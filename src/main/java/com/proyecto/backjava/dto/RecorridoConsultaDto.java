package com.proyecto.backjava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RecorridoConsultaDto {
    private String id;
    private String usuarioId;
    private String sedeId;
    private String areaId;
    private LocalDateTime fecha;
    private String modalidad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
