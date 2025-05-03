package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.dto.domicilio.DomicilioResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<DomicilioResponseDTO> getAllDomicilios(){
        List<Domicilio> domicilios = domicilioRepository.findAll();
        List<DomicilioResponseDTO> domicilioDTOs = new ArrayList<>();

        for (Domicilio domicilio : domicilios) {
            domicilioDTOs.add(toDto(domicilio));
        }

        return domicilioDTOs;
    }

    public DomicilioResponseDTO getDomicilioById(Long id){
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio con el id " + id + " no encontrado"));
        return toDto(domicilio);
    }

    public void deleteDomicilio(Long id){
        Domicilio domicilio = domicilioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domicilio con el id " + id + " no encontrado"));
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
        if (!domicilioDTO.getInfoAdicional().equals(domicilio.getInfoAdicional())) {
            domicilio.setInfoAdicional(domicilioDTO.getInfoAdicional());
        }

        if (!domicilioDTO.isActivo() == domicilio.isActivo()) {
            domicilio.setActivo(domicilioDTO.isActivo());
        }

        domicilioRepository.save(domicilio);
    }

    // MAPPERS
    private Domicilio toEntity(DomicilioDTO domicilioDTO) {
        return Domicilio.builder()
                .calle(domicilioDTO.getCalle())
                .numero(domicilioDTO.getNumero())
                .localidad(domicilioDTO.getLocalidad())
                .codigoPostal(domicilioDTO.getCodigoPostal())
                .infoAdicional(domicilioDTO.getInfoAdicional() == null ? "Sin información adicional" : domicilioDTO.getInfoAdicional())
                .activo(true) // cuando se GUARDA un domicilio, se le asigna el valor activo = true por defecto
                .build();
    }
    public DomicilioResponseDTO toDto(Domicilio domicilio) {
        return DomicilioResponseDTO.builder()
                .id(domicilio.getId())
                .calle(domicilio.getCalle())
                .numero(domicilio.getNumero())
                .localidad(domicilio.getLocalidad())
                .codigoPostal(domicilio.getCodigoPostal())
                .activo(domicilio.isActivo())
                .build();
    }
}
