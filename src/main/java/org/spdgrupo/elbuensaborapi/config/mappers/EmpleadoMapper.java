package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, DomicilioMapper.class})
public interface EmpleadoMapper extends GenericoMapper<Empleado, EmpleadoDTO, EmpleadoResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(source = "usuario", target = "usuario")
    @Mapping(source = "domicilio", target = "domicilio")
    Empleado toEntity(EmpleadoDTO empleadoDTO);

    @Mapping(source = "usuario", target = "usuario")
    @Mapping(source = "domicilio", target = "domicilio")
    EmpleadoResponseDTO toResponseDTO(Empleado empleado);
}
