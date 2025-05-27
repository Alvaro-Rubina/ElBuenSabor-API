package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.mappers.DomicilioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DomicilioService extends GenericoServiceImpl<Domicilio, DomicilioDTO, DomicilioResponseDTO, Long> {

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private DomicilioMapper domicilioMapper;

    public DomicilioService(GenericoRepository<Domicilio, Long> baseRepository, DomicilioMapper domicilioMapper) {
        super(baseRepository, domicilioMapper);
    }

    public void deleteDomicilio(Long id){
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio con el id " + id + " no encontrado"));
        domicilio.setActivo(false);
        domicilioRepository.save(domicilio);
    }

    public void updateDomicilio(Long id, DomicilioPatchDTO domicilioDTO) {
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));

        if (domicilioDTO.getCalle() != null) {
            domicilio.setCalle(domicilioDTO.getCalle());
        }
        if (domicilioDTO.getLocalidad() != null) {
            domicilio.setLocalidad(domicilioDTO.getLocalidad());
        }
        if (domicilioDTO.getNumero() != null) {
            domicilio.setNumero(domicilioDTO.getNumero());
        }
        if (domicilioDTO.getCodigoPostal() != null) {
            domicilio.setCodigoPostal(domicilioDTO.getCodigoPostal());
        }

        if (domicilioDTO.getActivo() != null) {
            domicilio.setActivo(domicilioDTO.getActivo());
        }

        domicilioRepository.save(domicilio);
    }
}
