package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomicilioService {

    // Dependencias
    private final DomicilioRepository domicilioRepository;

    public Domicilio saveDomicilio(DomicilioDTO domicilioDTO){
        Domicilio domicilio = toEntity(domicilioDTO);
        domicilioRepository.save(domicilio);
        return domicilio;
    }

    public List<DomicilioDTO> getAllDomicilios(){
        List<Domicilio> domicilios = domicilioRepository.findAll();
        List<DomicilioDTO> domiciliosDTO = new java.util.ArrayList<>();

        for (Domicilio domicilio : domicilios) {
            domiciliosDTO.add(toDto(domicilio));
        }

        return domiciliosDTO;
    }

    public DomicilioDTO getDomicilioById(Long id){
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));
        return toDto(domicilio);
    }

    public void deleteDomicilio(Long id){

        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));
        domicilio.setActivo(false);
        domicilioRepository.save(domicilio);
    }

    public void updateDomicilio(Long id, DomicilioDTO domicilioDTO) {
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));

        if (!domicilioDTO.getCalle().equals(domicilio.getCalle())) {
            domicilio.setCalle(domicilioDTO.getCalle());
        }
        if (!domicilioDTO.getLocalidad().equals(domicilio.getLocalidad())) {
            domicilio.setLocalidad(domicilioDTO.getLocalidad());
        }
        if (!domicilioDTO.getNumero().equals(domicilio.getNumero())) {
            domicilio.setNumero(domicilioDTO.getNumero());
        }
        if (!domicilioDTO.getCodigoPostal().equals(domicilio.getCodigoPostal())) {
            domicilio.setCodigoPostal(domicilioDTO.getCodigoPostal());
        }
        if (!domicilioDTO.getActivo().equals(domicilio.getActivo())) {
            domicilio.setActivo(domicilioDTO.getActivo());
        }

        domicilioRepository.save(domicilio);
    }

    // MAPPERS
    private Domicilio toEntity(DomicilioDTO domicilioDTO) {
        return Domicilio.builder()
                .calle(domicilioDTO.getCalle())
                .localidad(domicilioDTO.getLocalidad())
                .numero(domicilioDTO.getNumero())
                .codigoPostal(domicilioDTO.getCodigoPostal())
                .activo(true)
                .build();
    }
    public DomicilioDTO toDto(Domicilio domicilio) {
        return DomicilioDTO.builder()
                .id(domicilio.getId())
                .calle(domicilio.getCalle())
                .localidad(domicilio.getLocalidad())
                .numero(domicilio.getNumero())
                .codigoPostal(domicilio.getCodigoPostal())
                .activo(domicilio.getActivo())
                .build();
    }
}
