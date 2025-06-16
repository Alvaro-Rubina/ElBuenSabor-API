package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends GenericoRepository<Producto, Long> {
    List<Producto> findByDenominacionContainingIgnoreCase(String denominacion);

    List<Producto> findByRubroId(Long rubroId);

    List<Producto> findByDetalleProductosInsumoId(Long insumoId);

}
