package com.proyecto.backjava.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("createdAt")
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime fechaCreacion;

    @JsonProperty("updatedAt")
    @Column(name = "UPDATED_AT")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORRESPONDENCIA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Correspondencia correspondencia;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
