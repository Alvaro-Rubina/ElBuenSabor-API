package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.PromocionMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePromocion;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionService {

    // Dependencias
    private final PromocionRepository promocionRepository;
    private final DetallePromocionService detallePromocionService;
    private final PromocionMapper promocionMapper;

    @Transactional
    public void savePromocion(PromocionDTO promocionDTO) {
        validarFechas(promocionDTO.getFechaDesde(), promocionDTO.getFechaHasta());

        Promocion promocion = promocionMapper.toEntity(promocionDTO);

        // Manejo de detalles
        promocion.setDetallePromociones(new ArrayList<>());
        for (DetallePromocionDTO detalleDTO : promocionDTO.getDetallePromociones()) {
            DetallePromocion detalle = detallePromocionService.createDetallePromocion(detalleDTO);
            detalle.setPromocion(promocion);
            promocion.getDetallePromociones().add(detalle);
        }

        promocionRepository.save(promocion);
    }

    public PromocionResponseDTO getPromocionById(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promocion con el id " + id + " no encontrado"));
        return promocionMapper.toResponseDTO(promocion);
    }

    public List<PromocionResponseDTO> getAllPromociones() {
        return promocionRepository.findAll().stream()
                .map(promocionMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void updatePromocion(Long id, PromocionDTO promocionDTO) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promocion con el id " + id + " no encontrada"));

        validarFechas(promocionDTO.getFechaDesde(), promocionDTO.getFechaHasta());

        promocion.setDenominacion(promocionDTO.getDenominacion());
        promocion.setFechaDesde(promocionDTO.getFechaDesde());
        promocion.setFechaHasta(promocionDTO.getFechaHasta());
        promocion.setDescuento(promocionDTO.getDescuento());
        promocion.setActivo(promocionDTO.getActivo());

        promocion.getDetallePromociones().clear();
        for (DetallePromocionDTO detalleDTO : promocionDTO.getDetallePromociones()) {
            DetallePromocion detalle = detallePromocionService.createDetallePromocion(detalleDTO);
            detalle.setPromocion(promocion);
            promocion.getDetallePromociones().add(detalle);
        }

        promocionRepository.save(promocion);
    }

    private void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de finalización");
        }
    }
}
