package com.proyecto.backjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RECORRIDOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recorrido {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "USUARIO_ID", length = 36, nullable = false)
    private String usuarioId;

    @Column(name = "SEDE_ID", length = 36, nullable = false)
    private String sedeId;

    @Column(name = "AREA_ID", length = 36, nullable = false)
    private String areaId;

    @Column(name = "FECHA", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "MODALIDAD", length = 120, nullable = false)
    private String modalidad;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEDE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Sede sede;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Area area;

    @OneToMany(mappedBy = "recorrido", cascade = CascadeType.ALL)
    private List<Correspondencia> correspondencias = new ArrayList<>();

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
