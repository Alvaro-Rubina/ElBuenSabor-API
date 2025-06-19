package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.spdgrupo.elbuensaborapi.model.dto.rol.RolDTO;
import org.spdgrupo.elbuensaborapi.model.dto.rol.RolResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Rol;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring")
public interface RolMapper extends GenericoMapper<Rol, RolDTO, RolResponseDTO> {

    Rol toEntity(RolDTO rolDTO);
    RolResponseDTO toResponseDTO(Rol rol);
}
