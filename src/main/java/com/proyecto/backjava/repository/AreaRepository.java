package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, String> {
    List<Area> findByActivoTrueOrderByNombreAsc();
    List<Area> findBySedeIdAndActivoTrueOrderByNombreAsc(String sedeId);
}
