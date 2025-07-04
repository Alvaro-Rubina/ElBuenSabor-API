package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromocionRepository extends GenericoRepository<Promocion, Long> {

    List<Promocion> findAllByDetallePromocionesInsumoId(Long insumoId);
    List<Promocion> findAllByDetallePromocionesProductoId(Long productoId);
}
