package br.com.gestao.empresas.conta.controller;

import br.com.gestao.empresas.conta.dto.ContaFormDTO;
import br.com.gestao.empresas.conta.model.Conta;
import br.com.gestao.empresas.conta.service.ContaService;
import br.com.gestao.empresas.apolice.service.ApoliceService;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.service.LocalService;
import br.com.gestao.empresas.fornecedor.service.FornecedorService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/contas")
public class ContaController {

    private final LocalService localService;
    private final ContaService contaService;
    private final ApoliceService apoliceService;
    private final FornecedorService fornecedorService;

    public ContaController(
            LocalService localService,
            ContaService contaService,
            ApoliceService apoliceService,
            FornecedorService fornecedorService) {

        this.localService = localService;
        this.contaService = contaService;
        this.apoliceService = apoliceService;
        this.fornecedorService = fornecedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("contas", contaService.listarTodas());
        return "contas/lista";
    }

    @GetMapping("/local/{localId}")
    public String listarPorLocal(
            @PathVariable Long localId,
            Model model) {

        Local local = localService.buscarPorId(localId);

        model.addAttribute("contas", contaService.listarPorLocal(localId));
        model.addAttribute("apolices", apoliceService.listarPorLocal(localId));
        model.addAttribute("local", local);

        return "contas/lista";
    }

    @GetMapping("/nova")
    public String formulario(Model model) {

        model.addAttribute("conta", new ContaFormDTO());
        prepararDadosFormulario(model);
        model.addAttribute("acaoFormulario", "/contas");

        return "contas/formulario";
    }

    @PostMapping
    public String salvar(
            @ModelAttribute("conta") ContaFormDTO dadosFormulario,
            RedirectAttributes redirectAttributes) {

        contaService.cadastrar(dadosFormulario);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Conta cadastrada com sucesso!");

        return "redirect:/contas";
    }

    @GetMapping("/{id}/editar")
    public String formularioEdicao(
            @PathVariable Long id,
            Model model) {

        Conta conta = contaService.buscarPorId(id);

        ContaFormDTO formulario = converterParaDTO(conta);

        model.addAttribute("conta", formulario);
        prepararDadosFormulario(model);
        model.addAttribute("acaoFormulario", "/contas/" + id + "/editar");

        return "contas/formulario";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute("conta") ContaFormDTO dadosFormulario,
            RedirectAttributes redirectAttributes) {

        contaService.atualizar(id, dadosFormulario);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Conta atualizada com sucesso!");

        return "redirect:/contas";
    }

    @PostMapping("/{id}/pagar")
    public String marcarComoPaga(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        contaService.marcarComoPaga(id);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Pagamento registrado com sucesso!");

        return "redirect:/contas";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        contaService.excluir(id);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Conta excluída com sucesso!");

        return "redirect:/contas";
    }

    private void prepararDadosFormulario(Model model) {
        model.addAttribute("locais", localService.listarTodos());
        model.addAttribute("fornecedores", fornecedorService.listarTodos());
    }

    private ContaFormDTO converterParaDTO(Conta conta) {

        ContaFormDTO formulario = new ContaFormDTO();

        formulario.setDescricao(conta.getDescricao());
        formulario.setValor(conta.getValor());
        formulario.setDataVencimento(conta.getDataVencimento());
        formulario.setDataPagamento(conta.getDataPagamento());
        formulario.setStatus(conta.getStatus());
        formulario.setObservacoes(conta.getObservacoes());

        if (conta.getLocal() != null) {
            formulario.setLocalId(conta.getLocal().getId());
        }

        if (conta.getFornecedor() != null) {
            formulario.setFornecedorId(conta.getFornecedor().getId());
        }

        return formulario;
    }
}