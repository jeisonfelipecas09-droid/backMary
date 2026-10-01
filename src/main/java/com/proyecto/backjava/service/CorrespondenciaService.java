package com.proyecto.backjava.service;

import com.proyecto.backjava.constants.EstadosCorrespondencia;
import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.dto.ArchivoConsultaDto;
import com.proyecto.backjava.dto.CorrespondenciaActualizarRequest;
import com.proyecto.backjava.dto.CorrespondenciaConsultaDto;
import com.proyecto.backjava.dto.CorrespondenciaCreateRequest;
import com.proyecto.backjava.dto.HistorialConsultaDto;
import com.proyecto.backjava.dto.TransferenciaConsultaDto;
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
    public CorrespondenciaConsultaDto crear(User usuario, CorrespondenciaCreateRequest request) {
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
            if (!asignado.isEnabled()) {
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

        return mapear(correspondenciaRepository.findById(created.getId()).orElse(created));
    }

    public CorrespondenciaConsultaDto consultar(String id, User usuario) {
        return mapear(buscarPermitida(id, usuario));
    }

    public List<CorrespondenciaConsultaDto> inventario(User usuario) {
        if (Roles.USUARIO.equals(usuario.getRol())) {
            return correspondenciaRepository.findAllByUsuarioAsignadoIdOrderByFechaCreacionDesc(usuario.getId())
                    .stream().map(this::mapear).toList();
        }
        return correspondenciaRepository.findAllByOrderByFechaCreacionDesc().stream().map(this::mapear).toList();
    }

    @Transactional
    public CorrespondenciaConsultaDto actualizar(String id, CorrespondenciaActualizarRequest request, User usuario) {
        Correspondencia registro = buscarPermitida(id, usuario);
        if (request.getNombreDocumento() != null) {
            registro.setNombreDocumento(request.getNombreDocumento().trim());
        }
        if (request.getDestino() != null) {
            registro.setDestino(request.getDestino().trim());
        }
        if (request.getPersonaEntrega() != null) {
            registro.setPersonaEntrega(request.getPersonaEntrega().trim());
        }
        if (request.getObservacion() != null) {
            registro.setObservacion(request.getObservacion().trim());
        }
        return mapear(correspondenciaRepository.save(registro));
    }

    @Transactional
    public CorrespondenciaConsultaDto recibir(String id, User usuario) {
        Correspondencia registro = buscarPermitida(id, usuario);

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

        return mapear(correspondenciaRepository.findById(id).orElse(registro));
    }

    @Transactional
    public CorrespondenciaConsultaDto transferir(String id, User usuario, TransferenciaRequest request) {
        if (request.getUsuarioDestinoId() == null || request.getUsuarioDestinoId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioDestinoId es obligatorio");
        }

        Correspondencia registro = buscarPermitida(id, usuario);

        User destino = userRepository.findById(request.getUsuarioDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario destino no existe"));

        if (!destino.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario destino no existe o está inactivo");
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

        return mapear(correspondenciaRepository.findById(id).orElse(registro));
    }

    public List<HistorialConsultaDto> historial(User usuario) {
        List<HistorialEvento> eventos = Roles.ADMINISTRADOR.equals(usuario.getRol())
                ? historialEventoRepository.findAll()
                : historialEventoRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId());
        return eventos.stream().map(evento -> HistorialConsultaDto.builder()
                .id(evento.getId())
                .correspondenciaId(evento.getCorrespondenciaId())
                .usuarioId(evento.getUsuarioId())
                .tipo(evento.getTipo())
                .detalle(evento.getDetalle())
                .fecha(evento.getFecha())
                .build()).toList();
    }

    public List<TransferenciaConsultaDto> transferencias(User usuario) {
        List<Transferencia> transferencias = Roles.ADMINISTRADOR.equals(usuario.getRol())
                ? transferenciaRepository.findAllByOrderByFechaDesc()
                : transferenciaRepository.findByUsuarioParticipanteOrderByFechaDesc(usuario.getId());
        return transferencias.stream().map(transferencia -> TransferenciaConsultaDto.builder()
                .id(transferencia.getId())
                .correspondenciaId(transferencia.getCorrespondenciaId())
                .deUsuarioId(transferencia.getDeUsuarioId())
                .aUsuarioId(transferencia.getAUsuarioId())
                .fecha(transferencia.getFecha())
                .comentario(transferencia.getComentario())
                .build()).toList();
    }

    public HistorialConsultaDto consultarEvento(String id, User usuario) {
        HistorialEvento evento = historialEventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado"));
        if (Roles.USUARIO.equals(usuario.getRol()) && !usuario.getId().equals(evento.getUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes consultar eventos registrados por ti");
        }
        return HistorialConsultaDto.builder()
                .id(evento.getId())
                .correspondenciaId(evento.getCorrespondenciaId())
                .usuarioId(evento.getUsuarioId())
                .tipo(evento.getTipo())
                .detalle(evento.getDetalle())
                .fecha(evento.getFecha())
                .build();
    }

    public TransferenciaConsultaDto consultarTransferencia(String id, User usuario) {
        Transferencia transferencia = transferenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transferencia no encontrada"));
        if (Roles.USUARIO.equals(usuario.getRol())
                && !usuario.getId().equals(transferencia.getDeUsuarioId())
                && !usuario.getId().equals(transferencia.getAUsuarioId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes consultar esta transferencia");
        }
        return TransferenciaConsultaDto.builder()
                .id(transferencia.getId())
                .correspondenciaId(transferencia.getCorrespondenciaId())
                .deUsuarioId(transferencia.getDeUsuarioId())
                .aUsuarioId(transferencia.getAUsuarioId())
                .fecha(transferencia.getFecha())
                .comentario(transferencia.getComentario())
                .build();
    }

    private Correspondencia buscarPermitida(String id, User usuario) {
        Correspondencia registro = correspondenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Correspondencia no encontrada"));
        if (Roles.USUARIO.equals(usuario.getRol()) && !usuario.getId().equals(registro.getUsuarioAsignadoId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes acceder a correspondencia asignada a ti");
        }
        return registro;
    }

    private CorrespondenciaConsultaDto mapear(Correspondencia registro) {
        return CorrespondenciaConsultaDto.builder()
                .id(registro.getId())
                .recorridoId(registro.getRecorridoId())
                .creadoPorId(registro.getCreadoPorId())
                .usuarioAsignadoId(registro.getUsuarioAsignadoId())
                .recibidaPorId(registro.getRecibidaPorId())
                .numeroDocumento(registro.getNumeroDocumento())
                .contieneCorrespondencia(registro.getContieneCorrespondencia())
                .nombreDocumento(registro.getNombreDocumento())
                .destino(registro.getDestino())
                .personaEntrega(registro.getPersonaEntrega())
                .observacion(registro.getObservacion())
                .estado(registro.getEstado())
                .fechaRecepcion(registro.getFechaRecepcion())
                .fechaEntrega(registro.getFechaEntrega())
                .fechaCreacion(registro.getFechaCreacion())
                .fechaActualizacion(registro.getFechaActualizacion())
                .archivos(registro.getArchivos().stream().map(archivo -> ArchivoConsultaDto.builder()
                        .id(archivo.getId())
                        .correspondenciaId(archivo.getCorrespondenciaId())
                        .nombre(archivo.getNombre())
                        .url(archivo.getUrl())
                        .tipo(archivo.getTipo())
                        .fechaCreacion(archivo.getFechaCreacion())
                        .fechaActualizacion(archivo.getFechaActualizacion())
                        .build()).toList())
                .build();
    }
}
