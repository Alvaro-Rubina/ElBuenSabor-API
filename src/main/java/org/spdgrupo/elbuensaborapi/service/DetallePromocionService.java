package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DetallePromocionMapper;
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
    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;
    private final DetallePromocionMapper detallePromocionMapper;

    @Transactional
    public DetallePromocion createDetallePromocion(DetallePromocionDTO detallePromocionDTO) {
        validarDetallePromocion(detallePromocionDTO);

        DetallePromocion detallePromocion = detallePromocionMapper.toEntity(detallePromocionDTO);

        // Establecer producto o insumo
        if (detallePromocionDTO.getProductoId() != null) {
            detallePromocion.setProducto(productoRepository.findById(detallePromocionDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto con el id " + detallePromocionDTO.getProductoId() + " no encontrado")));
        } else {
            detallePromocion.setInsumo(insumoRepository.findById(detallePromocionDTO.getInsumoId())
                    .orElseThrow(() -> new NotFoundException("Insumo con el id " + detallePromocionDTO.getInsumoId() + " no encontrado")));
        }

        return detallePromocion;
    }

    public DetallePromocionResponseDTO getDetallePromocion(DetallePromocion detallePromocion) {
        return detallePromocionMapper.toResponseDTO(detallePromocion);
    }

    private void validarDetallePromocion(DetallePromocionDTO detallePromocionDTO) {
        if ((detallePromocionDTO.getProductoId() == null && detallePromocionDTO.getInsumoId() == null) ||
                (detallePromocionDTO.getProductoId() != null && detallePromocionDTO.getInsumoId() != null)) {
            throw new IllegalArgumentException("Debe haber un producto o un insumo en el DetallePromocion, no pueden ser ambos nulos ni tampoco pueden estar ambos");
        }
    }
}
