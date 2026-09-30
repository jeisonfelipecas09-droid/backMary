package com.proyecto.backjava.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AREAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "NOMBRE", length = 120, nullable = false)
    private String nombre;

    @Column(name = "SEDE_ID", length = 36, nullable = false)
    private String sedeId;

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEDE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Sede sede;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<Recorrido> recorridos = new ArrayList<>();

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
