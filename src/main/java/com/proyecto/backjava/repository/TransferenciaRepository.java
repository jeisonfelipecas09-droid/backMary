package com.proyecto.backjava.repository;

import com.proyecto.backjava.entity.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, String> {
    List<Transferencia> findAllByOrderByFechaDesc();

    @Query("""
            select transferencia
            from Transferencia transferencia
            where transferencia.deUsuarioId = :usuarioId
               or transferencia.aUsuarioId = :usuarioId
            order by transferencia.fecha desc
            """)
    List<Transferencia> findByUsuarioParticipanteOrderByFechaDesc(@Param("usuarioId") String usuarioId);
}
