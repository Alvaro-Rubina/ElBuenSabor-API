package org.spdgrupo.elbuensaborapi.config.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;

@Mapper(componentModel = "spring", uses = {UsuarioMapper.class, DetalleDomicilioMapper.class})
public interface ClienteMapper extends GenericoMapper<Cliente, ClienteDTO, ClienteResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(source = "usuario", target = "usuario")
    @Mapping(target = "detalleDomicilios", ignore = true)
    Cliente toEntity(ClienteDTO clienteDTO);

    @Mapping(source = "usuario", target = "usuario")
    @Mapping(source = "detalleDomicilios", target = "detalleDomicilios")
    ClienteResponseDTO toResponseDTO(Cliente cliente);
}
