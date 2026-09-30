package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.HistorialEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEventoRepository extends JpaRepository<HistorialEvento, String> {
    List<HistorialEvento> findByCorrespondenciaIdOrderByFechaDesc(String correspondenciaId);
    List<HistorialEvento> findByUsuarioIdOrderByFechaDesc(String usuarioId);
}
