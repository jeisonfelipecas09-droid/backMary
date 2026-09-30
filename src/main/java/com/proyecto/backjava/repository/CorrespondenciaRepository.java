package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.Correspondencia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CorrespondenciaRepository extends JpaRepository<Correspondencia, String> {
    @EntityGraph(attributePaths = {"recorrido", "creadoPor", "usuarioAsignado", "recibidaPor", "archivos"})
    Optional<Correspondencia> findById(String id);

    @EntityGraph(attributePaths = {"recorrido", "creadoPor", "usuarioAsignado", "recibidaPor", "archivos"})
    List<Correspondencia> findAllByOrderByCreatedAtDesc();
}
