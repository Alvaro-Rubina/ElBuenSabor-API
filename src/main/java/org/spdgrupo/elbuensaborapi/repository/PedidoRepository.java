package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends GenericoRepository<Pedido, Long> {
    Optional<Pedido> findByCodigo(String codigo);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE YEAR(p.fecha) = :anio AND MONTH(p.fecha) = :mes")
    int countByYearAndMonth(@Param("anio") int anio, @Param("mes") int mes);

    List<Pedido> findAllByEstado(Estado estado);

}
