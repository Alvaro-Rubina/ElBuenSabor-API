package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.PedidoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Pedido;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    public Pedido toEntity(PedidoDTO pedidoDTO) {
        return Pedido.builder()
                .fecha(LocalDate.now())
                .numero(pedidoDTO.getNumero())
                .estado(pedidoDTO.getEstado())
                .horaEstimadaFin(pedidoDTO.getHoraEstimadaFin())
                .tipoEnvio(pedidoDTO.getTipoEnvio())
                .totalVenta(pedidoDTO.getTotalVenta())
                .totalCosto(pedidoDTO.getTotalCosto())
                .formaPago(pedidoDTO.getFormaPago())
                .cliente(clienteRepository.findById(pedidoDTO.getCliente().getId())
                        .orElseThrow(() -> new NotFoundException("Cliente con el id " + pedidoDTO.getCliente().getId() + " no encontrado")))
                .domicilio(domicilioRepository.findById(pedidoDTO.getDomicilio().getId())
                        .orElseThrow(() -> new NotFoundException("Domicilio con el id " + pedidoDTO.getDomicilio().getId() + " no encontrado")))
                .build();
    }

    public PedidoDTO toDTO(Pedido pedido) {

        // TODO: ACA LA LOGICA DEL TODTO
        return null;
    }
}
