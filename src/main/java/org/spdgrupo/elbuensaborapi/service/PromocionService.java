package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionService {

    // Dependencias
    private final PromocionRepository promocionRepository;

    public void savePromocion(PromocionDTO promocionDTO) {

        validarFechas(promocionDTO);
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
        validarFechas(promocionDTO);
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con el id " + id + " no encontrado"));

        if (!promocion.getDenominacion().equals(promocionDTO.getDenominacion())) {
            promocion.setDenominacion(promocionDTO.getDenominacion());
        }
        if (!promocion.getUrlImagen().equals(promocionDTO.getUrlImagen())) {
            promocion.setUrlImagen(promocionDTO.getUrlImagen());
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

    public void deletePromocion(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con el id " + id + " no encontrado"));
        promocionRepository.delete(promocion);
    }

    // MAPPERS
    private Promocion toEntity(PromocionDTO promocionDTO) {
        return Promocion.builder()
                .denominacion(promocionDTO.getDenominacion())
                .urlImagen(promocionDTO.getUrlImagen())
                .fechaDesde(promocionDTO.getFechaDesde())
                .fechaHasta(promocionDTO.getFechaHasta())
                .descuento(promocionDTO.getDescuento())
                .build();
    }

    public PromocionDTO toDTO(Promocion promocion) {
        return PromocionDTO.builder()
                .id(promocion.getId())
                .denominacion(promocion.getDenominacion())
                .urlImagen(promocion.getUrlImagen())
                .fechaDesde(promocion.getFechaDesde())
                .fechaHasta(promocion.getFechaHasta())
                .descuento(promocion.getDescuento())
                .build();
    }
    private void validarFechas(PromocionDTO promocionDTO) {
        if (promocionDTO.getFechaHasta().isBefore(promocionDTO.getFechaDesde())) {
            throw new IllegalArgumentException("La fecha hasta no puede ser anterior a la fecha desde.");
        }
    }
}
