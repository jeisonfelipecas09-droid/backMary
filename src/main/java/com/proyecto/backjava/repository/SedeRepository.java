package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SedeRepository extends JpaRepository<Sede, String> {
    List<Sede> findByActivoTrueOrderByNombreAsc();
}
