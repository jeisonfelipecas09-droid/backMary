package com.proyecto.backjava.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransferenciaRequest {
    @NotBlank
    private String usuarioDestinoId;

    private String comentario;
}
