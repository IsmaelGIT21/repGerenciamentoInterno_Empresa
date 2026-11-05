package br.com.gestao.empresas.local.controller;

import br.com.gestao.empresas.local.dto.LocalFormDTO;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.service.LocalService;
import br.com.gestao.empresas.apolice.service.ApoliceService;
import br.com.gestao.empresas.conta.service.ContaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/locais")
public class LocalController {
        private static final Logger logger = LoggerFactory.getLogger(ApoliceService.class);
        private final LocalService localService;
        private final ContaService contaService;

        public LocalController(
                        LocalService localService,
                        ContaService contaService) {

                this.localService = localService;
                this.contaService = contaService;
        }

        @GetMapping
        public String listar(Model model) {
                model.addAttribute("locais", localService.listarTodos());
                return "locais/lista";
        }

        @GetMapping("/{id}")
        public String detalhes(
                        @PathVariable Long id,
                        Model model) {

                Local local = localService.buscarPorId(id);

                model.addAttribute("local", local);
                model.addAttribute("contas", contaService.listarPorLocal(id));

                return "locais/detalhes";
        }

        @GetMapping("/novo")
        public String formulario(Model model) {

                model.addAttribute("local", new LocalFormDTO());
                model.addAttribute("acaoFormulario", "/locais");

                return "locais/formulario";
        }

        @PostMapping
        public String salvar(
                        @Valid @ModelAttribute("local") LocalFormDTO dadosFormulario,
                        BindingResult resultado,
                        Model model,
                        RedirectAttributes redirectAttributes) {

                if (resultado.hasErrors()) {
                        model.addAttribute("acaoFormulario", "/locais");
                        model.addAttribute("erro", "Verifique os campos preenchidos.");
                        return "locais/formulario";
                }

                localService.cadastrar(dadosFormulario);

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Local cadastrado com sucesso!");
                logger.info("Local cadastrado com sucesso: {}", dadosFormulario.getNome());
                return "redirect:/locais";
        }

        @GetMapping("/{id}/editar")
        public String formularioEdicao(
                        @PathVariable Long id,
                        Model model) {

                Local local = localService.buscarPorId(id);

                model.addAttribute("local", converterParaDTO(local));
                model.addAttribute(
                                "acaoFormulario",
                                "/locais/" + id + "/editar");

                return "locais/formulario";
        }

        @PostMapping("/{id}/editar")
        public String atualizar(
                        @PathVariable Long id,
                        @Valid @ModelAttribute("local") LocalFormDTO dadosFormulario,
                        BindingResult resultado,
                        Model model,
                        RedirectAttributes redirectAttributes) {

                if (resultado.hasErrors()) {
                        model.addAttribute("acaoFormulario", "/locais/" + id + "/editar");
                        model.addAttribute("erro", "Verifique os campos preenchidos.");
                        return "locais/formulario";
                }

                localService.atualizar(id, dadosFormulario);

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Local atualizado com sucesso!");
                logger.info("Local atualizado com sucesso: {}", dadosFormulario.getNome());
                return "redirect:/locais";
        }

        @PostMapping("/{id}/excluir")
        public String excluir(
                        @PathVariable Long id,
                        RedirectAttributes redirectAttributes) {

                boolean excluido = localService.excluir(id);

                if (!excluido) {
                        redirectAttributes.addFlashAttribute(
                                        "mensagem",
                                        "Não é possível excluir este local porque existem contas ou apólices vinculadas.");

                        return "redirect:/locais";
                }

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Local excluído com sucesso!");
                logger.info("Local excluído com sucesso. ID: {}", id);
                return "redirect:/locais";
        }

        private LocalFormDTO converterParaDTO(Local local) {

                LocalFormDTO formulario = new LocalFormDTO();

                formulario.setNome(local.getNome());
                formulario.setTipo(local.getTipo());
                formulario.setEndereco(local.getEndereco());
                formulario.setAtivo(local.getAtivo());

                return formulario;
        }
}