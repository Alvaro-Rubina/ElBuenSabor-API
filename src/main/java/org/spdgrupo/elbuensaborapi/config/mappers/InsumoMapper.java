package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.insumo.InsumoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Insumo;

@Mapper(componentModel = "spring", uses = {RubroInsumoMapper.class})
public interface InsumoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "rubroId", target = "rubro.id")
    Insumo toEntity(InsumoDTO insumoDTO);

    InsumoResponseDTO toResponseDTO(Insumo insumo);
}