package com.proyecto.backjava.service;

import com.proyecto.backjava.dto.CatalogoResponse;
import com.proyecto.backjava.dto.AreaActualizarRequest;
import com.proyecto.backjava.dto.AreaConsultaDto;
import com.proyecto.backjava.dto.AreaCrearRequest;
import com.proyecto.backjava.dto.SedeActualizarRequest;
import com.proyecto.backjava.dto.SedeConsultaDto;
import com.proyecto.backjava.dto.SedeCrearRequest;
import com.proyecto.backjava.entity.Area;
import com.proyecto.backjava.entity.Sede;
import com.proyecto.backjava.repository.AreaRepository;
import com.proyecto.backjava.repository.SedeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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

    public List<SedeConsultaDto> listarSedes() {
        return sedeRepository.findAll().stream().map(this::mapearSede).toList();
    }

    public SedeConsultaDto consultarSede(String id) {
        return mapearSede(buscarSede(id));
    }

    public SedeConsultaDto crearSede(SedeCrearRequest request) {
        Sede sede = Sede.builder()
                .id(UUID.randomUUID().toString())
                .nombre(request.getNombre().trim())
                .activo(request.getActivo() == null || request.getActivo())
                .build();
        return mapearSede(sedeRepository.save(sede));
    }

    @Transactional
    public SedeConsultaDto actualizarSede(String id, SedeActualizarRequest request) {
        Sede sede = buscarSede(id);
        if (request.getNombre() != null) {
            sede.setNombre(request.getNombre().trim());
        }
        if (request.getActivo() != null) {
            sede.setActivo(request.getActivo());
        }
        return mapearSede(sedeRepository.save(sede));
    }

    public List<AreaConsultaDto> listarAreas() {
        return areaRepository.findAll().stream().map(this::mapearArea).toList();
    }

    public AreaConsultaDto consultarArea(String id) {
        return mapearArea(buscarArea(id));
    }

    public AreaConsultaDto crearArea(AreaCrearRequest request) {
        buscarSede(request.getSedeId());
        Area area = Area.builder()
                .id(UUID.randomUUID().toString())
                .nombre(request.getNombre().trim())
                .sedeId(request.getSedeId())
                .activo(request.getActivo() == null || request.getActivo())
                .build();
        return mapearArea(areaRepository.save(area));
    }

    @Transactional
    public AreaConsultaDto actualizarArea(String id, AreaActualizarRequest request) {
        Area area = buscarArea(id);
        if (request.getNombre() != null) {
            area.setNombre(request.getNombre().trim());
        }
        if (request.getSedeId() != null) {
            buscarSede(request.getSedeId());
            area.setSedeId(request.getSedeId());
        }
        if (request.getActivo() != null) {
            area.setActivo(request.getActivo());
        }
        return mapearArea(areaRepository.save(area));
    }

    private Sede buscarSede(String id) {
        return sedeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sede no encontrada"));
    }

    private Area buscarArea(String id) {
        return areaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Área no encontrada"));
    }

    private SedeConsultaDto mapearSede(Sede sede) {
        return SedeConsultaDto.builder()
                .id(sede.getId())
                .nombre(sede.getNombre())
                .activo(sede.getActivo())
                .fechaCreacion(sede.getFechaCreacion())
                .fechaActualizacion(sede.getFechaActualizacion())
                .build();
    }

    private AreaConsultaDto mapearArea(Area area) {
        return AreaConsultaDto.builder()
                .id(area.getId())
                .nombre(area.getNombre())
                .sedeId(area.getSedeId())
                .activo(area.getActivo())
                .fechaCreacion(area.getFechaCreacion())
                .fechaActualizacion(area.getFechaActualizacion())
                .build();
    }
}
