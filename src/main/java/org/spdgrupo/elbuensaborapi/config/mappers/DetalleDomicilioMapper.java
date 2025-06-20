package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {DomicilioMapper.class})
public interface DetalleDomicilioMapper extends GenericoMapper<DetalleDomicilio, DetalleDomicilioDTO, DetalleDomicilioResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "clienteId", target = "cliente.id")
    @Mapping(source = "domicilio", target = "domicilio")
    DetalleDomicilio toEntity(DetalleDomicilioDTO detalleDomicilioDTO);

    DetalleDomicilioResponseDTO toResponseDTO(DetalleDomicilio detalleDomicilio);
}

