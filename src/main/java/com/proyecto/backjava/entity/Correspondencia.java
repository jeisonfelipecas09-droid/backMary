package com.proyecto.backjava.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CORRESPONDENCIAS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Correspondencia {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "RECORRIDO_ID", length = 36, nullable = false)
    private String recorridoId;

    @Column(name = "CREADO_POR_ID", length = 36, nullable = false)
    private String creadoPorId;

    @Column(name = "USUARIO_ASIGNADO_ID", length = 36)
    private String usuarioAsignadoId;

    @Column(name = "RECIBIDA_POR_ID", length = 36)
    private String recibidaPorId;

    @Column(name = "NUMERO_DOCUMENTO", length = 40, nullable = false, unique = true)
    private String numeroDocumento;

    @Column(name = "CONTIENE_CORRESPONDENCIA", nullable = false)
    private Boolean contieneCorrespondencia = false;

    @Column(name = "NOMBRE_DOCUMENTO", length = 255)
    private String nombreDocumento;

    @Column(name = "DESTINO", length = 255)
    private String destino;

    @Column(name = "PERSONA_ENTREGA", length = 160)
    private String personaEntrega;

    @Lob
    @Column(name = "OBSERVACION")
    private String observacion;

    @Column(name = "ESTADO", length = 30, nullable = false)
    private String estado;

    @Column(name = "FECHA_RECEPCION")
    private LocalDateTime fechaRecepcion;

    @Column(name = "FECHA_ENTREGA")
    private LocalDateTime fechaEntrega;

    @JsonProperty("createdAt")
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime fechaCreacion;

    @JsonProperty("updatedAt")
    @Column(name = "UPDATED_AT")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECORRIDO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Recorrido recorrido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREADO_POR_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User creadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ASIGNADO_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User usuarioAsignado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIBIDA_POR_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User recibidaPor;

    @OneToMany(mappedBy = "correspondencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Archivo> archivos = new ArrayList<>();

    @OneToMany(mappedBy = "correspondencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transferencia> transferencias = new ArrayList<>();

    @OneToMany(mappedBy = "correspondencia", cascade = CascadeType.ALL)
    private List<HistorialEvento> eventos = new ArrayList<>();

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
