package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;

@Mapper(componentModel = "spring", uses = {DomicilioMapper.class})
public interface DetalleDomicilioMapper {

    DetalleDomicilioResponseDTO toResponseDTO(DetalleDomicilio detalleDomicilio);
}

