package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DetalleDomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleDomicilioService {

    // Dependencias
    private final DetalleDomicilioRepository detalleDomicilioRepository;
    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;
    private final DomicilioService domicilioService;
    private final DomicilioRepository domicilioRepository;

    // TODO: Es posible que se pueda cambiar la relcion entre Cliente y Domicilio de [N a N] a [1 a N]

    public void saveDetalleDomicilio(DetalleDomicilioDTO detalleDomicilioDTO) {
        DetalleDomicilio detalleDomicilio = toEntity(detalleDomicilioDTO);
        detalleDomicilioRepository.save(detalleDomicilio);
    }

    public DetalleDomicilioDTO getDetalleDomicilioById(Long id) {
        DetalleDomicilio detalleDomicilio = detalleDomicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleDomicilio no encontrado"));
        return toDto(detalleDomicilio);
    }

    public List<DetalleDomicilioDTO> getAllDetallesDomicilio() {
        List<DetalleDomicilio> detallesDomicilio = detalleDomicilioRepository.findAll();
        List<DetalleDomicilioDTO> detallesDomicilioDTO = new ArrayList<>();
        for (DetalleDomicilio detalle : detallesDomicilio) {
            detallesDomicilioDTO.add(toDto(detalle));
        }
        return detallesDomicilioDTO;
    }

    public List<DetalleDomicilioDTO> getDetallesDomicilioByClienteId(Long clienteId) {
        List<DetalleDomicilio> detallesDomicilio = detalleDomicilioRepository.findByClienteId(clienteId);
        List<DetalleDomicilioDTO> detallesDomicilioDTO = new ArrayList<>();
        for (DetalleDomicilio detalle : detallesDomicilio) {
            detallesDomicilioDTO.add(toDto(detalle));
        }
        return detallesDomicilioDTO;
    }

    public void updateDetalleDomicilio(Long id, DetalleDomicilioDTO detalleDomicilioDTO) {
        DetalleDomicilio detalleDomicilio = detalleDomicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleDomicilio no encontrado"));

        if (!detalleDomicilioDTO.getCliente().getId().equals(detalleDomicilio.getCliente().getId())) {
            detalleDomicilio.setCliente(clienteRepository.findById(detalleDomicilioDTO.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado")));
        }
        if (!detalleDomicilioDTO.getDomicilio().getId().equals(detalleDomicilio.getDomicilio().getId())) {
            detalleDomicilio.setDomicilio(domicilioRepository.findById(detalleDomicilioDTO.getDomicilio().getId())
                    .orElseThrow(() -> new RuntimeException("Domicilio no encontrado")));
        }

        detalleDomicilioRepository.save(detalleDomicilio);
    }

    // MAPPERS
    private DetalleDomicilio toEntity(DetalleDomicilioDTO detalleDomicilioDTO) {
        return DetalleDomicilio.builder()
                .cliente(clienteRepository.findById(detalleDomicilioDTO.getCliente().getId())
                        .orElseThrow(() -> new RuntimeException("Cliente no encontrado")))
                .domicilio(domicilioService.saveDomicilio(detalleDomicilioDTO.getDomicilio()))
                .build();
    }
    public DetalleDomicilioDTO toDto(DetalleDomicilio detalleDomicilio) {
        return DetalleDomicilioDTO.builder()
                .id(detalleDomicilio.getId())
                .cliente(clienteService.toDto(detalleDomicilio.getCliente()))
                .domicilio(domicilioService.toDto(detalleDomicilio.getDomicilio()))
                .build();
    }
}

