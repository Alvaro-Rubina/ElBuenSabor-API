package org.spdgrupo.elbuensaborapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.InsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoVentasDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.model.entity.Producto;
import org.spdgrupo.elbuensaborapi.model.entity.Promocion;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private PromocionService promocionService;

    public InsumoService(GenericoRepository<Insumo, Long> genericoRepository, GenericoMapper<Insumo, InsumoDTO, InsumoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public InsumoResponseDTO save(InsumoDTO insumoDTO) {
        Insumo insumo = insumoMapper.toEntity(insumoDTO);
        insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));
        insumoRepository.save(insumo);
        return insumoMapper.toResponseDTO(insumo);
    }

    public List<InsumoResponseDTO> getInsumosByDenominacion(String denominacion) {
        return insumoRepository.findByDenominacionContainingIgnoreCase(denominacion).stream()
                .map(insumoMapper::toResponseDTO)
                .toList();
    }

    public List<InsumoResponseDTO> findInsumosByRubroId(Long rubroProductoId) {
        return insumoRepository.findByRubroId(rubroProductoId).stream()
                .map(insumoMapper::toResponseDTO)
                .toList();
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
    @Transactional
    public InsumoResponseDTO update(Long id, InsumoDTO insumoDTO) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));

        if (!insumo.getDenominacion().equals(insumoDTO.getDenominacion())) {
            insumo.setDenominacion(insumoDTO.getDenominacion());
        }

        if (!insumo.getDescripcion().equals(insumoDTO.getDescripcion())) {
            insumo.setDescripcion(insumoDTO.getDescripcion());
        }

        if (!insumo.getUrlImagen().equals(insumoDTO.getUrlImagen())) {
            insumo.setUrlImagen(insumoDTO.getUrlImagen());
        }

        boolean precioCostoActualizado = false;
        if (!insumo.getPrecioCosto().equals(insumoDTO.getPrecioCosto())) {
            insumo.setPrecioCosto(insumoDTO.getPrecioCosto());
            precioCostoActualizado = true;
        }

        boolean precioVentaActualizado = false;
        if (!Objects.equals(insumo.getPrecioVenta(), insumoDTO.getPrecioVenta())) {
            insumo.setPrecioVenta(insumoDTO.getPrecioVenta());
            precioVentaActualizado = true;
        }

        if (precioCostoActualizado || precioVentaActualizado) {
            if (insumo.getEsParaElaborar()) {
                // Actualizo productos que contienen este insumo y promociones que contienen esos productos
                List<Producto> productos = productoService.findProductosByInsumoId(insumo.getId());
                for (Producto producto : productos) {
                    productoService.actualizarPreciosProducto(producto);

                    // Actualizo promociones que contienen este producto
                    List<Promocion> promociones = promocionService.findPromocionesByProductoId(producto.getId());
                    for (Promocion promocion : promociones) {
                        promocionService.actualizarPreciosPromocion(promocion);
                    }
                }
            } else {
                // Actualizo promociones que contienen directamente el insumo vendible
                List<Promocion> promociones = promocionService.findPromocionesByInsumoId(insumo.getId());
                for (Promocion promocion : promociones) {
                    promocionService.actualizarPreciosPromocion(promocion);
                }
            }
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

        if (!Objects.equals(insumo.getActivo(), insumoDTO.getActivo())) {
            insumo.setActivo(insumoDTO.getActivo());
        }

        if (!insumo.getUnidadMedida().equals(insumoDTO.getUnidadMedida())) {
            insumo.setUnidadMedida(insumoDTO.getUnidadMedida());
        }

        if (!insumo.getRubro().getId().equals(insumoDTO.getRubroId())) {
            insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));
        }

        if (insumo.getStockActual() < insumo.getStockMinimo()) {
            LOGGER.warn("El stock actual del insumo " + insumo.getDenominacion() + " es menor al minimo recomendado");
            insumo.setActivo(false);
        }

        insumoRepository.save(insumo);

        if (!insumo.getActivo() && insumo.getEsParaElaborar()) {
            LOGGER.warn("El insumo " + insumo.getDenominacion() + " no tiene stock, se desactiva el producto asociado");
            productoService.cambiarActivoProducto(id, false);
        }

        if (!insumo.getActivo() && !insumo.getEsParaElaborar()) {
            promocionService.cambiarEstadoPromocionesPorInsumo(id, false);
        }

        return insumoMapper.toResponseDTO(insumo);
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
    public void activate(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        insumo.setActivo(true);
        insumoRepository.save(insumo);
    }

    @Transactional
    public void actualizarStock(Long id, Double cantidad) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));

        double nuevoStock = insumo.getStockActual() + cantidad;
        boolean activoAnterior = insumo.getActivo();

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

        if (activoAnterior != insumo.getActivo()) {
            productoService.cambiarActivoProducto(id, insumo.getActivo() || !insumo.getEsParaElaborar());
            promocionService.cambiarEstadoPromocionesPorInsumo(id, insumo.getActivo() || insumo.getEsParaElaborar());
        }

    }

    @Override
    @Transactional
    public String toggleActivo(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));

        Boolean valorAnterior = insumo.getActivo();
        insumo.setActivo(!valorAnterior);
        Boolean valorActualizado = insumo.getActivo();

        if (valorActualizado) {
            if (!insumo.getRubro().getActivo()) {
                throw new RuntimeException("No es posible activar el insumo porque su rubro (" + insumo.getRubro().getDenominacion() + ") está inactivo");
            }
        }

        insumoRepository.save(insumo);

        if (valorAnterior && !insumo.getActivo() && insumo.getEsParaElaborar()) {
            productoService.cambiarActivoProducto(id, false);
        }

        return "Estado 'activo' actualizado" +
                "\n- Valor anterior: " + valorAnterior +
                "\n- Valor actualizado: " + valorActualizado;
    }

    @Transactional(readOnly = true)
    public List<InsumoVentasDTO> obtenerInsumosMasVendidos(LocalDate fechaDesde, LocalDate fechaHasta, int limite) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("La fechaDesde no puede ser posterior a la fechaHasta.");
        }

        List<Object[]> resultados = insumoRepository.findInsumosMasVendidos(fechaDesde, fechaHasta, PageRequest.of(0, limite));
        return resultados.stream()
                .map(obj -> new InsumoVentasDTO(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (long) ((Number) obj[2]).intValue()
                ))
                .collect(Collectors.toList());
    }

}
