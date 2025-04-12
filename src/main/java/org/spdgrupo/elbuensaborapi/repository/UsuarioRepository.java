package org.spdgrupo.elbuensaborapi.repository;

import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, buscar por nombre de usuario o correo electrónico
    // List<Usuario> findByUsername(String username);
    // List<Usuario> findByEmail(String email);
}
