package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.config.mappers.utils.CycleAvoidingMappingContext;
import org.spdgrupo.elbuensaborapi.config.mappers.utils.DoIgnore;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rubroinsumo.RubroInsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.RubroInsumo;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RubroInsumoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subRubros", ignore = true)
    @Mapping(target = "rubroPadre", ignore = true)
    RubroInsumo toEntity(RubroInsumoDTO rubroInsumoDTO);

    @Mapping(source = "rubroPadre.id", target = "rubroPadreId")
    @Mapping(source = "subRubros", target = "subRubros")
    RubroInsumoResponseDTO toResponseDTO(RubroInsumo rubroInsumo, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default RubroInsumoResponseDTO toResponseDTO(RubroInsumo rubroInsumo) {
        return toResponseDTO(rubroInsumo, new CycleAvoidingMappingContext());
    }
}