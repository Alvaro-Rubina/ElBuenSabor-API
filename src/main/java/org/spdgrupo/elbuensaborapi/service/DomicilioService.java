package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.DomicilioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomicilioService {

    @Autowired
    DomicilioRepository domicilioRepository;

    public void saveDomicilio(DomicilioDTO domicilioDTO) throws Exception {

        try {
            Domicilio domicilio = toEntity(domicilioDTO);
            domicilioRepository.save(domicilio);
        }catch (Exception e) {
            throw new Exception("Error al guardar el domicilio");
        }
    }

    public void updateDomicilio(DomicilioDTO domicilioDTO) throws Exception {

        try {
            Domicilio domicilio = domicilioRepository.getElementById(domicilioDTO.getId());

            if (domicilio != null) {

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
            }
            domicilioRepository.save(domicilio);

        }catch (Exception e) {
            throw new Exception("Error al actualizar el domicilio");
        }
    }

    public List<Domicilio> getAllDomicilios() throws Exception {
        try {
            return domicilioRepository.findAll();
        } catch (Exception e) {
            throw new Exception("Error al obtener los domicilios");
        }
    }

    public Domicilio getDomicilioById(Long id) throws Exception {
        try {
            return domicilioRepository.getElementById(id);
        } catch (Exception e) {
            throw new Exception("Error al obtener el domicilio");
        }
    }

    public void deleteDomicilio(Long id) throws Exception {
        try {
            Domicilio domicilio = domicilioRepository.getElementById(id);
            domicilio.setActivo(false);
            domicilioRepository.save(domicilio);
        } catch (Exception e) {
            throw new Exception("Error al eliminar el domicilio");
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
