package org.alvarub.elbuensaborapi.repository;

import org.alvarub.elbuensaborapi.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, buscar clientes por nombre, email, etc.
}
