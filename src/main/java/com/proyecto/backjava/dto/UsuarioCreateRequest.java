package com.proyecto.backjava.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateRequest {
    @NotBlank
    @Size(max = 80)
    private String nombre;

    @NotBlank
    @Size(max = 120)
    private String apellidos;

    @NotBlank
    @Email
    @Size(max = 160)
    private String email;

    @Size(max = 30)
    private String telefono;

    @NotBlank
    private String password;

    @NotBlank
    private String rol;

    private Boolean activo = true;
}
