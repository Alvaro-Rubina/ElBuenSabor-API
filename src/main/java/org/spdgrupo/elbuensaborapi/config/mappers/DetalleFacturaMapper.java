package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class, InsumoMapper.class})
public interface DetalleFacturaMapper extends GenericoMapper<DetalleFactura, DetalleFacturaDTO, DetalleFacturaResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "insumo", ignore = true)
    @Mapping(target = "promocion", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "subTotalCosto", ignore = true)
    DetalleFactura toEntity(DetalleFacturaDTO detalleFacturaDTO);

    @Mapping(source = "producto", target = "producto")
    @Mapping(source = "insumo", target = "insumo")
    @Mapping(target = "promocion", ignore = true)
    DetalleFacturaResponseDTO toResponseDTO(DetalleFactura detalleFactura);
}