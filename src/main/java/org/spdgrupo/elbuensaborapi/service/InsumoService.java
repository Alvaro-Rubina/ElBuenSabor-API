package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.InsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsumoService extends GenericoServiceImpl<Insumo, InsumoDTO, InsumoResponseDTO, Long> {
    // TODO: Falta logica (aca y capaz en el DTO) para manejar que el stockActual no sea nunca menor que el stockMinimo

    // Dependencias
    @Autowired
    private InsumoRepository insumoRepository;
    @Autowired
    private RubroInsumoRepository rubroInsumoRepository;
    @Autowired
    private InsumoMapper insumoMapper;

    public InsumoService(GenericoRepository<Insumo, Long> genericoRepository, GenericoMapper<Insumo, InsumoDTO, InsumoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public Insumo save(InsumoDTO insumoDTO) {
        Insumo insumo = insumoMapper.toEntity(insumoDTO);
        insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));
        return(insumoRepository.save(insumo));
    }

    public List<InsumoResponseDTO> getInsumosByDenominacion(String denominacion) {
        return insumoRepository.findByDenominacionContainingIgnoreCase(denominacion).stream()
                .map(insumoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public void update(Long id, InsumoDTO insumoDTO) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));


        insumo.setDenominacion(insumoDTO.getDenominacion());
        insumo.setUrlImagen(insumoDTO.getUrlImagen());
        insumo.setPrecioCosto(insumoDTO.getPrecioCosto());
        insumo.setPrecioVenta(insumoDTO.getPrecioVenta());
        insumo.setStockActual(insumoDTO.getStockActual());
        insumo.setStockMinimo(insumoDTO.getStockMinimo());
        insumo.setEsParaElaborar(insumoDTO.getEsParaElaborar());
        insumo.setActivo(insumoDTO.getActivo());
        insumo.setUnidadMedida(insumoDTO.getUnidadMedida());

        insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));

        insumoRepository.save(insumo);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        insumo.setActivo(false);
        insumoRepository.save(insumo);
    }

    @Transactional
    public void actualizarEstadoInsumo(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        if (insumo.isActivo()) {
            insumo.setActivo(false);
        } else {
            insumo.setActivo(true);
        }
        insumoRepository.save(insumo);
    }

    @Transactional
    public void actualizarStock(Long id, Double cantidad) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        double nuevoStock = insumo.getStockActual() + cantidad;
        if (nuevoStock < 0) { // NOTE: Esto en caso de que se reste una cantidad al realizar un pedido y el stock quede menor a 0
            throw new IllegalArgumentException("No hay suficiente stock para realizar la operación.");
        }
        insumo.setStockActual(nuevoStock);

        if (nuevoStock < insumo.getStockMinimo()) {
            insumo.setActivo(false);
        } else {
            insumo.setActivo(true);
        }

        insumoRepository.save(insumo);
    }
}
