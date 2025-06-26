package org.spdgrupo.elbuensaborapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.InsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Autowired
    private ProductoService productoService;

    public InsumoService(GenericoRepository<Insumo, Long> genericoRepository, GenericoMapper<Insumo, InsumoDTO, InsumoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @CacheEvict(value = "insumos", allEntries = true)
    @Transactional
    public InsumoResponseDTO save(InsumoDTO insumoDTO) {
        Insumo insumo = insumoMapper.toEntity(insumoDTO);
        insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));
        insumoRepository.save(insumo);
        return insumoMapper.toResponseDTO(insumo);
    }

    @Cacheable(value = "insumos", key = "'getInsumosByDenominacion_'+#denominacion")
    public List<InsumoResponseDTO> getInsumosByDenominacion(String denominacion) {
        return insumoRepository.findByDenominacionContainingIgnoreCase(denominacion).stream()
                .map(insumoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @CachePut(value = "insumos", key = "'update_'+#id")
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

        if (nuevoStock < 0) {
            throw new RuntimeException("No hay suficiente stock para realizar la operación.");
        }

        insumo.setStockActual(nuevoStock);

        if (nuevoStock < insumo.getStockMinimo()) {
            LOGGER.warn("El stock actual del insumo " + insumo.getDenominacion() +  " es menor al minimo recomendado");
            insumo.setActivo(false);
        } else {
            insumo.setActivo(true);
        }

        insumoRepository.save(insumo);

        if (!insumo.getActivo() && insumo.getEsParaElaborar()) {
            productoService.cambiarActivoProducto(id);
        }
    }

    @Override
    @CachePut(value = "insumos", key = "'toggleActivo_'+#id")
    @Transactional
    public void toggleActivo(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        boolean estabaActivo = insumo.getActivo();
        insumo.setActivo(!estabaActivo);
        insumoRepository.save(insumo);

        if (estabaActivo && !insumo.getActivo() && insumo.getEsParaElaborar()) {
            productoService.cambiarActivoProducto(id);
        }
    }

    @Transactional(readOnly = true)
    public List<InsumoVentasDTO> obtenerInsumosMasVendidos(LocalDate fechaDesde, LocalDate fechaHasta, int limite) {
        // Validación de fechas opcional
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("La fechaDesde no puede ser posterior a la fechaHasta.");
        }

        Pageable pageable = PageRequest.of(0, limite); // Limitar la cantidad de resultados
        return insumoRepository.findInsumosMasVendidos(fechaDesde, fechaHasta, pageable);
    }

    public List<InsumoResponseDTO> findAllVendibles() {
        return insumoRepository.findByEsParaElaborarFalse().stream()
                .map(insumoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<InsumoResponseDTO> findAllActivos() {
        return insumoRepository.findByActivoTrue().stream()
                .map(insumoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable("insumos")
    public List<InsumoResponseDTO> findAll() {
        return super.findAll();
    }

    @Override
    @Cacheable(value = "insumos", key = "'findById_'+#id")
    public InsumoResponseDTO findById(Long id) {
        return super.findById(id);
    }

}
