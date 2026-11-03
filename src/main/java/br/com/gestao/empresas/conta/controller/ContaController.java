package br.com.gestao.empresas.conta.controller;

import br.com.gestao.empresas.conta.model.Conta;
import br.com.gestao.empresas.conta.service.ContaService;
import br.com.gestao.empresas.apolice.service.ApoliceService;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import br.com.gestao.empresas.fornecedor.repository.FornecedorRepository;
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
@RequestMapping("/contas")
public class ContaController {

        private final LocalRepository localRepository;
        private final ContaService contaService;
        private final ApoliceService apoliceService;
        private final FornecedorRepository fornecedorRepository;

        public ContaController(
                        LocalRepository localRepository,
                        ContaService contaService,
                        ApoliceService apoliceService,
                        FornecedorRepository fornecedorRepository) {

                this.localRepository = localRepository;
                this.contaService = contaService;
                this.apoliceService = apoliceService;
                this.fornecedorRepository = fornecedorRepository;
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

                Local local = localRepository.findById(localId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Empresa não encontrada"));

                model.addAttribute(
                                "contas",
                                contaService.listarPorLocal(localId));

                model.addAttribute(
                                "apolices",
                                apoliceService.listarPorLocal(localId));

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
                model.addAttribute("fornecedores", fornecedorRepository.findAll());

                return "contas/formulario";
        }

        @PostMapping
        public String salvar(
                        @ModelAttribute Conta conta,
                        RedirectAttributes redirectAttributes) {

                contaService.cadastrar(conta);

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

                model.addAttribute("conta", conta);
                model.addAttribute("locais", localRepository.findAll());
                model.addAttribute("acaoFormulario", "/contas/" + id + "/editar");
                model.addAttribute("fornecedores", fornecedorRepository.findAll());

                return "contas/formulario";
        }

        // Salva as alterações de uma conta
        @PostMapping("/{id}/editar")
        public String atualizar(
                        @PathVariable Long id,
                        @ModelAttribute Conta dadosFormulario,
                        RedirectAttributes redirectAttributes) {
                System.out.println("ID da conta: " + id);

                System.out.println("Fornecedor recebido: "
                                + (dadosFormulario.getFornecedor() == null
                                                ? "null"
                                                : dadosFormulario.getFornecedor().getId()));
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

        // Exclui uma conta
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
}
