package org.alvarub.elbuensaborapi.repository;

import org.alvarub.elbuensaborapi.model.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar detalles de pedido por ID de pedido o producto

    // List<DetallePedido> findByPedidoId(Long pedidoId);
    // List<DetallePedido> findByProductoId(Long productoId);
}
