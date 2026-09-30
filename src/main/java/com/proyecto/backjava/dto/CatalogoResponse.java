package com.proyecto.backjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CatalogoResponse {
    private List<SedeDto> sedes;
    private List<AreaDto> areas;

    @Data
    @AllArgsConstructor
    public static class SedeDto {
        private String id;
        private String nombre;
        private Boolean activo;
    }

    @Data
    @AllArgsConstructor
    public static class AreaDto {
        private String id;
        private String nombre;
        private String sedeId;
        private Boolean activo;
    }
}
