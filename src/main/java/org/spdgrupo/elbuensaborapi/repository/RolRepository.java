package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Rol;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends GenericoRepository<Rol, Long> {
    Optional<Rol> findByAuth0RolId(String name);
    Optional<Rol> findByNombre(String name);
    boolean existsByAuth0RolId(String auth0RolId);

}