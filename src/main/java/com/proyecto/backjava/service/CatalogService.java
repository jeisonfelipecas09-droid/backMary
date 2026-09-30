package com.proyecto.backjava.service;

import com.proyecto.backjava.dto.CatalogoResponse;
import com.proyecto.backjava.entity.Area;
import com.proyecto.backjava.entity.Sede;
import com.proyecto.backjava.repository.AreaRepository;
import com.proyecto.backjava.repository.SedeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final SedeRepository sedeRepository;
    private final AreaRepository areaRepository;

    public CatalogoResponse listarCatalogos() {
        List<Sede> sedes = sedeRepository.findByActivoTrueOrderByNombreAsc();
        List<Area> areas = areaRepository.findByActivoTrueOrderByNombreAsc();

        List<CatalogoResponse.SedeDto> sedeDtos = sedes.stream()
                .map(sede -> new CatalogoResponse.SedeDto(sede.getId(), sede.getNombre(), sede.getActivo()))
                .toList();

        List<CatalogoResponse.AreaDto> areaDtos = areas.stream()
                .map(area -> new CatalogoResponse.AreaDto(area.getId(), area.getNombre(), area.getSedeId(), area.getActivo()))
                .toList();

        return new CatalogoResponse(sedeDtos, areaDtos);
    }
}
