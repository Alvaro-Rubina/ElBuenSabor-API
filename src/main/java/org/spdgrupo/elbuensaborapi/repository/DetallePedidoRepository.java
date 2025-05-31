package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends GenericoRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);
}
