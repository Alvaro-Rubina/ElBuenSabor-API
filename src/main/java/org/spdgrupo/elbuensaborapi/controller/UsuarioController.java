package org.spdgrupo.elbuensaborapi.controller;

import org.spdgrupo.elbuensaborapi.model.dto.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.model.entity.Usuario;
import org.spdgrupo.elbuensaborapi.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(Long id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUsuario(UsuarioDTO usuarioDTO) {
        usuarioService.saveUsuario(usuarioDTO);
        return ResponseEntity.ok("Usuario guardado exitosamente");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUsuario(UsuarioDTO usuarioDTO, Long id) {
        usuarioService.updateUsuario(usuarioDTO, id);
        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }



}
