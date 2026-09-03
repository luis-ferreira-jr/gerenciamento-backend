package com.gerenciamento.usuario;

import com.gerenciamento.usuario.dto.UsuarioRequestDTO;
import com.gerenciamento.usuario.dto.UsuarioResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUser(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario criado = usuarioService.createUser(dto);
        return new ResponseEntity<>(new UsuarioResponseDTO(criado), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsers() {
        List<UsuarioResponseDTO> usuarios = usuarioService.getAllUsers().stream()
                .map(UsuarioResponseDTO::new)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(new UsuarioResponseDTO(usuarioService.getUserById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(new UsuarioResponseDTO(usuarioService.updateUser(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        usuarioService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
