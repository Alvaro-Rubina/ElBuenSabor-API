package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallepromocion.DetallePromocionResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePromocion;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetallePromocionService {

    // Dependencias
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    // MAPPERS
    public DetallePromocion toEntity(DetallePromocionDTO detallePromocionDTO) {
        Producto producto = detallePromocionDTO.getProductoId() == null ? null : productoRepository.findById(detallePromocionDTO.getProductoId())
                .orElseThrow(() -> new NotFoundException("Producto con el id " + detallePromocionDTO.getProductoId() + " no encontrado"));
        Insumo insumo = detallePromocionDTO.getInsumoId() == null ? null : insumoRepository.findById(detallePromocionDTO.getInsumoId())
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + detallePromocionDTO.getInsumoId() + " no encontrado"));

        if ((producto == null && insumo == null) || (producto != null && insumo != null)) {
            throw new IllegalArgumentException("Debe haber un producto o un insumo en el DetallePedido, no pueden ser ambos nulos ni tampoco pueden estar ambos");
        }

        return DetallePromocion.builder()
                .cantidad(detallePromocionDTO.getCantidad())
                .producto(producto)
                .insumo(insumo)
                .build();
    }

    public DetallePromocionResponseDTO toDTO(DetallePromocion detallePromocion) {
        return DetallePromocionResponseDTO.builder()
                .id(detallePromocion.getId())
                .cantidad(detallePromocion.getCantidad())
                .producto(detallePromocion.getProducto() != null ? productoService.toDTO(detallePromocion.getProducto()) : null)
                /*.insumo(detallePromocion.getInsumo() != null ? insumoService.toDTO(detallePromocion.getInsumo()) : null)*/
                .build();
    }
}
