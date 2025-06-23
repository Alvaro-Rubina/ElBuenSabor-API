package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends GenericoRepository<Producto, Long> {
    Optional<Producto> findByDenominacion(String denominacion);
    List<Producto> findByDenominacionContainingIgnoreCase(String denominacion);
    List<Producto> findByRubroId(Long rubroId);
    List<Producto> findByDetalleProductosInsumoId(Long insumoId);

    @Query("SELECT new org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoVentasDTO(" +
            "p.id, p.denominacion, SUM(dp.cantidad)) " +
            "FROM DetallePedido dp " +
            "JOIN dp.producto p " +
            "WHERE (:fechaDesde IS NULL OR dp.pedido.fecha >= :fechaDesde) " +
            "AND (:fechaHasta IS NULL OR dp.pedido.fecha <= :fechaHasta) " +
            "GROUP BY p.id " +
            "ORDER BY SUM(dp.cantidad) DESC")
    List<ProductoVentasDTO> findProductosMasVendidos(
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);


}
