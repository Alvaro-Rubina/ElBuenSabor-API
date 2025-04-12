package org.alvarub.elbuensaborapi.repository;

import org.alvarub.elbuensaborapi.model.entity.DetalleDomicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleDomicilioRepository extends JpaRepository<DetalleDomicilio, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, buscar detalles de domicilio por ID de domicilio, etc.
}
