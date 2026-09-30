package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivoRepository extends JpaRepository<Archivo, String> {
}
