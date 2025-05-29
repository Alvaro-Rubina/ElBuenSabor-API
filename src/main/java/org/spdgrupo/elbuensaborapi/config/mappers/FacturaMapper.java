package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.factura.FacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Factura;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, PedidoMapper.class, DetalleFacturaMapper.class})
public interface FacturaMapper extends GenericoMapper<Factura, FacturaDTO, FacturaResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaFacturacion", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "horaFacturacion", expression = "java(java.time.LocalTime.now())")
    @Mapping(source = "pedidoId", target = "pedido.id")
    @Mapping(source = "clienteId", target = "cliente.id")
    @Mapping(target = "detalleFacturas", ignore = true)
    Factura toEntity(FacturaDTO facturaDTO);

    @Mapping(source = "cliente", target = "cliente")
    @Mapping(source = "pedido", target = "pedido")
    @Mapping(source = "detalleFacturas", target = "detalleFacturas")
    FacturaResponseDTO toResponseDTO(Factura factura);
}