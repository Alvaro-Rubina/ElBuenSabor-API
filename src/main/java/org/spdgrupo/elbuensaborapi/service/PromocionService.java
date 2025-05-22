package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
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

    public void savePromocion(PromocionDTO promocionDTO) {
        Promocion promocion = toEntity(promocionDTO);
        promocionRepository.save(promocion);
    }

    public PromocionResponseDTO getPromocionById(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con el id " + id + " no encontrado"));
        return toDTO(promocion);
    }

    public List<PromocionResponseDTO> getAllPromociones() {
        List<Promocion> promociones = promocionRepository.findAll();
        List<PromocionResponseDTO> promocionesDTO = new ArrayList<>();

        for (Promocion promocion : promociones) {
            promocionesDTO.add(toDTO(promocion));
        }
        return promocionesDTO;
    }

    public void updatePromocion(Long id, PromocionPatchDTO promocionDTO) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promocion con el id " + id + " no encontrado"));

        validarFechas(promocionDTO.getFechaDesde(), promocionDTO.getFechaHasta());

        if (promocion.getDenominacion() != null) {
            promocion.setDenominacion(promocionDTO.getDenominacion());
        }
        if (promocionDTO.getUrlImagen() != null) {
            promocion.setUrlImagen(promocionDTO.getUrlImagen());
        }
        if (promocion.getFechaDesde() != null) {
            promocion.setFechaDesde(promocionDTO.getFechaDesde());
        }
        if (promocion.getFechaHasta() != null) {
            promocion.setFechaHasta(promocionDTO.getFechaHasta());
        }
        if (promocion.getDenominacion() != null) {
            promocion.setDescuento(promocionDTO.getDescuento());
        }

        if (promocionDTO.getDetallePromociones() != null) {
            promocion.setDetallePromociones(new ArrayList<>());
            for (DetallePromocionDTO detalleDTO: promocionDTO.getDetallePromociones()) {
                DetallePromocion detalle = detallePromocionService.toEntity(detalleDTO);
                detalle.setPromocion(promocion);
                promocion.getDetallePromociones().add(detalle);
            }
        }

        promocionRepository.save(promocion);
    }

    // MAPPERS
    private Promocion toEntity(PromocionDTO promocionDTO) {
        validarFechas(promocionDTO.getFechaDesde(), promocionDTO.getFechaHasta());

        Promocion promocion = Promocion.builder()
                .denominacion(promocionDTO.getDenominacion())
                .urlImagen(promocionDTO.getUrlImagen())
                .fechaDesde(promocionDTO.getFechaDesde())
                .fechaHasta(promocionDTO.getFechaHasta())
                .descuento(promocionDTO.getDescuento())
                .detallePromociones(new ArrayList<>())
                .build();

        for (DetallePromocionDTO detalleDTO: promocionDTO.getDetallePromociones()) {
            DetallePromocion detalle = detallePromocionService.toEntity(detalleDTO);
            detalle.setPromocion(promocion);
            promocion.getDetallePromociones().add(detalle);
        }

        return promocion;
    }

    public PromocionResponseDTO toDTO(Promocion promocion) {
        return PromocionResponseDTO.builder()
                .id(promocion.getId())
                .denominacion(promocion.getDenominacion())
                .urlImagen(promocion.getUrlImagen())
                .fechaDesde(promocion.getFechaDesde())
                .fechaHasta(promocion.getFechaHasta())
                .descuento(promocion.getDescuento())
                .detallePromociones(promocion.getDetallePromociones().stream()
                        .map(detallePromocionService::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // Metodos adicionales
    private void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de finalización");
        }
    }
}
