package br.com.gestao.empresas.apolice.controller;

import br.com.gestao.empresas.apolice.model.Apolice;
import br.com.gestao.empresas.apolice.service.ApoliceService;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;

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
@RequestMapping("/apolices")
public class ApoliceController {

    private final LocalRepository localRepository;
    private final ApoliceService apoliceService;

    public ApoliceController(
            LocalRepository localRepository,
            ApoliceService apoliceService) {

        this.localRepository = localRepository;
        this.apoliceService = apoliceService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("apolices", apoliceService.listarTodas());
        return "apolices/lista";
    }

    @GetMapping("/local/{localId}")
    public String listarPorLocal(
            @PathVariable Long localId,
            Model model) {

        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));

        model.addAttribute(
                "apolices",
                apoliceService.listarPorLocal(localId));

        model.addAttribute("local", local);

        return "apolices/lista";
    }

    // Abre o formulário de cadastro
    @GetMapping("/nova")
    public String formulario(Model model) {

        Apolice apolice = new Apolice();
        apolice.setLocal(new Local());

        model.addAttribute("apolice", apolice);
        model.addAttribute("locais", localRepository.findAll());
        model.addAttribute("acaoFormulario", "/apolices");

        return "apolices/formulario";
    }

    // Salva uma nova apólice
    @PostMapping
    public String salvar(
            @ModelAttribute Apolice apolice,
            RedirectAttributes redirectAttributes) {

        apoliceService.cadastrar(apolice);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Apólice cadastrada com sucesso!");

        return "redirect:/apolices";
    }

    // Abre o formulário de edição
    @GetMapping("/{id}/editar")
    public String formularioEdicao(
            @PathVariable Long id,
            Model model) {

        Apolice apolice = apoliceService.buscarPorId(id);
	System.out.println("Data início: " + apolice.getDataInicio());
System.out.println("Data vencimento: " + apolice.getDataVencimento());
       model.addAttribute("apolice", apolice);
        model.addAttribute("locais", localRepository.findAll());
        model.addAttribute(
                "acaoFormulario",
                "/apolices/" + id + "/editar");

        return "apolices/formulario";
    }

    // Salva as alterações de uma apólice
    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Apolice dadosFormulario,
            RedirectAttributes redirectAttributes) {

        apoliceService.atualizar(id, dadosFormulario);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Apólice atualizada com sucesso!");

        return "redirect:/apolices";
    }

    // Exclui uma apólice
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        apoliceService.excluir(id);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Apólice excluída com sucesso!");

        return "redirect:/apolices";
    }
}
