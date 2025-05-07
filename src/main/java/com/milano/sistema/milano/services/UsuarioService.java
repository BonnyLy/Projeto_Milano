package com.milano.sistema.milano.services;

import com.milano.sistema.milano.model.Usuario;
import com.milano.sistema.milano.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criarUsuario(Usuario usuario) {
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuariosAtivos() {
        return usuarioRepository.findByAtivoTrue();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setEmail(usuarioAtualizado.getEmail());
                    usuario.setSenha(usuarioAtualizado.getSenha());
                    usuario.setTipo(usuarioAtualizado.getTipo());
                    return usuarioRepository.save(usuario);
                })
                .orElseGet(() -> {
                    usuarioAtualizado.setId(id);
                    return usuarioRepository.save(usuarioAtualizado);
                });
    }

    public void desativarUsuario(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
        });
    }
}