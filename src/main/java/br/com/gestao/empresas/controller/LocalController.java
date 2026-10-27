package br.com.gestao.empresas.controller;

import br.com.gestao.empresas.model.Local;
import br.com.gestao.empresas.repository.LocalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/locais")
public class LocalController {

    private final LocalRepository localRepository;

    public LocalController(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("locais", localRepository.findAll());
        return "locais/lista";
    }

    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("local", new Local());
        return "locais/formulario";
    }

    @PostMapping
    public String salvar(@ModelAttribute Local local, RedirectAttributes redirectAttributes) {
        localRepository.save(local);
        redirectAttributes.addFlashAttribute("mensagem", "Local cadastrado com sucesso!");
        return "redirect:/locais";
    }

    @GetMapping("/")
    public String inicio() {
    return "redirect:/locais";
    }
}
