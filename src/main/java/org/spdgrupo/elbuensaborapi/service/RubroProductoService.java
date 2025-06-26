package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.RubroProductoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.producto.ProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroproducto.RubroProductoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.entity.RubroProducto;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.ProductoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RubroProductoService extends GenericoServiceImpl<RubroProducto, RubroProductoDTO, RubroProductoResponseDTO, Long> {

    // Dependencias
    @Autowired
    private RubroProductoRepository rubroProductoRepository;
    @Autowired
    private RubroProductoMapper rubroProductoMapper;
    @Autowired
    private ProductoService productoService;

    public RubroProductoService(GenericoRepository<RubroProducto,Long> genericoRepository, GenericoMapper<RubroProducto, RubroProductoDTO, RubroProductoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public RubroProductoResponseDTO update(Long id, RubroProductoDTO rubroProductoDTO) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));

        if (!rubroProducto.getDenominacion().equals(rubroProductoDTO.getDenominacion())) {
            rubroProducto.setDenominacion(rubroProductoDTO.getDenominacion());
        }

        if (!rubroProducto.getActivo().equals(rubroProductoDTO.getActivo())) {
            rubroProducto.setActivo(rubroProductoDTO.getActivo());
        }

        return rubroProductoMapper.toResponseDTO(rubroProductoRepository.save(rubroProducto));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));
        rubroProducto.setActivo(false);
        rubroProductoRepository.save(rubroProducto);
    }

    @Override
    @Transactional
    public String toggleActivo(Long id) {
        RubroProducto rubroProducto = rubroProductoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RubroProducto con el id " + id + " no encontrado"));

        Boolean valorAnterior = rubroProducto.getActivo();
        rubroProducto.setActivo(!rubroProducto.getActivo());
        Boolean valorActualizado = rubroProducto.getActivo();

        if (valorActualizado.equals(false)) {
            List<ProductoResponseDTO> productos = productoService.findProductosByRubroId(rubroProducto.getId());
            for (ProductoResponseDTO producto: productos) {
                productoService.delete(producto.getId());
            }
        }

        genericoRepository.save(rubroProducto);
        return "Estado 'activo' actualizado" +
                "\n- Valor anterior: " + valorAnterior +
                "\n- Valor actualizado: " + valorActualizado;
    }
}
