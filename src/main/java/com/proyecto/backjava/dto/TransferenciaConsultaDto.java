package com.proyecto.backjava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransferenciaConsultaDto {
    private String id;
    private String correspondenciaId;
    private String deUsuarioId;
    private String aUsuarioId;
    private LocalDateTime fecha;
    private String comentario;
}
