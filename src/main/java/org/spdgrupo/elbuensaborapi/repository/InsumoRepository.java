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

    List<Insumo> findByRubroId(Long rubroId);

    List<Insumo> findByActivoTrue();


    @Query(
            value = """
    SELECT 
        i.id, 
        i.denominacion, 
        SUM(cantidad_total) AS cantidadVendidos
    FROM (
        -- Insumos vendidos directamente
        SELECT 
            dp.insumo_id AS insumo_id, 
            i.denominacion, 
            SUM(dp.cantidad) AS cantidad_total
        FROM detalle_pedido dp
        JOIN insumo i ON dp.insumo_id = i.id
        JOIN pedido ped ON dp.pedido_id = ped.id
        WHERE i.es_para_elaborar = false
          AND ped.estado = 'ENTREGADO'
          AND (:fechaDesde IS NULL OR ped.fecha >= :fechaDesde)
          AND (:fechaHasta IS NULL OR ped.fecha <= :fechaHasta)
        GROUP BY dp.insumo_id, i.denominacion

        UNION ALL

        -- Insumos vendidos a través de promociones
        SELECT 
            dprom.insumo_id AS insumo_id, 
            i2.denominacion, 
            SUM(dp2.cantidad * dprom.cantidad) AS cantidad_total
        FROM detalle_pedido dp2
        JOIN promocion promo ON dp2.promocion_id = promo.id
        JOIN detalle_promocion dprom ON dprom.promocion_id = promo.id
        JOIN insumo i2 ON dprom.insumo_id = i2.id
        JOIN pedido ped2 ON dp2.pedido_id = ped2.id
        WHERE i2.es_para_elaborar = false
          AND ped2.estado = 'ENTREGADO'
          AND (:fechaDesde IS NULL OR ped2.fecha >= :fechaDesde)
          AND (:fechaHasta IS NULL OR ped2.fecha <= :fechaHasta)
        GROUP BY dprom.insumo_id, i2.denominacion
    ) AS ventas
    JOIN insumo i ON ventas.insumo_id = i.id
    GROUP BY i.id, i.denominacion
    ORDER BY cantidadVendidos DESC
    """,
            nativeQuery = true
    )
    List<Object[]> findInsumosMasVendidos(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable
    );
}
