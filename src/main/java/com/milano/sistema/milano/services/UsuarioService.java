package com.milano.sistema.milano.services;

import com.milano.sistema.milano.exception.ResourceNotFoundException;
import com.milano.sistema.milano.model.Usuario;
import com.milano.sistema.milano.model.enums.TipoUsuario;
import com.milano.sistema.milano.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario criarUsuario(Usuario usuario) {
        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setAtivo(true);
        
        // Define CLIENTE como padrão se não informado
        if(usuario.getTipo() == null) {
            usuario.setTipo(TipoUsuario.CLIENTE);
        }
        
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuariosAtivos() {
        return usuarioRepository.findByAtivoTrue();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }
    
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com email: " + email));
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioAtualizado.getNome());
                    usuario.setEmail(usuarioAtualizado.getEmail());
                    
                    // Só atualiza senha se for fornecida
                    if(usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                        usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
                    }
                    
                    // Atualiza tipo se fornecido
                    if(usuarioAtualizado.getTipo() != null) {
                        usuario.setTipo(usuarioAtualizado.getTipo());
                    }
                    
                    usuario.setDataAtualizacao(LocalDateTime.now());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public void desativarUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setAtivo(false);
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
    
    public void ativarUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setAtivo(true);
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
    
    public boolean existeUsuarioComEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}