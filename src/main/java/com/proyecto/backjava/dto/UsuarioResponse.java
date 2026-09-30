package com.proyecto.backjava.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {
    private String id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String rol;
    private Boolean activo;
}
