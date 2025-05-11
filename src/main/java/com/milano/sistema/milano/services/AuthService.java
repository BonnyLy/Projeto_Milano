package com.milano.sistema.milano.services;

import com.milano.sistema.milano.dto.request.LoginRequest;
import com.milano.sistema.milano.dto.request.RegisterRequest;
import com.milano.sistema.milano.dto.response.AuthResponse;
import com.milano.sistema.milano.exception.BusinessException;
import com.milano.sistema.milano.model.Usuario;
import com.milano.sistema.milano.model.enums.TipoUsuario;
import com.milano.sistema.milano.repositories.UsuarioRepository;
import com.milano.sistema.milano.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Iniciando registro para email: {}", request.getEmail());
        
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            log.error("Email já cadastrado: {}", request.getEmail());
            throw new BusinessException("Este email já está em uso");
        }

        Usuario novoUsuario = Usuario.builder()
                .nome(request.getNome().trim())
                .email(request.getEmail().toLowerCase().trim())
                .senha(passwordEncoder.encode(request.getSenha()))
                .tipo(TipoUsuario.CLIENTE)
                .ativo(true)
                .build();

        try {
            Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
            log.info("Novo usuário registrado com ID: {}", usuarioSalvo.getId());
            
            String jwtToken = jwtService.generateToken(usuarioSalvo);
            
            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
                    
        } catch (Exception e) {
            log.error("Falha ao registrar usuário: {}", e.getMessage());
            throw new BusinessException("Falha no registro: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Iniciando login para email: {}", request.getEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail().toLowerCase().trim(),
                    request.getSenha()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            Usuario usuario = (Usuario) authentication.getPrincipal();
            
            if (!usuario.isEnabled()) {
                log.warn("Tentativa de login para conta inativa: {}", request.getEmail());
                throw new BusinessException("Esta conta está desativada");
            }
            
            log.info("Login bem-sucedido para usuário ID: {}", usuario.getId());
            
            String jwtToken = jwtService.generateToken(usuario);
            
            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
                    
        } catch (BadCredentialsException e) {
            log.warn("Credenciais inválidas para email: {}", request.getEmail());
            throw new BusinessException("Email ou senha incorretos");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro durante o login: {}", e.getMessage());
            throw new BusinessException("Falha no processo de login");
        }
    }
}