package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.PedidoCodigoSequence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoCodigoSequenceRepository extends JpaRepository<PedidoCodigoSequence, String> {

    Optional<PedidoCodigoSequence> findByAnioMes(String anioMes);

}
