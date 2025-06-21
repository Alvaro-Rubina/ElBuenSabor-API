package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends GenericoRepository<Factura, Long> {
    Optional<Factura> findByPedido_Id(Long id);

}
