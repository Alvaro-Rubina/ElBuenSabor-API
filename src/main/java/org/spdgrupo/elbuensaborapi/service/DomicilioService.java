package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomicilioService {

    @Autowired
    DomicilioRepository domicilioRepository;

    public void saveDomicilio(DomicilioDTO domicilioDTO) throws Exception {

        try {

        }catch (Exception e) {
            throw new Exception("Error al guardar el domicilio");
        }
    }



    public Domicilio toEntity(DomicilioDTO domicilioDTO) {
        return Domicilio.builder()
                .calle(domicilioDTO.getCalle())
                .localidad(domicilioDTO.getLocalidad())
                .numero(domicilioDTO.getNumero())
                .codigoPostal(domicilioDTO.getCodigoPostal())
                .activo(true)
                .build();
    }
    public static DomicilioDTO toDto(Domicilio domicilio) {
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
