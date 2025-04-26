package org.spdgrupo.elbuensaborapi.controller;

import lombok.RequiredArgsConstructor;
import org.spdgrupo.elbuensaborapi.model.dto.UsuarioDTO;
import org.spdgrupo.elbuensaborapi.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.saveUsuario(usuarioDTO);
        return ResponseEntity.ok("Usuario guardado exitosamente");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUsuario(@PathVariable Long id,
                                                @RequestBody UsuarioDTO usuarioDTO ) {
        usuarioService.updateUsuario(id, usuarioDTO);
        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }



}
