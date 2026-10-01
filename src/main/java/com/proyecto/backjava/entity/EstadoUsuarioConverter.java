package com.proyecto.backjava.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EstadoUsuarioConverter implements AttributeConverter<EstadoUsuario, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EstadoUsuario estado) {
        if (estado == null) {
            return null;
        }
        return estado == EstadoUsuario.ACTIVO ? 1 : 0;
    }

    @Override
    public EstadoUsuario convertToEntityAttribute(Integer valor) {
        if (valor == null) {
            return null;
        }
        return switch (valor) {
            case 1 -> EstadoUsuario.ACTIVO;
            case 0 -> EstadoUsuario.INACTIVO;
            default -> throw new IllegalArgumentException("El estado del usuario debe ser 0 o 1");
        };
    }
}
