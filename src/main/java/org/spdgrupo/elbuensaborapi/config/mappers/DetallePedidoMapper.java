package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepedido.DetallePedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {ProductoMapper.class, InsumoMapper.class, PromocionMapper.class})
public interface DetallePedidoMapper extends GenericoMapper<DetallePedido, DetallePedidoDTO, DetallePedidoResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "insumo", ignore = true)
    @Mapping(target = "promocion", ignore = true)
    @Mapping(target = "subTotal", ignore = true)
    @Mapping(target = "subTotalCosto", ignore = true)
    DetallePedido toEntity(DetallePedidoDTO detallePedidoDTO);

    @Mapping(source = "producto", target = "producto")
    @Mapping(source = "insumo", target = "insumo")
    @Mapping(source = "promocion", target = "promocion")
    DetallePedidoResponseDTO toResponseDTO(DetallePedido detallePedido);
}