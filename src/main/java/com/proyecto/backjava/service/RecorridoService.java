package com.proyecto.backjava.service;

import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.dto.RecorridoActualizarRequest;
import com.proyecto.backjava.dto.RecorridoConsultaDto;
import com.proyecto.backjava.dto.RecorridoCrearRequest;
import com.proyecto.backjava.entity.Area;
import com.proyecto.backjava.entity.Recorrido;
import com.proyecto.backjava.entity.Sede;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.repository.AreaRepository;
import com.proyecto.backjava.repository.RecorridoRepository;
import com.proyecto.backjava.repository.SedeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecorridoService {
    private final RecorridoRepository recorridoRepository;
    private final SedeRepository sedeRepository;
    private final AreaRepository areaRepository;

    public List<RecorridoConsultaDto> listar(User usuario) {
        List<Recorrido> recorridos = Roles.ADMINISTRADOR.equals(usuario.getRol())
                ? recorridoRepository.findAll()
                : recorridoRepository.findAllByUsuarioIdOrderByFechaDesc(usuario.getId());
        return recorridos.stream().map(this::mapear).toList();
    }

    public RecorridoConsultaDto consultar(String id, User usuario) {
        return mapear(buscarPermitido(id, usuario));
    }

    public RecorridoConsultaDto crear(RecorridoCrearRequest request, User usuario) {
        validarSedeArea(request.getSedeId(), request.getAreaId());
        Recorrido recorrido = Recorrido.builder()
                .id(UUID.randomUUID().toString())
                .usuarioId(usuario.getId())
                .sedeId(request.getSedeId())
                .areaId(request.getAreaId())
                .fecha(LocalDateTime.now())
                .modalidad(request.getModalidad().trim())
                .build();
        return mapear(recorridoRepository.save(recorrido));
    }

    @Transactional
    public RecorridoConsultaDto actualizar(String id, RecorridoActualizarRequest request, User usuario) {
        Recorrido recorrido = buscarPermitido(id, usuario);
        String sedeId = request.getSedeId() == null ? recorrido.getSedeId() : request.getSedeId();
        String areaId = request.getAreaId() == null ? recorrido.getAreaId() : request.getAreaId();
        if (request.getSedeId() != null || request.getAreaId() != null) {
            validarSedeArea(sedeId, areaId);
            recorrido.setSedeId(sedeId);
            recorrido.setAreaId(areaId);
        }
        if (request.getModalidad() != null) {
            recorrido.setModalidad(request.getModalidad().trim());
        }
        return mapear(recorridoRepository.save(recorrido));
    }

    private Recorrido buscarPermitido(String id, User usuario) {
        Recorrido recorrido = recorridoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recorrido no encontrado"));
        if (Roles.USUARIO.equals(usuario.getRol()) && !usuario.getId().equals(recorrido.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes acceder al recorrido de otro usuario");
        }
        return recorrido;
    }

    private void validarSedeArea(String sedeId, String areaId) {
        Sede sede = sedeRepository.findById(sedeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sede no existe"));
        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El área no existe"));
        if (!Boolean.TRUE.equals(sede.getActivo()) || !Boolean.TRUE.equals(area.getActivo())
                || !sedeId.equals(area.getSedeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sede y el área deben estar activas y relacionadas");
        }
    }

    private RecorridoConsultaDto mapear(Recorrido recorrido) {
        return RecorridoConsultaDto.builder()
                .id(recorrido.getId())
                .usuarioId(recorrido.getUsuarioId())
                .sedeId(recorrido.getSedeId())
                .areaId(recorrido.getAreaId())
                .fecha(recorrido.getFecha())
                .modalidad(recorrido.getModalidad())
                .fechaCreacion(recorrido.getFechaCreacion())
                .fechaActualizacion(recorrido.getFechaActualizacion())
                .build();
    }
}
