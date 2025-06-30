package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends GenericoRepository<Producto, Long> {
    Optional<Producto> findByDenominacion(String denominacion);
    List<Producto> findByDenominacionContainingIgnoreCase(String denominacion);
    List<Producto> findByRubroId(Long rubroId);
    List<Producto> findByDetalleProductosInsumoId(Long insumoId);

    @Query(
            value = """
    SELECT 
        p.id, 
        p.denominacion, 
        SUM(cantidad_total) AS cantidadVendidos
    FROM (
        SELECT 
            dp.producto_id AS producto_id, 
            p.denominacion, 
            SUM(dp.cantidad) AS cantidad_total
        FROM detalle_pedido dp
        JOIN producto p ON dp.producto_id = p.id
        JOIN pedido ped ON dp.pedido_id = ped.id
        WHERE ped.estado = 'ENTREGADO'
          AND (:fechaDesde IS NULL OR ped.fecha >= :fechaDesde)
          AND (:fechaHasta IS NULL OR ped.fecha <= :fechaHasta)
        GROUP BY dp.producto_id, p.denominacion

        UNION ALL

        SELECT 
            dprom.producto_id AS producto_id, 
            p2.denominacion, 
            SUM(dp2.cantidad * dprom.cantidad) AS cantidad_total
        FROM detalle_pedido dp2
        JOIN promocion promo ON dp2.promocion_id = promo.id
        JOIN detalle_promocion dprom ON dprom.promocion_id = promo.id
        JOIN producto p2 ON dprom.producto_id = p2.id
        JOIN pedido ped2 ON dp2.pedido_id = ped2.id
        WHERE ped2.estado = 'ENTREGADO'
          AND (:fechaDesde IS NULL OR ped2.fecha >= :fechaDesde)
          AND (:fechaHasta IS NULL OR ped2.fecha <= :fechaHasta)
        GROUP BY dprom.producto_id, p2.denominacion
    ) AS ventas
    JOIN producto p ON ventas.producto_id = p.id
    GROUP BY p.id, p.denominacion
    ORDER BY cantidadVendidos DESC
    """,
            nativeQuery = true
    )
    List<Object[]> findProductosMasVendidos(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable
    );

}
