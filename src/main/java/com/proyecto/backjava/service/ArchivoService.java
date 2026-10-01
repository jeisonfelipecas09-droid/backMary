package com.proyecto.backjava.service;

import com.proyecto.backjava.constants.Roles;
import com.proyecto.backjava.dto.ArchivoActualizarRequest;
import com.proyecto.backjava.dto.ArchivoConsultaDto;
import com.proyecto.backjava.dto.ArchivoCrearRequest;
import com.proyecto.backjava.entity.Archivo;
import com.proyecto.backjava.entity.Correspondencia;
import com.proyecto.backjava.entity.User;
import com.proyecto.backjava.repository.ArchivoRepository;
import com.proyecto.backjava.repository.CorrespondenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArchivoService {
    private final ArchivoRepository archivoRepository;
    private final CorrespondenciaRepository correspondenciaRepository;

    public List<ArchivoConsultaDto> listar(User usuario) {
        List<Archivo> archivos;
        if (Roles.ADMINISTRADOR.equals(usuario.getRol())) {
            archivos = archivoRepository.findAll();
        } else {
            List<String> idsCorrespondencia = correspondenciaRepository
                    .findAllByUsuarioAsignadoIdOrderByFechaCreacionDesc(usuario.getId())
                    .stream().map(Correspondencia::getId).toList();
            archivos = idsCorrespondencia.isEmpty()
                    ? List.of()
                    : archivoRepository.findAllByCorrespondenciaIdIn(idsCorrespondencia);
        }
        return archivos.stream().map(this::mapear).toList();
    }

    public ArchivoConsultaDto consultar(String id, User usuario) {
        return mapear(buscarPermitido(id, usuario));
    }

    public ArchivoConsultaDto crear(ArchivoCrearRequest request, User usuario) {
        buscarCorrespondenciaPermitida(request.getCorrespondenciaId(), usuario);
        Archivo archivo = Archivo.builder()
                .id(UUID.randomUUID().toString())
                .correspondenciaId(request.getCorrespondenciaId())
                .nombre(request.getNombre().trim())
                .url(request.getUrl().trim())
                .tipo(request.getTipo())
                .build();
        return mapear(archivoRepository.save(archivo));
    }

    @Transactional
    public ArchivoConsultaDto actualizar(String id, ArchivoActualizarRequest request, User usuario) {
        Archivo archivo = buscarPermitido(id, usuario);
        if (request.getNombre() != null) {
            archivo.setNombre(request.getNombre().trim());
        }
        if (request.getUrl() != null) {
            archivo.setUrl(request.getUrl().trim());
        }
        if (request.getTipo() != null) {
            archivo.setTipo(request.getTipo().trim());
        }
        return mapear(archivoRepository.save(archivo));
    }

    private Archivo buscarPermitido(String id, User usuario) {
        Archivo archivo = archivoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado"));
        buscarCorrespondenciaPermitida(archivo.getCorrespondenciaId(), usuario);
        return archivo;
    }

    private Correspondencia buscarCorrespondenciaPermitida(String id, User usuario) {
        Correspondencia correspondencia = correspondenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Correspondencia no encontrada"));
        if (Roles.USUARIO.equals(usuario.getRol())
                && !usuario.getId().equals(correspondencia.getUsuarioAsignadoId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo puedes acceder a archivos asignados a ti");
        }
        return correspondencia;
    }

    private ArchivoConsultaDto mapear(Archivo archivo) {
        return ArchivoConsultaDto.builder()
                .id(archivo.getId())
                .correspondenciaId(archivo.getCorrespondenciaId())
                .nombre(archivo.getNombre())
                .url(archivo.getUrl())
                .tipo(archivo.getTipo())
                .fechaCreacion(archivo.getFechaCreacion())
                .fechaActualizacion(archivo.getFechaActualizacion())
                .build();
    }
}
