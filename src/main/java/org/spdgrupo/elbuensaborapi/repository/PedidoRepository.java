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

    @Query("SELECT p FROM Pedido p " +
            "WHERE (:estado IS NULL OR p.estado = :estado) " +
            "AND p.cliente.id = :clienteId")
    List<Pedido> findPedidosByClienteIdAndEstado(@Param("clienteId") Long clienteId,
                                                 @Param("estado") Estado estado);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE YEAR(p.fecha) = :anio AND MONTH(p.fecha) = :mes")
    int countByYearAndMonth(@Param("anio") int anio, @Param("mes") int mes);

    List<Pedido> findAllByEstado(Estado estado);

    @Query("SELECT p FROM Pedido p " +
            "WHERE p.estado IN (org.spdgrupo.elbuensaborapi.model.enums.Estado.ENTREGADO, org.spdgrupo.elbuensaborapi.model.enums.Estado.CANCELADO) " +
            "AND (:fechaDesde IS NULL OR p.fecha >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR p.fecha <= :fechaHasta)")
    List<Pedido> findPedidosEntregadosYCancelados(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta);


    @Query("SELECT p FROM Pedido p " +
            "WHERE p.estado = org.spdgrupo.elbuensaborapi.model.enums.Estado.ENTREGADO " +
            "AND (:fechaDesde IS NULL OR p.fecha >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR p.fecha <= :fechaHasta)")
    List<Pedido> findPedidosEntregados(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta);


}
