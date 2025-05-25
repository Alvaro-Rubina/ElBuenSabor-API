package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DetalleDomicilioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DetalleDomicilioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleDomicilioService {

    // Dependencias
    private final DetalleDomicilioRepository detalleDomicilioRepository;
    private final ClienteRepository clienteRepository;
    private final DomicilioService domicilioService;
    private final DetalleDomicilioMapper detalleDomicilioMapper;

    public void saveDetalleDomicilio(DetalleDomicilioDTO detalleDomicilioDTO) {
        DetalleDomicilio detalleDomicilio = DetalleDomicilio.builder()
                .cliente(clienteRepository.findById(detalleDomicilioDTO.getClienteId())
                        .orElseThrow(() -> new NotFoundException("Cliente con el id " + detalleDomicilioDTO.getClienteId() + " no encontrado")))
                .domicilio(domicilioService.saveDomicilio(detalleDomicilioDTO.getDomicilio()))
                .build();
        detalleDomicilioRepository.save(detalleDomicilio);
    }

    public DetalleDomicilioResponseDTO getDetalleDomicilioById(Long id) {
        DetalleDomicilio detalleDomicilio = detalleDomicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleDomicilio con el id " + id + " no encontrado"));
        return detalleDomicilioMapper.toResponseDTO(detalleDomicilio);
    }

    public List<DetalleDomicilioResponseDTO> getAllDetallesDomicilio() {
        return detalleDomicilioRepository.findAll().stream()
                .map(detalleDomicilioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // TODO: Ver si este metodo se queda o hacer uno que cumpla la misma funcion en ClienteService
    public List<DetalleDomicilioResponseDTO> getDetallesDomicilioByClienteId(Long clienteId) {
        return detalleDomicilioRepository.findByClienteId(clienteId).stream()
                .map(detalleDomicilioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

