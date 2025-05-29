package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.InvalidRolException;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.config.mappers.EmpleadoMapper;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Domicilio;
import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoMapper;
import org.spdgrupo.elbuensaborapi.model.interfaces.GenericoRepository;
import org.spdgrupo.elbuensaborapi.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService extends GenericoServiceImpl<Empleado, EmpleadoDTO, EmpleadoResponseDTO, Long> {

    // Dependencias
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DomicilioService domicilioService;
    @Autowired
    private EmpleadoMapper empleadoMapper;

    public EmpleadoService(GenericoRepository<Empleado, Long> genericoRepository, GenericoMapper<Empleado, EmpleadoDTO, EmpleadoResponseDTO> genericoMapper) {
        super(genericoRepository, genericoMapper);
    }

    @Override
    @Transactional
    public Empleado save(EmpleadoDTO empleadoDTO) {
        if (empleadoDTO.getUsuario().getRol() == Rol.CLIENTE) {
            throw new InvalidRolException("No se puede asignar el rol CLIENTE a un empleado");
        }

        Usuario usuario = usuarioService.saveUsuario(empleadoDTO.getUsuario());
        Domicilio domicilio = domicilioService.save(empleadoDTO.getDomicilio());

        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);
        empleado.setUsuario(usuario);
        empleado.setDomicilio(domicilio);

        return (empleadoRepository.save(empleado));
    }

    @Override
    @Transactional
    public void update(Long id, EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el " + id + " no encontrado"));

        if (empleadoDTO.getUsuario().getRol() == Rol.CLIENTE) {
            throw new InvalidRolException("No se puede asignar el rol CLIENTE a un empleado");
        }

        empleado.setNombreCompleto(empleadoDTO.getNombreCompleto());
        empleado.setTelefono(empleadoDTO.getTelefono());

        usuarioService.updateUsuario(empleado.getUsuario().getId(), empleadoDTO.getUsuario());
        domicilioService.update(empleado.getDomicilio().getId(), empleadoDTO.getDomicilio());

        empleadoRepository.save(empleado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id" + id  + " no encontrado"));
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
    }
}
