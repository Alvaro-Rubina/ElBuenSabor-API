package org.alvarub.elbuensaborapi.repository;

import org.alvarub.elbuensaborapi.model.entity.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
