package com.proyecto.backjava.service;

import com.proyecto.backjava.constants.EstadosCorrespondencia;
import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.dto.CorrespondenciaCreateRequest;
import com.proyecto.backjava.dto.TransferenciaRequest;
import com.proyecto.backjava.entity.*;
import com.proyecto.backjava.repository.*;
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
public class CorrespondenciaService {
    private final SedeRepository sedeRepository;
    private final AreaRepository areaRepository;
    private final UserRepository userRepository;
    private final RecorridoRepository recorridoRepository;
    private final CorrespondenciaRepository correspondenciaRepository;
    private final ArchivoRepository archivoRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final HistorialEventoRepository historialEventoRepository;

    @Transactional
    public Correspondencia crear(User usuario, CorrespondenciaCreateRequest request) {
        if (request.getSedeId() == null || request.getAreaId() == null || request.getModalidad() == null || request.getContieneCorrespondencia() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sedeId, areaId, modalidad y contieneCorrespondencia son obligatorios");
        }

        if (Boolean.TRUE.equals(request.getContieneCorrespondencia()) && (request.getNombreDocumento() == null || request.getDestino() == null || request.getPersonaEntrega() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La correspondencia requiere documento, destino y persona que recibe");
        }

        Sede sede = sedeRepository.findById(request.getSedeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sede no existe"));
        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El área no existe"));

        if (!Boolean.TRUE.equals(sede.getActivo()) || !Boolean.TRUE.equals(area.getActivo()) || !request.getSedeId().equals(area.getSedeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La sede o el área no existe, está inactiva o no pertenece a la sede");
        }

        if (request.getUsuarioAsignadoId() != null) {
            User asignado = userRepository.findById(request.getUsuarioAsignadoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario asignado no existe"));
            if (!Boolean.TRUE.equals(asignado.getActivo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario asignado no existe o está inactivo");
            }
        }

        Recorrido recorrido = Recorrido.builder()
                .id(UUID.randomUUID().toString())
                .usuarioId(usuario.getId())
                .sedeId(sede.getId())
                .areaId(area.getId())
                .fecha(LocalDateTime.now())
                .modalidad(request.getModalidad())
                .build();
        Recorrido savedRecorrido = recorridoRepository.save(recorrido);

        String estado = request.getUsuarioAsignadoId() != null ? EstadosCorrespondencia.ASIGNADA : EstadosCorrespondencia.PENDIENTE;
        Correspondencia registro = Correspondencia.builder()
                .id(UUID.randomUUID().toString())
                .recorridoId(savedRecorrido.getId())
                .creadoPorId(usuario.getId())
                .usuarioAsignadoId(request.getUsuarioAsignadoId())
                .numeroDocumento("COR-" + System.currentTimeMillis())
                .contieneCorrespondencia(Boolean.TRUE.equals(request.getContieneCorrespondencia()))
                .nombreDocumento(Boolean.TRUE.equals(request.getContieneCorrespondencia()) ? request.getNombreDocumento() : null)
                .destino(Boolean.TRUE.equals(request.getContieneCorrespondencia()) ? request.getDestino() : null)
                .personaEntrega(Boolean.TRUE.equals(request.getContieneCorrespondencia()) ? request.getPersonaEntrega() : null)
                .observacion(request.getObservacion())
                .estado(estado)
                .build();

        Correspondencia created = correspondenciaRepository.save(registro);

        for (var archivo : request.getArchivos()) {
            Archivo item = Archivo.builder()
                    .id(UUID.randomUUID().toString())
                    .correspondenciaId(created.getId())
                    .nombre(archivo.getNombre())
                    .url(archivo.getUrl())
                    .tipo(archivo.getTipo())
                    .build();
            archivoRepository.save(item);
        }

        historialEventoRepository.save(HistorialEvento.builder()
                .id(UUID.randomUUID().toString())
                .correspondenciaId(created.getId())
                .usuarioId(usuario.getId())
                .tipo("CREADA")
                .detalle("Registro creado durante el recorrido")
                .fecha(LocalDateTime.now())
                .build());

        if (request.getUsuarioAsignadoId() != null) {
            historialEventoRepository.save(HistorialEvento.builder()
                    .id(UUID.randomUUID().toString())
                    .correspondenciaId(created.getId())
                    .usuarioId(usuario.getId())
                    .tipo("ASIGNADA")
                    .detalle("Asignada al usuario " + request.getUsuarioAsignadoId())
                    .fecha(LocalDateTime.now())
                    .build());
        }

        return correspondenciaRepository.findById(created.getId()).orElse(created);
    }

    public List<Correspondencia> inventario(User usuario) {
        if (Roles.USUARIO.equals(usuario.getRol())) {
            return correspondenciaRepository.findAllByOrderByCreatedAtDesc().stream()
                    .filter(c -> usuario.getId().equals(c.getUsuarioAsignadoId()))
                    .toList();
        }
        return correspondenciaRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public Correspondencia recibir(String id, User usuario) {
        Correspondencia registro = correspondenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Correspondencia no encontrada"));

        if (Roles.USUARIO.equals(usuario.getRol()) && !usuario.getId().equals(registro.getUsuarioAsignadoId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes recibir correspondencia asignada a ti");
        }

        registro.setEstado(EstadosCorrespondencia.RECIBIDA);
        registro.setRecibidaPorId(usuario.getId());
        registro.setFechaRecepcion(LocalDateTime.now());
        correspondenciaRepository.save(registro);

        historialEventoRepository.save(HistorialEvento.builder()
                .id(UUID.randomUUID().toString())
                .correspondenciaId(registro.getId())
                .usuarioId(usuario.getId())
                .tipo("RECIBIDA")
                .detalle("Correspondencia recibida en inventario")
                .fecha(LocalDateTime.now())
                .build());

        return correspondenciaRepository.findById(id).orElse(registro);
    }

    @Transactional
    public Correspondencia transferir(String id, User usuario, TransferenciaRequest request) {
        if (request.getUsuarioDestinoId() == null || request.getUsuarioDestinoId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioDestinoId es obligatorio");
        }

        Correspondencia registro = correspondenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Correspondencia no encontrada"));

        User destino = userRepository.findById(request.getUsuarioDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario destino no existe"));

        if (!Boolean.TRUE.equals(destino.getActivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario destino no existe o está inactivo");
        }

        if (Roles.USUARIO.equals(usuario.getRol()) && !usuario.getId().equals(registro.getUsuarioAsignadoId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes transferir correspondencia que tienes asignada");
        }

        registro.setUsuarioAsignadoId(destino.getId());
        registro.setEstado(EstadosCorrespondencia.ASIGNADA);
        correspondenciaRepository.save(registro);

        transferenciaRepository.save(Transferencia.builder()
                .id(UUID.randomUUID().toString())
                .correspondenciaId(registro.getId())
                .deUsuarioId(usuario.getId())
                .aUsuarioId(destino.getId())
                .comentario(request.getComentario())
                .fecha(LocalDateTime.now())
                .build());

        historialEventoRepository.save(HistorialEvento.builder()
                .id(UUID.randomUUID().toString())
                .correspondenciaId(registro.getId())
                .usuarioId(usuario.getId())
                .tipo("TRANSFERIDA")
                .detalle("Transferida al usuario " + destino.getId())
                .fecha(LocalDateTime.now())
                .build());

        return correspondenciaRepository.findById(id).orElse(registro);
    }

    public List<HistorialEvento> historial(User usuario) {
        if (Roles.ADMINISTRADOR.equals(usuario.getRol()) || Roles.CORRESPONDENCIA.equals(usuario.getRol())) {
            return historialEventoRepository.findAll();
        }
        return historialEventoRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId());
    }
}
