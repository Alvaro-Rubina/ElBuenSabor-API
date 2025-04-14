package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleDomicilioRepository extends JpaRepository<DetalleDomicilio, Long> {
    List<DetalleDomicilio> findByClienteId(Long clienteId);
}
