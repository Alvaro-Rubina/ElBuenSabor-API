package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends GenericoRepository<Producto, Long> {
    Optional<Producto> findByDenominacion(String denominacion);
    List<Producto> findByDenominacionContainingIgnoreCase(String denominacion);

    List<Producto> findByRubroId(Long rubroId);
}
