package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {DetallePromocionMapper.class})
public interface PromocionMapper extends GenericoMapper<Promocion, PromocionDTO, PromocionResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "detallePromociones", ignore = true)
    Promocion toEntity(PromocionDTO promocionDTO);

    @Mapping(source = "detallePromociones", target = "detallePromociones")
    PromocionResponseDTO toResponseDTO(Promocion promocion);
}