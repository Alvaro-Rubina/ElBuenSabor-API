package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;
import org.spdgrupo.elbuensaborapi.repository.InsumoRepository;
import org.spdgrupo.elbuensaborapi.repository.RubroInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private RubroInsumoRepository rubroInsumoRepository;

    @Autowired
    private RubroInsumoService rubroInsumoService;

    public void saveInsumo(InsumoDTO insumoDTO) {
        Insumo insumo = toEntity(insumoDTO);
        insumoRepository.save(insumo);
    }

    public InsumoDTO getInsumo(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        return toDTO(insumo);
    }

    public List<InsumoDTO> getAllInsumos() {
        List<Insumo> insumos = insumoRepository.findAll();
        List<InsumoDTO> insumosDTO = new ArrayList<>();

        for (Insumo insumo : insumos) {
            insumosDTO.add(toDTO(insumo));
        }

        return insumosDTO;
    }

    public void editInsumo(Long id, InsumoDTO insumoDTO) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));

        if (!insumo.getDenominacion().equals(insumoDTO.getDenominacion())) {
            insumo.setDenominacion(insumoDTO.getDenominacion());
        }

        if (!insumo.getUrlImagen().equals(insumoDTO.getUrlImagen())) {
            insumo.setUrlImagen(insumoDTO.getUrlImagen());
        }

        if (!insumo.getPrecioCompra().equals(insumoDTO.getPrecioCompra())) {
            insumo.setPrecioCompra(insumoDTO.getPrecioCompra());
        }

        if (!insumo.getPrecioVenta().equals(insumoDTO.getPrecioVenta())) {
            insumo.setPrecioVenta(insumoDTO.getPrecioVenta());
        }

        if (insumo.getStockActual().equals(insumoDTO.getStockActual())) {
            insumo.setStockActual(insumoDTO.getStockActual());
        }

        if (insumo.getStockMinimo().equals(insumoDTO.getStockMinimo())) {
            insumo.setStockMinimo(insumoDTO.getStockMinimo());
        }

        if (insumo.isEsParaElaborar() != insumoDTO.isEsParaElaborar()) {
            insumo.setEsParaElaborar(insumoDTO.isEsParaElaborar());
        }

        if (insumo.isActivo() != insumoDTO.isActivo()) {
            insumo.setActivo(insumoDTO.isActivo());
        }

        if (!insumo.getUnidadMedida().equals(insumoDTO.getUnidadMedida())) {
            insumo.setUnidadMedida(insumoDTO.getUnidadMedida());
        }

        if (!insumo.getRubro().getId().equals(insumoDTO.getRubro().getId())) {
            insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubro().getId())
                    .orElseThrow(() -> new NotFoundException("RubroInsumo con el id" + insumoDTO.getRubro().getId() + "no encontrado")));
        }

        insumoRepository.save(insumo);
    }

    // MAPPERS
    public Insumo toEntity(InsumoDTO insumoDTO) {
        return Insumo.builder()
                .denominacion(insumoDTO.getDenominacion())
                .urlImagen(insumoDTO.getUrlImagen())
                .precioCompra(insumoDTO.getPrecioCompra())
                .precioVenta(insumoDTO.getPrecioVenta())
                .stockActual(insumoDTO.getStockActual())
                .stockMinimo(insumoDTO.getStockMinimo())
                .esParaElaborar(insumoDTO.isEsParaElaborar())
                .activo(insumoDTO.isActivo())
                .unidadMedida(insumoDTO.getUnidadMedida())
                .rubro(rubroInsumoRepository.findById(insumoDTO.getRubro().getId())
                        .orElseThrow(() -> new NotFoundException("RubroInsumo con el id" + insumoDTO.getRubro().getId() + "no encontrado")))
                .build();
    }

    public InsumoDTO toDTO(Insumo insumo) {
        return InsumoDTO.builder()
                .denominacion(insumo.getDenominacion())
                .urlImagen(insumo.getUrlImagen())
                .precioCompra(insumo.getPrecioCompra())
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
