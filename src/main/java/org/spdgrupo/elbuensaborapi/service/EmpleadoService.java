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
import org.spdgrupo.elbuensaborapi.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    // Dependencias
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioService usuarioService;
    private final DomicilioService domicilioService;
    private final EmpleadoMapper empleadoMapper;

    public void saveEmpleado(EmpleadoDTO empleadoDTO) {
        if (empleadoDTO.getUsuario().getRol() == Rol.CLIENTE) {
            throw new InvalidRolException("No se puede asignar el rol CLIENTE a un empleado");
        }

        Usuario usuario = usuarioService.saveUsuario(empleadoDTO.getUsuario());
        Domicilio domicilio = domicilioService.save(empleadoDTO.getDomicilio());

        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);
        empleado.setUsuario(usuario);
        empleado.setDomicilio(domicilio);

        empleadoRepository.save(empleado);
    }

    public EmpleadoResponseDTO getEmpleadoById(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el " +  id + " no encontrado"));
        return empleadoMapper.toResponseDTO(empleado);
    }

    public List<EmpleadoResponseDTO> getAllEmpleados() {
        return empleadoRepository.findAll().stream()
                .map(empleadoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void updateEmpleado(Long id, EmpleadoDTO empleadoDTO) {
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
    public void deleteEmpleado(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id" + id  + " no encontrado"));
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
    }
}
