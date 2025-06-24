package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DomicilioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleDomicilio;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.DetalleDomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DomicilioService extends GenericoServiceImpl<Domicilio, DomicilioDTO, DomicilioResponseDTO, Long> {

    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private DetalleDomicilioRepository detalleDomicilioRepository;
    @Autowired
    private DomicilioMapper domicilioMapper;

    public DomicilioService(GenericoRepository<Domicilio, Long> genericoRepository, GenericoMapper<Domicilio,DomicilioDTO,DomicilioResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    public List<DomicilioResponseDTO> getDomiciliosByClienteId(Long clienteId){
        List<DetalleDomicilio> detalleDomicilios = detalleDomicilioRepository.findByClienteId(clienteId);
        return detalleDomicilios.stream()
                .map(detalleDomicilio -> domicilioMapper.toResponseDTO(detalleDomicilio.getDomicilio()))
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id){
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domicilio con el id " + id + " no encontrado"));
        domicilio.setActivo(false);
        domicilioRepository.save(domicilio);
    }

    @Override
    @Transactional
    public void update(Long id, DomicilioDTO domicilioDTO) {
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Domicilio no encontrado"));

        // Actualizamos todos los campos
        domicilio.setCalle(domicilioDTO.getCalle());
        domicilio.setLocalidad(domicilioDTO.getLocalidad());
        domicilio.setNumero(domicilioDTO.getNumero());
        domicilio.setCodigoPostal(domicilioDTO.getCodigoPostal());
        domicilio.setActivo(true);

        domicilioRepository.save(domicilio);
    }
}
