package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.PromocionMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.promocion.PromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePromocion;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromocionService extends GenericoServiceImpl<Promocion, PromocionDTO, PromocionResponseDTO, Long> {

    // Dependencias
    @Autowired
    private PromocionRepository promocionRepository;
    @Autowired
    private DetallePromocionService detallePromocionService;
    @Autowired
    private PromocionMapper promocionMapper;

    public PromocionService(GenericoRepository<Promocion, Long> genericoRepository, GenericoMapper<Promocion, PromocionDTO, PromocionResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public void save(PromocionDTO promocionDTO) {
        validarFechas(promocionDTO.getFechaDesde(), promocionDTO.getFechaHasta());

        Promocion promocion = promocionMapper.toEntity(promocionDTO);

        // Manejo de detalles
        promocion.setDetallePromociones(new ArrayList<>());
        for (DetallePromocionDTO detalleDTO : promocionDTO.getDetallePromociones()) {
            DetallePromocion detalle = detallePromocionService.createDetallePromocion(detalleDTO);
            detalle.setPromocion(promocion);
            promocion.getDetallePromociones().add(detalle);
        }

        // calculo totales
        promocion.setTotal(getTotalVenta(promocion.getDetallePromociones()));
        promocion.setTotalCosto(getTotalCosto(promocion.getDetallePromociones()));

        promocionRepository.save(promocion);
    }

    @Override
    @Transactional
    public void update(Long id, PromocionDTO promocionDTO) {
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

    // Metodos adicionales
    private Double getTotalVenta(List<DetallePromocion> detallePromociones) {
        Double totalVenta = 0.0;

        for (DetallePromocion detallePromocion : detallePromociones) {
            totalVenta += detallePromocion.getSubTotal();
        }
        return totalVenta;
    }

    private Double getTotalCosto(List<DetallePromocion> detallePromociones) {
        Double totalCosto = 0.0;

        for (DetallePromocion detallePromocion: detallePromociones) {
            totalCosto += detallePromocion.getSubTotalCosto();
        }

        return totalCosto;
    }
}
