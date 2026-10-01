package com.proyecto.backjava.dto;

import com.proyecto.backjava.entity.EstadoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateRequest {
    @Size(max = 80)
    private String nombre;

    @Size(max = 120)
    private String apellidos;

    @Email
    @Size(max = 160)
    private String email;

    @Size(max = 30)
    private String telefono;

    @Size(max = 72)
    private String password;

    private String rol;
    private EstadoUsuario estado;
}
