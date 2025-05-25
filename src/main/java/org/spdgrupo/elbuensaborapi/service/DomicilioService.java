package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.mappers.DomicilioMapper;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DomicilioService {

    // Dependencias
    private final DomicilioRepository domicilioRepository;
    private final DomicilioMapper domicilioMapper;

    public Domicilio saveDomicilio(DomicilioDTO domicilioDTO){
        Domicilio domicilio = domicilioMapper.toEntity(domicilioDTO);
        domicilioRepository.save(domicilio);
        return domicilio;
    }

    public List<DomicilioResponseDTO> getAllDomicilios(){
        return domicilioRepository.findAll().stream()
                .map(domicilioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DomicilioResponseDTO getDomicilioById(Long id){
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio con el id " + id + " no encontrado"));
        return domicilioMapper.toResponseDTO(domicilio);
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
    public DomicilioResponseDTO toDTO(Domicilio domicilio) {
        return DomicilioResponseDTO.builder()
                .id(domicilio.getId())
                .calle(domicilio.getCalle())
                .numero(domicilio.getNumero())
                .localidad(domicilio.getLocalidad())
                .codigoPostal(domicilio.getCodigoPostal())
                .activo(domicilio.getActivo())
                .build();
    }
}
