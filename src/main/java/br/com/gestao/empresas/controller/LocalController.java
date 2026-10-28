package br.com.gestao.empresas.controller;

import br.com.gestao.empresas.model.Local;
import br.com.gestao.empresas.repository.LocalRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("acaoFormulario", "/locais");
        return "locais/formulario";
    }

    @PostMapping
    public String salvar(
            @ModelAttribute Local local,
            RedirectAttributes redirectAttributes) {

        localRepository.save(local);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Local cadastrado com sucesso!");

        return "redirect:/locais";
    }

    @GetMapping("/{id}/editar")
    public String formularioEdicao(
            @PathVariable Long id,
            Model model) {

        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));

        model.addAttribute("local", local);
        model.addAttribute(
                "acaoFormulario",
                "/locais/" + id + "/editar");

        return "locais/formulario";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Local dadosFormulario,
            RedirectAttributes redirectAttributes) {

        Local local = localRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));

        local.setNome(dadosFormulario.getNome());
        local.setTipo(dadosFormulario.getTipo());
        local.setEndereco(dadosFormulario.getEndereco());

        localRepository.save(local);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Local atualizado com sucesso!");

        return "redirect:/locais";
    }
}
