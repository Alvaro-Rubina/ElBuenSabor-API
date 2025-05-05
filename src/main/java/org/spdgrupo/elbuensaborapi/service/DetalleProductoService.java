package org.spdgrupo.elbuensaborapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.detalleproducto.DetalleProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.DetalleProducto;
import org.spdgrupo.elbuensaborapi.repository.DetalleProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleProductoService {

    // Dependencias
    private final DetalleProductoRepository detalleProductoRepository;
    private final ProductoRepository productoRepository;
    private final InsumoService insumoService;
    private final InsumoRepository insumoRepository;

    public void saveDetalleProducto(DetalleProductoDTO detalleProductoDTO) {
        DetalleProducto detalleProducto = toEntity(detalleProductoDTO);
        detalleProductoRepository.save(detalleProducto);
    }

    @Transactional
    public void saveMultipleDetalleProducto(List<DetalleProductoDTO> detalleProductoDTOList, Long productoId) {
        detalleProductoDTOList.forEach(dto -> dto.setProductoId(productoId));

        List<DetalleProducto> detalles = detalleProductoDTOList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        detalleProductoRepository.saveAll(detalles);
    }

    public DetalleProductoResponseDTO getDetalleProductoById(Long id) {
        DetalleProducto detalleProducto = detalleProductoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de producto con el id " + id + " no encontrado"));
        return toDTO(detalleProducto);
    }

    public List<DetalleProductoResponseDTO> getAllDetallesProducto() {
        List<DetalleProducto> detallesProducto = detalleProductoRepository.findAll();
        List<DetalleProductoResponseDTO> detallesProductoDTO = new ArrayList<>();

        for (DetalleProducto detalleProducto : detallesProducto) {
            detallesProductoDTO.add(toDTO(detalleProducto));
        }
        return detallesProductoDTO;
    }

    // MAPPERS
    public DetalleProducto toEntity(DetalleProductoDTO detalleProductoDTO) {
        return DetalleProducto.builder()
                .cantidad(detalleProductoDTO.getCantidad())
                .producto(productoRepository.findById(detalleProductoDTO.getProductoId())
                        .orElseThrow(() -> new IllegalArgumentException("Producto con el id " + detalleProductoDTO.getProductoId() + " no encontrado")))
                .insumo(insumoRepository.findById(detalleProductoDTO.getInsumoId())
                        .orElseThrow(() -> new IllegalArgumentException("Insumo con el id " + detalleProductoDTO.getInsumoId() + " no encontrado")))
                .build();
    }

    public DetalleProductoResponseDTO toDTO(DetalleProducto detalleProducto) {
        return DetalleProductoResponseDTO.builder()
                .id(detalleProducto.getId())
                .cantidad(detalleProducto.getCantidad())
                .insumo(insumoService.toDTO(detalleProducto.getInsumo()))
                .build();
    }
}
