package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {
}
