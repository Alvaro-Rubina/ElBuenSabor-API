package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends GenericoRepository<Empleado, Long> {
    Optional<Empleado> findByUsuarioEmail(String usuarioEmail);
    Optional<Empleado> findByUsuario_Auth0Id(String auth0Id);
}
