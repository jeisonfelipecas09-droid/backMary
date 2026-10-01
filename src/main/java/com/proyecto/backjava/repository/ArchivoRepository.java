package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArchivoRepository extends JpaRepository<Archivo, String> {
    List<Archivo> findAllByCorrespondenciaIdIn(Collection<String> correspondenciaIds);
    List<Archivo> findAllByCorrespondenciaId(String correspondenciaId);
}
