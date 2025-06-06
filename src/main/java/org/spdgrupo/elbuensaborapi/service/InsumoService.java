package org.spdgrupo.elbuensaborapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Objects;

@Service
public class InsumoService extends GenericoServiceImpl<Insumo, InsumoDTO, InsumoResponseDTO, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsumoService.class);

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
    public void save(InsumoDTO insumoDTO) {
        Insumo insumo = insumoMapper.toEntity(insumoDTO);
        insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));
        insumoRepository.save(insumo);
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


        if (!insumo.getDenominacion().equals(insumoDTO.getDenominacion())) {
            insumo.setDenominacion(insumoDTO.getDenominacion());
        }

        if (!insumo.getUrlImagen().equals(insumoDTO.getUrlImagen())) {
            insumo.setUrlImagen(insumoDTO.getUrlImagen());
        }

        if (!insumo.getPrecioCosto().equals(insumoDTO.getPrecioCosto())) {
            insumo.setPrecioCosto(insumoDTO.getPrecioCosto());
        }

        if (!Objects.equals(insumo.getPrecioVenta(), (insumoDTO.getPrecioVenta()))) {
            insumo.setPrecioVenta(insumoDTO.getPrecioVenta());
        }

        if (!insumo.getStockActual().equals(insumoDTO.getStockActual())) {
            insumo.setStockActual(insumoDTO.getStockActual());
        }

        if (!insumo.getStockMinimo().equals(insumoDTO.getStockMinimo())) {
            insumo.setStockMinimo(insumoDTO.getStockMinimo());
        }

        if (!insumo.getEsParaElaborar().equals(insumoDTO.getEsParaElaborar())) {
            insumo.setEsParaElaborar(insumoDTO.getEsParaElaborar());
        }

        if (!Objects.equals(insumo.getActivo(), (insumoDTO.getActivo()))) {
            insumo.setActivo(insumoDTO.getActivo());
        }

        if (!insumo.getUnidadMedida().equals(insumoDTO.getUnidadMedida())) {
            insumo.setUnidadMedida(insumoDTO.getUnidadMedida());
        }

        if (!insumo.getRubro().getId().equals(insumoDTO.getRubroId())) {
            insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));
        }

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
    public void actualizarStock(Long id, Double cantidad) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        double nuevoStock = insumo.getStockActual() + cantidad;
        if (nuevoStock < 0) { // NOTE: Esto en caso de que se reste una cantidad al realizar un pedido y el stock quede menor a 0
            throw new IllegalArgumentException("No hay suficiente stock para realizar la operación.");
        }
        insumo.setStockActual(nuevoStock);

        if (nuevoStock < insumo.getStockMinimo()) {
            LOGGER.warn("El stock actual del insumo " + insumo.getDenominacion() +  " es menor al minimo recomendado");
            insumo.setActivo(false);
        } else {
            insumo.setActivo(true);
        }

        insumoRepository.save(insumo);
    }
}
