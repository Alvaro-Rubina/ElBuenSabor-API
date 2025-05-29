package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.ClienteMapper;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteDTO;
import org.spdgrupo.elbuensaborapi.model.dto.cliente.ClienteResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Cliente;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ClienteService extends GenericoServiceImpl<Cliente, ClienteDTO, ClienteResponseDTO, Long> {

    // Dependencias
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DetalleDomicilioService detalleDomicilioService;
    @Autowired
    private ClienteMapper clienteMapper;

    public ClienteService(GenericoRepository<Cliente, Long> genericoRepository, GenericoMapper<Cliente, ClienteDTO, ClienteResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public Cliente save(ClienteDTO clienteDTO) {
        clienteDTO.getUsuario().setRol(Rol.CLIENTE);
        Usuario usuario = usuarioService.saveUsuario(clienteDTO.getUsuario());

        Cliente cliente = clienteMapper.toEntity(clienteDTO);

        cliente.setUsuario(usuario);

        return (clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void update(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id " + id + " no encontrado"));

        cliente.setNombreCompleto(clienteDTO.getNombreCompleto());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setActivo(clienteDTO.getActivo());

        clienteDTO.getUsuario().setRol(Rol.CLIENTE);
        usuarioService.updateUsuario(cliente.getUsuario().getId(), clienteDTO.getUsuario());

        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente con el id" + id  + " no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
}
