package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    public void savePromocion(PromocionDTO promocionDTO) {
        Promocion promocion = toEntity(promocionDTO);
        promocionRepository.save(promocion);
    }

    public PromocionDTO getPromocionById(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con el id " + id + " no encontrado"));
        return toDTO(promocion);
    }

    public List<PromocionDTO> getAllPromociones() {
        List<Promocion> promociones = promocionRepository.findAll();
        List<PromocionDTO> promocionesDTO = new ArrayList<>();

        for (Promocion promocion : promociones) {
            promocionesDTO.add(toDTO(promocion));
        }
        return promocionesDTO;
    }

    public void updatePromocion(Long id, PromocionDTO promocionDTO) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con el id " + id + " no encontrado"));

        if (!promocion.getDenominacion().equals(promocionDTO.getDenominacion())) {
            promocion.setDenominacion(promocionDTO.getDenominacion());
        }
        if (!promocion.getFechaDesde().equals(promocionDTO.getFechaDesde())) {
            promocion.setFechaDesde(promocionDTO.getFechaDesde());
        }
        if (!promocion.getFechaHasta().equals(promocionDTO.getFechaHasta())) {
            promocion.setFechaHasta(promocionDTO.getFechaHasta());
        }
        if (!promocion.getDescuento().equals(promocionDTO.getDescuento())) {
            promocion.setDescuento(promocionDTO.getDescuento());
        }

        promocionRepository.save(promocion);
    }

    // MAPPERS
    private Promocion toEntity(PromocionDTO promocionDTO) {
        return Promocion.builder()
                .denominacion(promocionDTO.getDenominacion())
                .fechaDesde(promocionDTO.getFechaDesde())
                .fechaHasta(promocionDTO.getFechaHasta())
                .descuento(promocionDTO.getDescuento())
                .build();
    }

    public PromocionDTO toDTO(Promocion promocion) {
        return PromocionDTO.builder()
                .id(promocion.getId())
                .denominacion(promocion.getDenominacion())
                .fechaDesde(promocion.getFechaDesde())
                .fechaHasta(promocion.getFechaHasta())
                .descuento(promocion.getDescuento())
                .build();
    }
}
