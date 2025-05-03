package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DetalleDomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleDomicilioService {

    // Dependencias
    private final DetalleDomicilioRepository detalleDomicilioRepository;
    private final ClienteRepository clienteRepository;
    private final DomicilioService domicilioService;
    private final DomicilioRepository domicilioRepository;

    public void saveDetalleDomicilio(DetalleDomicilioDTO detalleDomicilioDTO) {
        DetalleDomicilio detalleDomicilio = toEntity(detalleDomicilioDTO);
        detalleDomicilioRepository.save(detalleDomicilio);
    }

    public DetalleDomicilioResponseDTO getDetalleDomicilioById(Long id) {
        DetalleDomicilio detalleDomicilio = detalleDomicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleDomicilio con el id " + id + " no encontrado"));
        return toDTO(detalleDomicilio);
    }

    public List<DetalleDomicilioResponseDTO> getAllDetallesDomicilio() {
        List<DetalleDomicilio> detallesDomicilio = detalleDomicilioRepository.findAll();
        List<DetalleDomicilioResponseDTO> detallesDomicilioDTOs = new ArrayList<>();
        
        for (DetalleDomicilio detalleDomicilio : detallesDomicilio) {
            detallesDomicilioDTOs.add(toDTO(detalleDomicilio));
        }
        return detallesDomicilioDTOs;
    }

    public List<DetalleDomicilioResponseDTO> getDetallesDomicilioByClienteId(Long clienteId) {
        List<DetalleDomicilio> detallesDomicilio = detalleDomicilioRepository.findByClienteId(clienteId);
        List<DetalleDomicilioResponseDTO> detallesDomicilioDTOs = new ArrayList<>();
        
        for (DetalleDomicilio detalleDomicilio : detallesDomicilio) {
            detallesDomicilioDTOs.add(toDTO(detalleDomicilio));
        }
        return detallesDomicilioDTOs;
    }

    // NOTE: no hay endpoint que ocupe este metodo en el controller porque no es tan necesario pero está por las dudas
    public void updateDetalleDomicilio(Long id, DetalleDomicilioDTO detalleDomicilioDTO) {
        DetalleDomicilio detalleDomicilio = detalleDomicilioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DetalleDomicilio con el id " + id + " no encontrado"));

        if (!detalleDomicilioDTO.getClienteId().equals(detalleDomicilio.getCliente().getId())) {
            detalleDomicilio.setCliente(clienteRepository.findById(detalleDomicilioDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente con el id " + detalleDomicilioDTO.getClienteId() + " no encontrado")));
        }
        if (!detalleDomicilioDTO.getDomicilioId().equals(detalleDomicilio.getDomicilio().getId())) {
            detalleDomicilio.setDomicilio(domicilioRepository.findById(detalleDomicilioDTO.getDomicilioId())
                    .orElseThrow(() -> new RuntimeException("Domicilio con el id " + detalleDomicilioDTO.getDomicilioId() + " no encontrado")));
        }

        detalleDomicilioRepository.save(detalleDomicilio);
    }

    // MAPPERS
    private DetalleDomicilio toEntity(DetalleDomicilioDTO detalleDomicilioDTO) {
        return DetalleDomicilio.builder()
                .cliente(clienteRepository.findById(detalleDomicilioDTO.getClienteId())
                        .orElseThrow(() -> new NotFoundException("Cliente con el id " + detalleDomicilioDTO.getClienteId() + " no encontrado")))
                .domicilio(domicilioRepository.findById(detalleDomicilioDTO.getDomicilioId())
                        .orElseThrow(() -> new NotFoundException("Domicilio con el id " + detalleDomicilioDTO.getDomicilioId() + " no encontrado")))
                .build();
    }

    public DetalleDomicilioResponseDTO toDTO(DetalleDomicilio detalleDomicilio) {
        return DetalleDomicilioResponseDTO.builder()
                .id(detalleDomicilio.getId())
                .domicilio(domicilioService.toDTO(detalleDomicilio.getDomicilio()))
                .build();
    }
}

