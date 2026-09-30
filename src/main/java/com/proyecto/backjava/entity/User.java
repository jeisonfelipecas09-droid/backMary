package com.proyecto.backjava.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @Column(name = "ID", length = 36)
    private String id;

    @Column(name = "NOMBRE", length = 80, nullable = false)
    private String nombre;

    @Column(name = "APELLIDOS", length = 120, nullable = false)
    private String apellidos;

    @Column(name = "EMAIL", length = 160, nullable = false, unique = true)
    private String email;

    @Column(name = "TELEFONO", length = 30)
    private String telefono;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Column(name = "ROL", length = 30, nullable = false)
    private String rol;

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo = true;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recorrido> recorridos = new ArrayList<>();

    @OneToMany(mappedBy = "creadoPor", cascade = CascadeType.ALL)
    private List<Correspondencia> creadas = new ArrayList<>();

    @OneToMany(mappedBy = "usuarioAsignado", cascade = CascadeType.ALL)
    private List<Correspondencia> asignadas = new ArrayList<>();

    @OneToMany(mappedBy = "recibidaPor", cascade = CascadeType.ALL)
    private List<Correspondencia> recibidas = new ArrayList<>();

    @OneToMany(mappedBy = "deUsuario", cascade = CascadeType.ALL)
    private List<Transferencia> transferenciasRealizadas = new ArrayList<>();

    @OneToMany(mappedBy = "aUsuario", cascade = CascadeType.ALL)
    private List<Transferencia> transferenciasRecibidas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<HistorialEvento> eventos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(activo);
    }
}
