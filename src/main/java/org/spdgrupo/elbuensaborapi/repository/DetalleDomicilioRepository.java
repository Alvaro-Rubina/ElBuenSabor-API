package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleDomicilioRepository extends GenericoRepository<DetalleDomicilio, Long> {
    List<DetalleDomicilio> findByClienteId(Long clienteId);
}
