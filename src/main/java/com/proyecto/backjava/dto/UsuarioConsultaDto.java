package com.proyecto.backjava.dto;

import com.proyecto.backjava.entity.EstadoUsuario;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioConsultaDto {
    private String id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String rol;
    private EstadoUsuario estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
