package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {InsumoMapper.class})
public interface DetalleProductoMapper extends GenericoMapper<DetalleProducto, DetalleProductoDTO, DetalleProductoResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "insumoId", target = "insumo.id")
    DetalleProducto toEntity(DetalleProductoDTO detalleProductoDTO);

    DetalleProductoResponseDTO toResponseDTO(DetalleProducto detalleProducto);
}