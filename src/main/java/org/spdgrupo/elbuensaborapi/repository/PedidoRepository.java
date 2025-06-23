package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.model.enums.Estado;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends GenericoRepository<Pedido, Long> {
    Optional<Pedido> findByCodigo(String codigo);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE YEAR(p.fecha) = :anio AND MONTH(p.fecha) = :mes")
    int countByYearAndMonth(@Param("anio") int anio, @Param("mes") int mes);

    List<Pedido> findAllByEstado(Estado estado);

    @Query("SELECT SUM(" +
            "CASE WHEN p.estado = 'ENTREGADO' THEN p.totalVenta ELSE 0 END) AS ingresos, " +
            "SUM(p.totalCosto) AS egresos " +
            "FROM Pedido p " +
            "WHERE p.estado IN ('ENTREGADO', 'CANCELADO') " +
            "AND (:fechaDesde IS NULL OR p.fecha >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR p.fecha <= :fechaHasta)")
    Object[] calcularIngresosEgresos(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta);


}
