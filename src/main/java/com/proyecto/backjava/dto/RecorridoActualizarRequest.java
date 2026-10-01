package com.proyecto.backjava.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecorridoActualizarRequest {
    private String sedeId;
    private String areaId;
    @Size(max = 120)
    private String modalidad;
}
