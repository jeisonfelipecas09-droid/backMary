package com.proyecto.backjava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ArchivoConsultaDto {
    private String id;
    private String correspondenciaId;
    private String nombre;
    private String url;
    private String tipo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
