package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring")
public interface RubroProductoMapper extends GenericoMapper<RubroProducto, RubroProductoDTO, RubroProductoResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    RubroProducto toEntity(RubroProductoDTO rubroProductoDTO);

    RubroProductoResponseDTO toResponseDTO(RubroProducto rubroProducto);

}
