package br.com.gestao.empresas.fornecedor.controller;

import br.com.gestao.empresas.fornecedor.dto.FornecedorFormDTO;
import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.service.FornecedorService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("fornecedores", fornecedorService.listarTodos());
        return "fornecedores/lista";
    }

    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("fornecedor", new FornecedorFormDTO());
        model.addAttribute("acaoFormulario", "/fornecedores");
        return "fornecedores/formulario";
    }

    @PostMapping
    public String salvar(
            @ModelAttribute("fornecedor") FornecedorFormDTO dadosFormulario,
            RedirectAttributes redirectAttributes) {

        try {
            fornecedorService.cadastrar(dadosFormulario);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Fornecedor cadastrado com sucesso!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/fornecedores/novo";
        }

        return "redirect:/fornecedores";
    }

    @GetMapping("/{id}/editar")
    public String formularioEdicao(
            @PathVariable Long id,
            Model model) {

        Fornecedor fornecedor = fornecedorService.buscarPorId(id);

        FornecedorFormDTO formulario = new FornecedorFormDTO();
        formulario.setNome(fornecedor.getNome());

        model.addAttribute("fornecedor", formulario);
        model.addAttribute(
                "acaoFormulario",
                "/fornecedores/" + id + "/editar");

        return "fornecedores/formulario";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute("fornecedor") FornecedorFormDTO dadosFormulario,
            RedirectAttributes redirectAttributes) {

        try {
            fornecedorService.atualizar(id, dadosFormulario);

            redirectAttributes.addFlashAttribute(
                    "mensagem",
                    "Fornecedor atualizado com sucesso!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/fornecedores/" + id + "/editar";
        }

        return "redirect:/fornecedores";
    }

    @PostMapping("/{id}/status")
    public String alterarStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        fornecedorService.alterarStatus(id);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Status do fornecedor alterado com sucesso!");

        return "redirect:/fornecedores";
    }
}