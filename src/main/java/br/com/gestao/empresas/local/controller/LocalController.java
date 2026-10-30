package br.com.gestao.empresas.local.controller;

import br.com.gestao.empresas.local.model.Local;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.gestao.empresas.local.service.LocalService;

@Controller
@RequestMapping("/locais")
public class LocalController {
        private final LocalService localService;

        public LocalController(
                        LocalService localService) {
                this.localService = localService;
        }

        @GetMapping
        public String listar(Model model) {
                model.addAttribute("locais", localService.listarTodos());
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

                localService.cadastrar(local);

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Local cadastrado com sucesso!");

                return "redirect:/locais";
        }

        @GetMapping("/{id}/editar")
        public String formularioEdicao(
                        @PathVariable Long id,
                        Model model) {

                Local local = localService.buscarPorId(id);

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

                localService.atualizar(id, dadosFormulario);

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Local atualizado com sucesso!");

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
                                        "Não é possível excluir este local porque existem contas vinculadas.");

                        return "redirect:/locais";
                }

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Local excluído com sucesso!");

                return "redirect:/locais";
        }
}
