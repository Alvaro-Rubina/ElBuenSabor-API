package org.spdgrupo.elbuensaborapi.service;

import org.spdgrupo.elbuensaborapi.config.exception.NotFoundException;
import org.spdgrupo.elbuensaborapi.model.dto.EmpleadoDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Empleado;
import org.spdgrupo.elbuensaborapi.repository.DomicilioRepository;
import org.spdgrupo.elbuensaborapi.repository.EmpleadoRepository;
import org.spdgrupo.elbuensaborapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DomicilioService domicilioService;

    @Autowired
    private DomicilioRepository domicilioRepository;

    // MAPPERS
    private Empleado toEntity(EmpleadoDTO empleadoDTO) {
        return Empleado.builder()
                .nombreCompleto(empleadoDTO.getNombreCompleto())
                .telefono(empleadoDTO.getTelefono())
                .activo(empleadoDTO.getActivo())
                .usuario(usuarioRepository.findById(empleadoDTO.getUsuario().getId())
                        .orElseThrow(() -> new NotFoundException("Usuario no encontrado")))
                .domicilio(domicilioRepository.findById(empleadoDTO.getDomicilio().getId())
                        .orElseThrow(() -> new NotFoundException("Domicilio no encontrado")))
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
