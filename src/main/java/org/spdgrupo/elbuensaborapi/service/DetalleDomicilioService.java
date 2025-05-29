package org.spdgrupo.elbuensaborapi.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DetalleDomicilioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalledomicilio.DetalleDomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.spdgrupo.elbuensaborapi.repository.DetalleDomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class DetalleDomicilioService extends GenericoServiceImpl<DetalleDomicilio, DetalleDomicilioDTO, DetalleDomicilioResponseDTO, Long> {

    // Dependencias
    @Autowired
    private DetalleDomicilioRepository detalleDomicilioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DomicilioService domicilioService;
    @Autowired
    private DetalleDomicilioMapper detalleDomicilioMapper;

    public DetalleDomicilioService(GenericoRepository<DetalleDomicilio, Long> genericoRepository, GenericoMapper<DetalleDomicilio, DetalleDomicilioDTO, DetalleDomicilioResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public DetalleDomicilio save(DetalleDomicilioDTO detalleDomicilioDTO) {
        DetalleDomicilio detalleDomicilio = DetalleDomicilio.builder()
                .cliente(clienteRepository.findById(detalleDomicilioDTO.getClienteId())
                        .orElseThrow(() -> new NotFoundException("Cliente con el id " + detalleDomicilioDTO.getClienteId() + " no encontrado")))
                .domicilio(domicilioService.save(detalleDomicilioDTO.getDomicilio()))
                .build();
        return (detalleDomicilioRepository.save(detalleDomicilio));
    }
    // TODO: Ver si este metodo se queda o hacer uno que cumpla la misma funcion en ClienteService
    public List<DetalleDomicilioResponseDTO> getDetallesDomicilioByClienteId(Long clienteId) {
        return detalleDomicilioRepository.findByClienteId(clienteId).stream()
                .map(detalleDomicilioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}

