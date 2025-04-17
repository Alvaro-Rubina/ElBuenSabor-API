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
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private DomicilioService domicilioService;

    @Autowired
    private ClienteService clienteService;

    // TODO: TERMINAR ESTE SERVICE

    public void savePedido(PedidoDTO pedidoDTO) {
        Pedido pedido = toEntity(pedidoDTO);
        pedidoRepository.save(pedido);
    }

    public PedidoDTO getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + id + " no encontrado"));
        return toDTO(pedido);
    }

    public PedidoDTO getPedidoByNumero(Integer numero) {
        Pedido pedido = pedidoRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Pedido con el numero " + numero + " no encontrado"));
        return toDTO(pedido);
    }

    public List<PedidoDTO> getAllPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<PedidoDTO> pedidosDTO = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            PedidoDTO pedidoDTO = toDTO(pedido);
            pedidosDTO.add(pedidoDTO);
        }
        return pedidosDTO;
    }

    public void editPedido(Long id, PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con el id " + id + " no encontrado"));

        if (!pedido.getFecha().equals(pedidoDTO.getFecha())) {
            pedido.setFecha(pedidoDTO.getFecha());
        }

        if (!pedido.getNumero().equals(pedidoDTO.getNumero())) {
            pedido.setNumero(pedidoDTO.getNumero());
        }
        if (!pedido.getEstado().equals(pedidoDTO.getEstado())) {
            pedido.setEstado(pedidoDTO.getEstado());
        }
        if (!pedido.getHoraEstimadaFin().equals(pedidoDTO.getHoraEstimadaFin())) {
            pedido.setHoraEstimadaFin(pedidoDTO.getHoraEstimadaFin());
        }
        if (!pedido.getTipoEnvio().equals(pedidoDTO.getTipoEnvio())) {
            pedido.setTipoEnvio(pedidoDTO.getTipoEnvio());
        }
        if (!pedido.getTotalVenta().equals(pedidoDTO.getTotalVenta())) {
            pedido.setTotalVenta(pedidoDTO.getTotalVenta());
        }
        if (!pedido.getTotalCosto().equals(pedidoDTO.getTotalCosto())) {
            pedido.setTotalCosto(pedidoDTO.getTotalCosto());
        }
        if (!pedido.getFormaPago().equals(pedidoDTO.getFormaPago())) {
            pedido.setFormaPago(pedidoDTO.getFormaPago());
        }

        pedidoRepository.save(pedido);
    }

    // MAPPERS
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
        return PedidoDTO.builder()
                .id(pedido.getId())
                .fecha(pedido.getFecha())
                .numero(pedido.getNumero())
                .estado(pedido.getEstado())
                .horaEstimadaFin(pedido.getHoraEstimadaFin())
                .tipoEnvio(pedido.getTipoEnvio())
                .totalVenta(pedido.getTotalVenta())
                .totalCosto(pedido.getTotalCosto())
                .formaPago(pedido.getFormaPago())
                .cliente(clienteService.toDto(pedido.getCliente()))
                .domicilio(domicilioService.toDto(pedido.getDomicilio()))
                .build();
    }
}
