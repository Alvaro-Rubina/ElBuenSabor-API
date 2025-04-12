package org.alvarub.elbuensaborapi.repository;

import org.alvarub.elbuensaborapi.model.entity.MercadoPagoDatos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoPagoDatosRepository extends JpaRepository<MercadoPagoDatos, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
