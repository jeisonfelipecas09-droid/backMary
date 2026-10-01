package com.proyecto.backjava.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RecorridoCrearRequest {
    @NotBlank
    private String sedeId;
    @NotBlank
    private String areaId;
    @NotBlank
    @Size(max = 120)
    private String modalidad;
}
