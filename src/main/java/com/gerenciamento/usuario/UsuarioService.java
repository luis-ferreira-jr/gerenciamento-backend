package com.gerenciamento.usuario;

import com.gerenciamento.common.RecursoNaoEncontradoException;
import com.gerenciamento.common.RegraDeNegocioException;
import com.gerenciamento.usuario.dto.UsuarioRequestDTO;
import com.gerenciamento.usuario.dto.UsuarioUpdateDTO;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario createUser(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RegraDeNegocioException("Email já está em uso");
        }
        if (usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new RegraDeNegocioException("CPF já está em uso");
        }

        Usuario usuario = new Usuario(dto.getNome(), dto.getEmail(), dto.getCpf(), passwordEncoder.encode(dto.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    public Usuario updateUser(Long id, UsuarioUpdateDTO dto) {
        Usuario existente = getUserById(id);

        if (!existente.getEmail().equals(dto.getEmail()) &&
            usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new RegraDeNegocioException("Email já está em uso");
        }

        if (!existente.getCpf().equals(dto.getCpf()) &&
            usuarioRepository.existsByCpfAndIdNot(dto.getCpf(), id)) {
            throw new RegraDeNegocioException("CPF já está em uso");
        }

        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setCpf(dto.getCpf());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return usuarioRepository.save(existente);
    }

    public void deleteUser(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
