package com.proyecto.backjava.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CorrespondenciaConsultaDto {
    private String id;
    private String recorridoId;
    private String creadoPorId;
    private String usuarioAsignadoId;
    private String recibidaPorId;
    private String numeroDocumento;
    private Boolean contieneCorrespondencia;
    private String nombreDocumento;
    private String destino;
    private String personaEntrega;
    private String observacion;
    private String estado;
    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaEntrega;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<ArchivoConsultaDto> archivos;
}
