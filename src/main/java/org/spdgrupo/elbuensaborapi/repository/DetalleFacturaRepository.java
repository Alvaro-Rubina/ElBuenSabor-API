package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacturaRepository extends GenericoRepository<DetalleFactura, Long> {
}
