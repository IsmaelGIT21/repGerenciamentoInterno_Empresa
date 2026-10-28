package br.com.gestao.empresas.config;

import br.com.gestao.empresas.model.Usuario;
import br.com.gestao.empresas.repository.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner criarAdministrador(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsername("admin");
                usuario.setPassword(
                        passwordEncoder.encode("SistemaEmpresas2026"));
                usuario.setAtivo(true);

                usuarioRepository.save(usuario);

                System.out.println(
                        "Usuário administrador inicial criado.");
            }
        };
    }
}