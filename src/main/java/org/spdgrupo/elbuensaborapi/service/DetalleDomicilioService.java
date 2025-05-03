package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DetalleDomicilioRepository;
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

    // NOTE: No hay metodo updateDetalleDomicilio ya que lo que se edita/actualiza es el domicilio o cliente en sí

    // MAPPERS
    private DetalleDomicilio toEntity(DetalleDomicilioDTO detalleDomicilioDTO) {
        return DetalleDomicilio.builder()
                .cliente(clienteRepository.findById(detalleDomicilioDTO.getClienteId())
                        .orElseThrow(() -> new NotFoundException("Cliente con el id " + detalleDomicilioDTO.getClienteId() + " no encontrado")))
                .domicilio(domicilioService.saveDomicilio(detalleDomicilioDTO.getDomicilio()))
                .build();
    }

    public DetalleDomicilioResponseDTO toDTO(DetalleDomicilio detalleDomicilio) {
        return DetalleDomicilioResponseDTO.builder()
                .id(detalleDomicilio.getId())
                .domicilio(domicilioService.toDTO(detalleDomicilio.getDomicilio()))
                .build();
    }
}

