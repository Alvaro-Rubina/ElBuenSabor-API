package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DomicilioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DomicilioService extends GenericoServiceImpl<Domicilio, DomicilioDTO, DomicilioResponseDTO, Long> {

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private DomicilioMapper domicilioMapper;

    public DomicilioService(GenericoRepository<Domicilio, Long> genericoRepository, GenericoMapper<Domicilio,DomicilioDTO,DomicilioResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
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
