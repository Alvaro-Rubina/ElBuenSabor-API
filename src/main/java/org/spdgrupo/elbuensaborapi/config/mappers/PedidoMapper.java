package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.pedido.PedidoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, DomicilioMapper.class, DetallePedidoMapper.class})
public interface PedidoMapper extends GenericoMapper<Pedido, PedidoDTO, PedidoResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fecha", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "hora", expression = "java(java.time.LocalTime.now())")
    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "horaEstimadaFin", ignore = true)
    @Mapping(target = "totalVenta", ignore = true)
    @Mapping(target = "totalCosto", ignore = true)
    @Mapping(target = "factura", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "domicilio", ignore = true)
    @Mapping(target = "detallePedidos", ignore = true)
    @Mapping(target = "costoEnvio", ignore = true)
    Pedido toEntity(PedidoDTO pedidoDTO);

    @Mapping(source = "cliente", target = "cliente")
    @Mapping(source = "domicilio", target = "domicilio")
    @Mapping(source = "detallePedidos", target = "detallePedidos")
    PedidoResponseDTO toResponseDTO(Pedido pedido);
}