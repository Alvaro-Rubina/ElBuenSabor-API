package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.PromocionMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePromocion;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void updatePromocion(Long id, PromocionPatchDTO promocionDTO) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promocion con el id " + id + " no encontrado"));

        if (promocionDTO.getFechaDesde() != null && promocionDTO.getFechaHasta() != null) {
            validarFechas(promocionDTO.getFechaDesde(), promocionDTO.getFechaHasta());
        }

        updatePromocionFields(promocion, promocionDTO);

        if (promocionDTO.getDetallePromociones() != null) {
            updateDetallePromociones(promocion, promocionDTO.getDetallePromociones());
        }

        promocionRepository.save(promocion);
    }

    private void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de finalización");
        }
    }

    private void updatePromocionFields(Promocion promocion, PromocionPatchDTO promocionDTO) {
        if (promocionDTO.getDenominacion() != null) promocion.setDenominacion(promocionDTO.getDenominacion());
        if (promocionDTO.getUrlImagen() != null) promocion.setUrlImagen(promocionDTO.getUrlImagen());
        if (promocionDTO.getFechaDesde() != null) promocion.setFechaDesde(promocionDTO.getFechaDesde());
        if (promocionDTO.getFechaHasta() != null) promocion.setFechaHasta(promocionDTO.getFechaHasta());
        if (promocionDTO.getDescuento() != null) promocion.setDescuento(promocionDTO.getDescuento());
    }

    private void updateDetallePromociones(Promocion promocion, List<DetallePromocionDTO> detallesDTO) {
        promocion.getDetallePromociones().clear();
        for (DetallePromocionDTO detalleDTO : detallesDTO) {
            DetallePromocion detalle = detallePromocionService.createDetallePromocion(detalleDTO);
            detalle.setPromocion(promocion);
            promocion.getDetallePromociones().add(detalle);
        }
    }
}
