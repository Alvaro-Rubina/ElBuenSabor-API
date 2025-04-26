package org.spdgrupo.elbuensaborapi.service;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.config.exception.InvalidRolException;
import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.model.enums.Rol;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.EmpleadoRepository;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoService {  // TODO: HACER EL EmpleadoController

    // Dependencias
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final DomicilioService domicilioService;
    private final DomicilioRepository domicilioRepository;

    public void saveEmpleado(EmpleadoDTO empleadoDTO) {
        Empleado empleado = toEntity(empleadoDTO);

        if (empleado.getUsuario().getRol() == Rol.CLIENTE) {
            throw new InvalidRolException("No se puede asignar el rol CLIENTE a un empleado");
        }
        empleadoRepository.save(empleado);
    }

    public EmpleadoDTO getEmpleadoById(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el " +  id + " no encontrado"));
        return toDTO(empleado);
    }

    public List<EmpleadoDTO> getAllEmpleados() {
        List<Empleado> empleados = empleadoRepository.findAll();
        List<EmpleadoDTO> empleadosDTO = new ArrayList<>();
        for (Empleado empleado : empleados) {
            empleadosDTO.add(toDTO(empleado));
        }
        return empleadosDTO;
    }

    public void updateEmpleado(Long id, EmpleadoDTO empleadoDTO) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empleado con el " +  id + " no encontrado"));

        if (!empleado.getNombreCompleto().equals(empleadoDTO.getNombreCompleto())) {
            empleado.setNombreCompleto(empleadoDTO.getNombreCompleto());
        }

        if (!empleado.getTelefono().equals(empleadoDTO.getTelefono())) {
            empleado.setTelefono(empleadoDTO.getTelefono());
        }

        if (!empleado.getActivo().equals(empleadoDTO.getActivo())) {
            empleado.setActivo(empleadoDTO.getActivo());
        }

        // actualizo el domicilio del empleado
        domicilioService.updateDomicilio(empleado.getDomicilio().getId(), empleadoDTO.getDomicilio());

        // actualizo el usuario del empleado
        usuarioService.updateUsuario(empleado.getUsuario().getId(), empleadoDTO.getUsuario());

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

    public EmpleadoDTO toDTO(Empleado empleado) {
        return EmpleadoDTO.builder()
                .id(empleado.getId())
                .nombreCompleto(empleado.getNombreCompleto())
                .telefono(empleado.getTelefono())
                .activo(empleado.getActivo())
                .usuario(usuarioService.toDto(empleado.getUsuario()))
                .domicilio(domicilioService.toDto(empleado.getDomicilio()))
                .build();
    }

}
