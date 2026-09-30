package com.proyecto.backjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CorrespondenciaCreateRequest {
    @NotBlank
    private String sedeId;

    @NotBlank
    private String areaId;

    @NotBlank
    private String modalidad;

    @NotNull
    private Boolean contieneCorrespondencia;

    @Size(max = 255)
    private String nombreDocumento;

    @Size(max = 255)
    private String destino;

    @Size(max = 160)
    private String personaEntrega;

    private String observacion;

    private String usuarioAsignadoId;

    private List<ArchivoRequest> archivos = List.of();

    @Data
    public static class ArchivoRequest {
        @NotBlank
        private String nombre;

        @NotBlank
        private String url;

        private String tipo;
    }
}
