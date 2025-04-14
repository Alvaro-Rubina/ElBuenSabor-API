package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.model.dto.DetalleFacturaDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleFactura;
import org.spdgrupo.elbuensaborapi.repository.DetalleFacturaRepository;
import org.spdgrupo.elbuensaborapi.repository.FacturaRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleFacturaService {

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaService facturaService;

    public void saveDetalleFactura(DetalleFacturaDTO detalleFacturaDTO) {
        DetalleFactura detalleFactura = toEntity(detalleFacturaDTO);
        detalleFacturaRepository.save(detalleFactura);
    }

    public DetalleFacturaDTO getDetalleFacturaById(Long id) {
        DetalleFactura detalleFactura = detalleFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleFactura con el id " + id + " no encontrado"));
        return toDTO(detalleFactura);
    }

    public List<DetalleFacturaDTO> getAllDetallesFactura() {
        List<DetalleFactura> detallesFactura = detalleFacturaRepository.findAll();
        List<DetalleFacturaDTO> detallesFacturaDTO = new ArrayList<>();

        for (DetalleFactura detalleFactura : detallesFactura) {
            detallesFacturaDTO.add(toDTO(detalleFactura));
        }
        return detallesFacturaDTO;
    }

    public void updateDetalleFactura(Long id, DetalleFacturaDTO detalleFacturaDTO) {
        DetalleFactura detalleFactura = detalleFacturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DetalleFactura con el id " + id + " no encontrado"));

        if (!detalleFactura.getCantidad().equals(detalleFacturaDTO.getCantidad())) {
            detalleFactura.setCantidad(detalleFacturaDTO.getCantidad());
        }

        if (!detalleFactura.getSubTotal().equals(detalleFacturaDTO.getSubTotal())) {
            detalleFactura.setSubTotal(detalleFacturaDTO.getSubTotal());
        }

        if (!detalleFactura.getFactura().getId().equals(detalleFacturaDTO.getFactura().getId())) {
            detalleFactura.setFactura(facturaRepository.findById(detalleFacturaDTO.getFactura().getId())
                    .orElseThrow(() -> new RuntimeException("Factura con el id " + detalleFacturaDTO.getFactura().getId() + " no encontrada")));
        }

        if (!detalleFactura.getProducto().getId().equals(detalleFacturaDTO.getProducto().getId())) {
            detalleFactura.setProducto(productoRepository.findById(detalleFacturaDTO.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto con el id " + detalleFacturaDTO.getProducto().getId() + " no encontrado")));
        }

        if (!detalleFactura.getInsumo().getId().equals(detalleFacturaDTO.getInsumo().getId())) {
            detalleFactura.setInsumo(insumoRepository.findById(detalleFacturaDTO.getInsumo().getId())
                    .orElseThrow(() -> new RuntimeException("Insumo con el id " + detalleFacturaDTO.getInsumo().getId() + " no encontrado")));
        }

        detalleFacturaRepository.save(detalleFactura);
    }

    // MAPPERS
    public DetalleFactura toEntity(DetalleFacturaDTO detalleFacturaDTO) {
        return DetalleFactura.builder()
                .cantidad(detalleFacturaDTO.getCantidad())
                .subTotal(detalleFacturaDTO.getSubTotal())
                .factura(facturaRepository.findById(detalleFacturaDTO.getFactura().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Factura con el id " + detalleFacturaDTO.getFactura().getId() + " no encontrada")))
                .producto(productoRepository.findById(detalleFacturaDTO.getProducto().getId()).orElse(null))
                .insumo(insumoRepository.findById(detalleFacturaDTO.getInsumo().getId()).orElse(null))
                .build();
    }

    public DetalleFacturaDTO toDTO(DetalleFactura detalleFactura) {
        return DetalleFacturaDTO.builder()
                .id(detalleFactura.getId())
                .cantidad(detalleFactura.getCantidad())
                .subTotal(detalleFactura.getSubTotal())
                .factura(facturaService.toDTO(detalleFactura.getFactura()))
                .producto(productoService.toDTO(detalleFactura.getProducto()))
                .insumo(insumoService.toDTO(detalleFactura.getInsumo()))
                .build();
    }
}
