package br.com.gestao.empresas.usuario.controller;

import br.com.gestao.empresas.usuario.model.Usuario;
import br.com.gestao.empresas.usuario.model.UsuarioCadastroForm;
import br.com.gestao.empresas.usuario.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new UsuarioCadastroForm());
        return "usuarios/formulario";
    }

    @PostMapping("/usuarios")
    public String cadastrarUsuario(
            @ModelAttribute("usuario") UsuarioCadastroForm formulario,
            Model model,
            RedirectAttributes redirectAttributes) {

        String username = formulario.getUsername() == null
                ? ""
                : formulario.getUsername().trim();

        String password = formulario.getPassword();

        if (username.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("erro", "Preencha todos os campos.");
            return "usuarios/formulario";
        }

        if (password.length() < 8) {
            model.addAttribute(
                    "erro", "A senha deve ter pelo menos 8 caracteres.");
            return "usuarios/formulario";
        }

        if (usuarioRepository.findByUsername(username).isPresent()) {
            model.addAttribute(
                    "erro", "Esse nome de usuário já está cadastrado.");
            return "usuarios/formulario";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        redirectAttributes.addFlashAttribute(
                "mensagem", "Usuário cadastrado com sucesso!");

        return "redirect:/locais";
    }
}
