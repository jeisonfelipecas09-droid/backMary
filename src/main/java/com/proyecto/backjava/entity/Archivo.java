package com.proyecto.backjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ARCHIVOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Archivo {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "CORRESPONDENCIA_ID", length = 36, nullable = false)
    private String correspondenciaId;

    @Column(name = "NOMBRE", length = 255, nullable = false)
    private String nombre;

    @Column(name = "URL", length = 500, nullable = false)
    private String url;

    @Column(name = "TIPO", length = 100)
    private String tipo;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORRESPONDENCIA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Correspondencia correspondencia;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
