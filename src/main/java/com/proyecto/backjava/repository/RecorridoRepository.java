package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.Recorrido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecorridoRepository extends JpaRepository<Recorrido, String> {
    List<Recorrido> findAllByUsuarioIdOrderByFechaDesc(String usuarioId);
}
