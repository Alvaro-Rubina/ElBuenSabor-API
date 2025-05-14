package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.InvalidRolException;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoPatchDTO;
import org.spdgrupo.elbuensaborapi.model.dto.empleado.EmpleadoResponseDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;
import org.spdgrupo.elbuensaborapi.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    // Dependencias
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioService usuarioService;
    private final DomicilioService domicilioService;

    public void saveEmpleado(EmpleadoDTO empleadoDTO) {
        Empleado empleado = toEntity(empleadoDTO);

        if (empleado.getUsuario().getRol() == Rol.CLIENTE) {
            throw new InvalidRolException("No se puede asignar el rol CLIENTE a un empleado");
        }
        empleadoRepository.save(empleado);
    }

    public EmpleadoResponseDTO getEmpleadoById(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el " +  id + " no encontrado"));
        return toDTO(empleado);
    }

    public List<EmpleadoResponseDTO> getAllEmpleados() {
        List<Empleado> empleados = empleadoRepository.findAll();
        List<EmpleadoResponseDTO> empleadosDTO = new ArrayList<>();
        for (Empleado empleado : empleados) {
            empleadosDTO.add(toDTO(empleado));
        }
        return empleadosDTO;
    }

    public void updateEmpleado(Long id, EmpleadoPatchDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el " +  id + " no encontrado"));

        if (empleadoDTO.getNombreCompleto() != null) {
            empleado.setNombreCompleto(empleadoDTO.getNombreCompleto());
        }

        if (empleadoDTO.getTelefono() != null) {
            empleado.setTelefono(empleadoDTO.getTelefono());
        }

        if (empleadoDTO.getActivo() != null) {
            empleado.setActivo(empleadoDTO.getActivo());
        }

        // actualizo el usuario del empleado
        usuarioService.updateUsuario(empleado.getUsuario().getId(), empleadoDTO.getUsuario());

        // actualizo el domicilio del empleado
        domicilioService.updateDomicilio(empleado.getDomicilio().getId(), empleadoDTO.getDomicilio());

        if (empleado.getUsuario().getRol() == Rol.CLIENTE) {
            throw new InvalidRolException("No se puede asignar el rol CLIENTE a un empleado");
        }

        empleadoRepository.save(empleado);
    }

    public void deleteEmpleado(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el id" + id  + " no encontrado"));
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
    }

    // MAPPERS
    private Empleado toEntity(EmpleadoDTO empleadoDTO) {
        return Empleado.builder()
                .nombreCompleto(empleadoDTO.getNombreCompleto())
                .telefono(empleadoDTO.getTelefono())
                .activo(true)
                .usuario(usuarioService.saveUsuario(empleadoDTO.getUsuario()))
                .domicilio(domicilioService.saveDomicilio(empleadoDTO.getDomicilio()))
                .build();
    }

    public EmpleadoResponseDTO toDTO(Empleado empleado) {
        return EmpleadoResponseDTO.builder()
                .id(empleado.getId())
                .nombreCompleto(empleado.getNombreCompleto())
                .telefono(empleado.getTelefono())
                .activo(empleado.getActivo())
                .usuario(usuarioService.toDTO(empleado.getUsuario()))
                .domicilio(domicilioService.toDTO(empleado.getDomicilio()))
                .build();
    }

}
