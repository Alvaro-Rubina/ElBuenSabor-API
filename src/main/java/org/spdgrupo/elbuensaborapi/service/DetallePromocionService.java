package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.DetallePromocionDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePromocion;
import org.spdgrupo.elbuensaborapi.repository.DetallePromocionRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetallePromocionService {

    @Autowired
    private DetallePromocionRepository detallePromocionRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private PromocionService promocionService;

    @Autowired
    private PromocionRepository promocionRepository;

    public void saveDetallePromocion(DetallePromocionDTO detallePromocionDTO) {
        DetallePromocion detallePromocion = toEntity(detallePromocionDTO);
        detallePromocionRepository.save(detallePromocion);
    }

    public DetallePromocionDTO getDetallePromocionById(Long id) {
        DetallePromocion detallePromocion = detallePromocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetallePromocion con el id " + id + " no encontrado"));
        return toDTO(detallePromocion);
    }

    public List<DetallePromocionDTO> getAllDetallesPromocion() {
        List<DetallePromocion> detallesPromocion = detallePromocionRepository.findAll();
        List<DetallePromocionDTO> detallesPromocionDTO = new ArrayList<>();

        for (DetallePromocion detallePromocion : detallesPromocion) {
            detallesPromocionDTO.add(toDTO(detallePromocion));
        }
        return detallesPromocionDTO;
    }

    public void updateDetallePromocion(Long id, DetallePromocionDTO detallePromocionDTO) {
        DetallePromocion detallePromocion = detallePromocionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetallePromocion con el id " + id + " no encontrado"));

        if (!detallePromocion.getCantidad().equals(detallePromocionDTO.getCantidad())) {
            detallePromocion.setCantidad(detallePromocionDTO.getCantidad());
        }

        if (!detallePromocion.getPromocion().getId().equals(detallePromocionDTO.getPromocion().getId())) {
            detallePromocion.setPromocion(promocionRepository.findById(detallePromocionDTO.getPromocion().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Promocion con el id " + detallePromocionDTO.getPromocion().getId() + " no encontrada")));
        }

        if (!detallePromocion.getProducto().getId().equals(detallePromocionDTO.getProducto().getId())) {
            detallePromocion.setProducto(productoRepository.findById(detallePromocionDTO.getProducto().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto con el id " + detallePromocionDTO.getProducto().getId() + " no encontrado")));
        }

        if (!detallePromocion.getInsumo().getId().equals(detallePromocionDTO.getInsumo().getId())) {
            detallePromocion.setInsumo(insumoRepository.findById(detallePromocionDTO.getInsumo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Insumo con el id " + detallePromocionDTO.getInsumo().getId() + " no encontrado")));
        }

    }

    // MAPPERS
    private DetallePromocion toEntity(DetallePromocionDTO detallePromocionDTO) {
        return DetallePromocion.builder()
                .cantidad(detallePromocionDTO.getCantidad())
                .promocion(
                        promocionRepository.findById(detallePromocionDTO.getPromocion().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Promocion con el id " + detallePromocionDTO.getPromocion().getId() + " no encontrada"))
                )
                .producto(
                        detallePromocionDTO.getProducto() != null && detallePromocionDTO.getProducto().getId() != null
                                ? productoRepository.findById(detallePromocionDTO.getProducto().getId()).orElse(null)
                                : null
                )
                .insumo(
                        detallePromocionDTO.getInsumo() != null && detallePromocionDTO.getInsumo().getId() != null
                                ? insumoRepository.findById(detallePromocionDTO.getInsumo().getId()).orElse(null)
                                : null
                )
                .build();
    }

    public DetallePromocionDTO toDTO(DetallePromocion detallePromocion) {
        return DetallePromocionDTO.builder()
                .id(detallePromocion.getId())
                .cantidad(detallePromocion.getCantidad())
                .promocion(promocionService.toDTO(detallePromocion.getPromocion()))
                .producto(detallePromocion.getProducto() != null ? productoService.toDTO(detallePromocion.getProducto()) : null)
                .insumo(detallePromocion.getInsumo() != null ? insumoService.toDTO(detallePromocion.getInsumo()) : null)
                .build();
    }
}
