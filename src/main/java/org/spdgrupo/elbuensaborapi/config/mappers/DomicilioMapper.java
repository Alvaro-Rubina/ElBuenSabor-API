package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring")
public interface DomicilioMapper extends GenericoMapper<Domicilio, DomicilioDTO, DomicilioResponseDTO> {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Domicilio toEntity(DomicilioDTO domicilioDTO);

    DomicilioResponseDTO toResponseDTO(Domicilio domicilio);
}