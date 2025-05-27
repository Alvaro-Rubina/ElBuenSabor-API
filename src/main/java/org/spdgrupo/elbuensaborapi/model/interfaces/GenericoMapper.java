package org.spdgrupo.elbuensaborapi.model.interfaces;

import org.spdgrupo.elbuensaborapi.model.entity.Base;

public interface GenericoMapper<E extends Base, D, R> {

    E toEntity(D dto);
    R toResponseDTO(E entity);
}
