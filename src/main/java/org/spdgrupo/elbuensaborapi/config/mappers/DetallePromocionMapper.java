package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePromocion;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class, InsumoMapper.class})
public interface DetallePromocionMapper extends GenericoMapper<DetallePromocion, DetallePromocionDTO, DetallePromocionResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "productoId", target = "producto.id")
    @Mapping(source = "insumoId", target = "insumo.id")
    DetallePromocion toEntity(DetallePromocionDTO detallePromocionDTO);

    @Mapping(source = "producto", target = "producto")
    @Mapping(source = "insumo", target = "insumo")
    DetallePromocionResponseDTO toResponseDTO(DetallePromocion detallePromocion);
}