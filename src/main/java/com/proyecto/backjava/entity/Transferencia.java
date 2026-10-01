package com.proyecto.backjava.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSFERENCIAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transferencia {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "CORRESPONDENCIA_ID", length = 36, nullable = false)
    private String correspondenciaId;

    @Column(name = "DE_USUARIO_ID", length = 36, nullable = false)
    private String deUsuarioId;

    @Column(name = "A_USUARIO_ID", length = 36, nullable = false)
    private String aUsuarioId;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "COMENTARIO", length = 255)
    private String comentario;

    @JsonProperty("createdAt")
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime fechaCreacion;

    @JsonProperty("updatedAt")
    @Column(name = "UPDATED_AT")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORRESPONDENCIA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Correspondencia correspondencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DE_USUARIO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User deUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "A_USUARIO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User aUsuario;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
