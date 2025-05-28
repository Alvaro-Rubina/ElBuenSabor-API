package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.InsumoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoPatchDTO;
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
    // TODO: Falta logica (aca y capaz en el DTO) para manejar que el stockActual no sea nunca menor que el stockMinimo

    // Dependencias
    private final InsumoRepository insumoRepository;
    private final RubroInsumoRepository rubroInsumoRepository;
    private final InsumoMapper insumoMapper;

    public void saveInsumo(InsumoDTO insumoDTO) {
        Insumo insumo = insumoMapper.toEntity(insumoDTO);
        insumo.setRubro(rubroInsumoRepository.findById(insumoDTO.getRubroId())
                .orElseThrow(() -> new NotFoundException("RubroInsumo con el id " + insumoDTO.getRubroId() + " no encontrado")));
        insumoRepository.save(insumo);
    }

    public InsumoResponseDTO getInsumoById(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        return insumoMapper.toResponseDTO(insumo);
    }

    public List<InsumoResponseDTO> getInsumosByDenominacion(String denominacion) {
        return insumoRepository.findByDenominacionContainingIgnoreCase(denominacion).stream()
                .map(insumoMapper::toResponseDTO)
                .toList();
    }

    // Acá busca por rubro, y si no se le pasa parametro (o es 0), busca todos
    public List<InsumoResponseDTO> getAllInsumos(Long rubroId) {
        List<Insumo> insumos = (rubroId == null || rubroId == 0L)
                ? insumoRepository.findAll()
                : insumoRepository.findByRubroId(rubroId);

        return insumos.stream()
                .map(insumoMapper::toResponseDTO)
                .toList();
    }

    public void updateInsumo(Long id, InsumoDTO insumoDTO) {
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

    public void deleteInsumo(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Insumo con el id " + id + " no encontrado"));
        insumo.setActivo(false);
        insumoRepository.save(insumo);
    }

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
}
