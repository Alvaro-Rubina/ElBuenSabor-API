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
    public PromocionResponseDTO save(PromocionDTO promocionDTO) {
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
        double totalVenta = getTotalVenta(promocion.getDetallePromociones());
        double totalConDescuento = totalVenta - (totalVenta * (promocion.getDescuento() / 100.0));
        promocion.setPrecioVenta(totalConDescuento);
        promocion.setPrecioCosto(getTotalCosto(promocion.getDetallePromociones()));

        promocionRepository.save(promocion);
        return promocionMapper.toResponseDTO(promocion);
    }

    @Override
    @Transactional
    public PromocionResponseDTO update(Long id, PromocionDTO promocionDTO) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promocion con el id " + id + " no encontrada"));

        validarFechas(promocionDTO.getFechaDesde(), promocionDTO.getFechaHasta());

        if (!promocion.getDenominacion().equals(promocionDTO.getDenominacion())) {
            promocion.setDenominacion(promocionDTO.getDenominacion());
        }

        if (!promocion.getDescripcion().equals(promocionDTO.getDescripcion())) {
            promocion.setDescripcion(promocionDTO.getDescripcion());
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

        if (!promocion.getActivo().equals(promocionDTO.getActivo())) {
            promocion.setActivo(promocionDTO.getActivo());
        }

        promocion.getDetallePromociones().clear();
        for (DetallePromocionDTO detalleDTO : promocionDTO.getDetallePromociones()) {
            DetallePromocion detalle = detallePromocionService.createDetallePromocion(detalleDTO);
            detalle.setPromocion(promocion);
            promocion.getDetallePromociones().add(detalle);
        }
        promocion.setPrecioCosto(getTotalCosto(promocion.getDetallePromociones()));
        double precioVenta = getTotalVenta(promocion.getDetallePromociones());
        double totalConDescuento = precioVenta - (precioVenta * (promocion.getDescuento() / 100.0));
        promocion.setPrecioVenta(totalConDescuento);

        return promocionMapper.toResponseDTO(promocionRepository.save(promocion));
    }

    private void validarFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        if (fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de finalización");
        }
    }
    @Transactional
    public void cambiarEstadoPromocionesPorProducto(Long productoId, boolean activo) {
        List<Promocion> promociones = promocionRepository.findAllByDetallePromocionesProductoId(productoId);
        cambiarEstadoPromociones(promociones, activo);
    }

    @Transactional
    public void cambiarEstadoPromocionesPorInsumo(Long insumoId, boolean activo) {
        List<Promocion> promociones = promocionRepository.findAllByDetallePromocionesInsumoId(insumoId);
        cambiarEstadoPromociones(promociones, activo);
    }

    // Metodos adicionales
    private void cambiarEstadoPromociones(List<Promocion> promociones, boolean activo) {

        for (Promocion promocion : promociones) {
            LocalDate now = LocalDate.now();

            boolean estadoActivo = now.isAfter(promocion.getFechaDesde().minusDays(1))
                    && now.isBefore(promocion.getFechaHasta().plusDays(1));

            if (!estadoActivo) {
                continue;
            }

            boolean todosActivos = promocion.getDetallePromociones().stream().allMatch(detalle ->
                    (detalle.getProducto() == null || Boolean.TRUE.equals(detalle.getProducto().getActivo())) &&
                    (detalle.getInsumo() == null || Boolean.TRUE.equals(detalle.getInsumo().getActivo()))
            );

            boolean estadoAnterior = promocion.getActivo();

            if (activo && !todosActivos) {
                promocion.setActivo(false);
            } else {
                promocion.setActivo(activo);
            }

            if (estadoAnterior != promocion.getActivo()) {
                promocionRepository.save(promocion);
            }
        }
    }

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
