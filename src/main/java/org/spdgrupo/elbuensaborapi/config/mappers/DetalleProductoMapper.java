package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;

@Mapper(componentModel = "spring", uses = {InsumoMapper.class})
public interface DetalleProductoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "insumoId", target = "insumo.id")
    DetalleProducto toEntity(DetalleProductoDTO detalleProductoDTO);

    DetalleProductoResponseDTO toResponseDTO(DetalleProducto detalleProducto);
}