package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByCodigo(String codigo);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE YEAR(p.fecha) = :anio AND MONTH(p.fecha) = :mes")
    int countByYearAndMonth(@Param("anio") int anio, @Param("mes") int mes);

}
