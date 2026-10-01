package com.proyecto.backjava.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HISTORIAL_EVENTOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialEvento {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "CORRESPONDENCIA_ID", length = 36, nullable = false)
    private String correspondenciaId;

    @Column(name = "USUARIO_ID", length = 36, nullable = false)
    private String usuarioId;

    @Column(name = "TIPO", length = 60, nullable = false)
    private String tipo;

    @Lob
    @Column(name = "DETALLE")
    private String detalle;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

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
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User usuario;

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
