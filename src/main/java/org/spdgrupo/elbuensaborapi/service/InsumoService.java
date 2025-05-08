package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoService {

    // Dependencias
    private final InsumoRepository insumoRepository;
    private final RubroInsumoRepository rubroInsumoRepository;
    private final RubroInsumoService rubroInsumoService;

    public void saveInsumo(InsumoDTO insumoDTO) {
        Insumo insumo = toEntity(insumoDTO);
        insumoRepository.save(insumo);
    }

    public InsumoResponseDTO getInsumoById(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        return toDTO(insumo);
    }

    public List<InsumoResponseDTO> getInsumosByDenominacion(String denominacion) {
        List<Insumo> insumos = insumoRepository.findByDenominacionContainingIgnoreCase(denominacion);
        List<InsumoResponseDTO> insumosDTO = new ArrayList<>();

        for (Insumo insumo : insumos) {
            insumosDTO.add(toDTO(insumo));
        }
        return insumosDTO;
    }

    // Acá busca por rubro, y si no se le pasa parametro (o es 0), busca todos
    public List<InsumoResponseDTO> getAllInsumos(Long rubroId) {
        List<Insumo> insumos;

        if (rubroId == null || rubroId == 0L) {
            insumos = insumoRepository.findAll();
        } else {
            insumos = insumoRepository.findByRubroId(rubroId);
        }
        List<InsumoResponseDTO> insumosDTO = new ArrayList<>();

        for (Insumo insumo : insumos) {
            insumosDTO.add(toDTO(insumo));
        }

        return insumosDTO;
    }

    public void updateInsumo(Long id, InsumoDTO insumoDTO) {
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
        if (!insumo.getPrecioVenta().equals(insumoDTO.getPrecioVenta())) {
            insumo.setPrecioVenta(insumoDTO.getPrecioVenta());
        }
        if (!insumo.getStockActual().equals(insumoDTO.getStockActual())) {
            insumo.setStockActual(insumoDTO.getStockActual());
        }
        if (!insumo.getStockMinimo().equals(insumoDTO.getStockMinimo())) {
            insumo.setStockMinimo(insumoDTO.getStockMinimo());
        }
        if (!insumo.isEsParaElaborar() == insumoDTO.isEsParaElaborar()) {
            insumo.setEsParaElaborar(insumoDTO.isEsParaElaborar());
        }
        if (!insumo.isActivo() == insumoDTO.isActivo()) {
            insumo.setActivo(insumoDTO.isActivo());
        }
        if (!insumo.getUnidadMedida().equals(insumoDTO.getUnidadMedida())) {
            insumo.setUnidadMedida(insumoDTO.getUnidadMedida());
        }
        if (!insumo.getRubro().getId().equals(insumoDTO.getRubroId())) {
            insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo con el id" + insumoDTO.getRubroId() + "no encontrado")));
        }
        insumoRepository.save(insumo);
    }

    public void deleteInsumo(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        insumo.setActivo(false);
        insumoRepository.save(insumo);
    }

    // MAPPERS
    public Insumo toEntity(InsumoDTO insumoDTO) {
        return Insumo.builder()
                .denominacion(insumoDTO.getDenominacion())
                .urlImagen(insumoDTO.getUrlImagen())
                .precioCosto(insumoDTO.getPrecioCosto())
                .precioVenta(insumoDTO.getPrecioVenta())
                .stockActual(insumoDTO.getStockActual())
                .stockMinimo(insumoDTO.getStockMinimo())
                .esParaElaborar(insumoDTO.isEsParaElaborar())
                .activo(insumoDTO.isActivo())
                .unidadMedida(insumoDTO.getUnidadMedida())
                .rubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                        .orElseThrow(() -> new NotFoundException("RubroInsumo con el id" + insumoDTO.getRubroId() + "no encontrado")))
                .build();
    }

    public InsumoResponseDTO toDTO(Insumo insumo) {
        return InsumoResponseDTO.builder()
                .id(insumo.getId())
                .denominacion(insumo.getDenominacion())
                .urlImagen(insumo.getUrlImagen())
                .precioCosto(insumo.getPrecioCosto())
                .precioVenta(insumo.getPrecioVenta())
                .stockActual(insumo.getStockActual())
                .stockMinimo(insumo.getStockMinimo())
                .esParaElaborar(insumo.isEsParaElaborar())
                .activo(insumo.isActivo())
                .unidadMedida(insumo.getUnidadMedida())
                .rubro(rubroInsumoService.toDTO(insumo.getRubro()))
                .build();
    }
}
