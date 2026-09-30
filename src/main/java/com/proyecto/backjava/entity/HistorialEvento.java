package com.proyecto.backjava.entity;

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

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

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
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
