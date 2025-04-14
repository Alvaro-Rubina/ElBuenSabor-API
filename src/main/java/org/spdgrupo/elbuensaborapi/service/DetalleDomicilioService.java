package org.spdgrupo.elbuensaborapi.service;

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
public class DetalleDomicilioService {

    @Autowired
    DetalleDomicilioRepository detalleDomicilioRepository;
    @Autowired
    ClienteService clienteService;
    @Autowired
    DomicilioService domicilioService;
    @Autowired
    DomicilioRepository domicilioRepository;
    @Autowired
    ClienteRepository clienteRepository;

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

    public void updateDetalleDomicilio(DetalleDomicilioDTO detalleDomicilioDTO, Long id) {
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
                .domicilio(domicilioRepository.findById(detalleDomicilioDTO.getDomicilio().getId())
                        .orElseThrow(() -> new RuntimeException("Domicilio no encontrado")))
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

