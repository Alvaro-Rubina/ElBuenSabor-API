package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends GenericoRepository<Cliente, Long> {
    Optional<Cliente> findByUsuarioEmail(String usuarioEmail);
    Optional<Cliente> findByUsuario_Auth0Id(String auth0Id);
}
