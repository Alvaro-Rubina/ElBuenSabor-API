package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.DetalleFacturaMapper;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detallefactura.DetalleFacturaResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.model.entity.DetallePedido;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetalleFacturaService {

    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;
    private final DetalleFacturaMapper detalleFacturaMapper;

    @Transactional
    public DetalleFactura createDetalleFactura(DetalleFacturaDTO detalleFacturaDTO) {
        validarDetalleFactura(detalleFacturaDTO);

        DetalleFactura detalleFactura = detalleFacturaMapper.toEntity(detalleFacturaDTO);

        if (detalleFacturaDTO.getProductoId() != null) {
            detalleFactura.setProducto(productoRepository.findById(detalleFacturaDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto con el id " + detalleFacturaDTO.getProductoId() + " no encontrado")));
        } else {
            detalleFactura.setInsumo(insumoRepository.findById(detalleFacturaDTO.getInsumoId())
                    .orElseThrow(() -> new NotFoundException("Insumo con el id " + detalleFacturaDTO.getInsumoId() + " no encontrado")));
        }

        calcularSubtotales(detalleFactura);

        return detalleFactura;
    }

    public DetalleFacturaResponseDTO getDetalleFactura(DetalleFactura detalleFactura) {
        return detalleFacturaMapper.toResponseDTO(detalleFactura);
    }

    private void validarDetalleFactura(DetalleFacturaDTO detalleFacturaDTO) {
        if ((detalleFacturaDTO.getProductoId() == null && detalleFacturaDTO.getInsumoId() == null) ||
                (detalleFacturaDTO.getProductoId() != null && detalleFacturaDTO.getInsumoId() != null)) {
            throw new IllegalArgumentException("Debe haber un producto o un insumo en el DetalleFactura, no pueden ser ambos nulos ni tampoco pueden estar ambos");
        }
    }

    private void calcularSubtotales(DetalleFactura detalleFactura) {
        Double precioVenta = detalleFactura.getProducto() != null ?
                detalleFactura.getProducto().getPrecioVenta() :
                detalleFactura.getInsumo().getPrecioVenta();

        Double precioCosto = detalleFactura.getProducto() != null ?
                detalleFactura.getProducto().getPrecioCosto() :
                detalleFactura.getInsumo().getPrecioCosto();

        detalleFactura.setSubTotal(precioVenta * detalleFactura.getCantidad());
        detalleFactura.setSubTotalCosto(precioCosto * detalleFactura.getCantidad());
    }
}