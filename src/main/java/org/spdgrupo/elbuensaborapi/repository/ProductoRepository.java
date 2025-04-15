package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByDenominacionContainingIgnoreCase(String denominacion);

    List<Producto> findByRubroDenominacionContainingIgnoreCase(String denominacion);

    List<Producto> findByRubroId(Long rubroId);
}
