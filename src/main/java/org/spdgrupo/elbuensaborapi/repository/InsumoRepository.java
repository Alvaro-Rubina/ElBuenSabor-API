package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InsumoRepository extends GenericoRepository<Insumo, Long> {

    List<Insumo> findByDenominacionContainingIgnoreCase(String denominacion);

    List<Insumo> findByEsParaElaborarFalse();

    @Query("SELECT new org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoVentasDTO(" +
            "i.id, i.denominacion, SUM(dp.cantidad)) " +
            "FROM DetallePedido dp " +
            "JOIN dp.insumo i " +
            "WHERE i.esParaElaborar = false " +
            "AND dp.pedido.estado = 'ENTREGADO' " + // Filtro por estado "ENTREGADO"
            "AND (:fechaDesde IS NULL OR dp.pedido.fecha >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR dp.pedido.fecha <= :fechaHasta) " +
            "GROUP BY i.id " +
            "ORDER BY SUM(dp.cantidad) DESC")
    List<InsumoVentasDTO> findInsumosMasVendidos(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);

}
