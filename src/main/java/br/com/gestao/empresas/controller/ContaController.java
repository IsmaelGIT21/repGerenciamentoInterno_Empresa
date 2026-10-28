
package br.com.gestao.empresas.controller;

import br.com.gestao.empresas.model.Conta;
import br.com.gestao.empresas.model.Local;
import br.com.gestao.empresas.repository.ContaRepository;
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

import java.time.LocalDate;

@Controller
@RequestMapping("/contas")
public class ContaController {

    private final ContaRepository contaRepository;
    private final LocalRepository localRepository;

    public ContaController(
            ContaRepository contaRepository,
            LocalRepository localRepository) {
        this.contaRepository = contaRepository;
        this.localRepository = localRepository;
    }

    // Lista todas as contas
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("contas", contaRepository.findAll());
        return "contas/lista";
    }

    // Lista apenas as contas associadas a um local
    @GetMapping("/local/{localId}")
    public String listarPorLocal(
            @PathVariable Long localId,
            Model model) {

        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Empresa não encontrada"));

        model.addAttribute("contas", contaRepository.findByLocalId(localId));
        model.addAttribute("local", local);

        return "contas/lista";
    }

    // Abre o formulário de cadastro
    @GetMapping("/nova")
    public String formulario(Model model) {
        Conta conta = new Conta();
        conta.setLocal(new Local());

        model.addAttribute("conta", conta);
        model.addAttribute("locais", localRepository.findAll());
        model.addAttribute("acaoFormulario", "/contas");

        return "contas/formulario";
    }

    // Salva uma nova conta
    @PostMapping
    public String salvar(
            @ModelAttribute Conta conta,
            RedirectAttributes redirectAttributes) {

        if (conta.getLocal() == null || conta.getLocal().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione um local para a conta");
        }

        Long localId = conta.getLocal().getId();

        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));

        conta.setLocal(local);

        if (conta.getStatus() == null || conta.getStatus().isBlank()) {
            conta.setStatus("PENDENTE");
        }

        if ("PAGA".equals(conta.getStatus())) {
            if (conta.getDataPagamento() == null) {
                conta.setDataPagamento(LocalDate.now());
            }
        } else {
            conta.setDataPagamento(null);
        }

        contaRepository.save(conta);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Conta cadastrada com sucesso!");

        return "redirect:/contas";
    }

    // Abre o formulário de edição
    @GetMapping("/{id}/editar")
    public String formularioEdicao(
            @PathVariable Long id,
            Model model) {

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conta não encontrada"));

        model.addAttribute("conta", conta);
        model.addAttribute("locais", localRepository.findAll());
        model.addAttribute(
                "acaoFormulario",
                "/contas/" + id + "/editar");

        return "contas/formulario";
    }

    // Salva as alterações de uma conta
    @PostMapping("/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @ModelAttribute Conta dadosFormulario,
            RedirectAttributes redirectAttributes) {

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conta não encontrada"));

        if (dadosFormulario.getLocal() == null
                || dadosFormulario.getLocal().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione um local para a conta");
        }

        Long localId = dadosFormulario.getLocal().getId();

        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));

        conta.setDescricao(dadosFormulario.getDescricao());
        conta.setValor(dadosFormulario.getValor());
        conta.setDataVencimento(dadosFormulario.getDataVencimento());
        conta.setObservacoes(dadosFormulario.getObservacoes());
        conta.setLocal(local);

        String status = dadosFormulario.getStatus();

        if (status == null || status.isBlank()) {
            status = "PENDENTE";
        }

        conta.setStatus(status);

        if ("PAGA".equals(status)) {
            if (dadosFormulario.getDataPagamento() != null) {
                conta.setDataPagamento(
                        dadosFormulario.getDataPagamento());
            } else if (conta.getDataPagamento() == null) {
                conta.setDataPagamento(LocalDate.now());
            }
        } else {
            conta.setDataPagamento(null);
        }

        contaRepository.save(conta);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Conta atualizada com sucesso!");

        return "redirect:/contas";
    }

    // Marca uma conta como paga
    @PostMapping("/{id}/pagar")
    public String marcarComoPaga(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conta não encontrada"));

        if (!"PAGA".equals(conta.getStatus())) {
            conta.setStatus("PAGA");
            conta.setDataPagamento(LocalDate.now());
            contaRepository.save(conta);
        }

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Pagamento registrado com sucesso!");

        return "redirect:/contas";
    }

    // Exclui uma conta
    @PostMapping("/{id}/excluir")
    public String excluir(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conta não encontrada"));

        contaRepository.delete(conta);

        redirectAttributes.addFlashAttribute(
                "mensagem",
                "Conta excluída com sucesso!");

        return "redirect:/contas";
    }
}