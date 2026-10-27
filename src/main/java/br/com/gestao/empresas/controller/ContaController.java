package br.com.gestao.empresas.controller;

import br.com.gestao.empresas.model.Conta;
import br.com.gestao.empresas.repository.ContaRepository;
import br.com.gestao.empresas.repository.LocalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("contas", contaRepository.findAll());
        return "contas/lista";
    }

    @GetMapping("/nova")
    public String formulario(Model model) {
        model.addAttribute("conta", new Conta());
        model.addAttribute("locais", localRepository.findAll());
        return "contas/formulario";
    }

    @PostMapping
    public String salvar(
            @ModelAttribute Conta conta,
            RedirectAttributes redirectAttributes) {

        Long localId = conta.getLocal().getId();

        var local = localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Local não encontrado"));

        conta.setLocal(local);

        if (conta.getStatus() == null || conta.getStatus().isBlank()) {
            conta.setStatus("PENDENTE");
        }

        contaRepository.save(conta);

        redirectAttributes.addFlashAttribute(
                "mensagem", "Conta cadastrada com sucesso!");

        return "redirect:/contas";
    }

    @PostMapping("/{id}/pagar")
    public String marcarComoPaga(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Conta não encontrada"));

        if (!"PAGA".equals(conta.getStatus())) {
            conta.setStatus("PAGA");
            conta.setDataPagamento(LocalDate.now());

            contaRepository.save(conta);
        }

        redirectAttributes.addFlashAttribute(
                "mensagem", "Pagamento registrado com sucesso!");

        return "redirect:/contas";
    }
}