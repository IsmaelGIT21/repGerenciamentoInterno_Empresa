package br.com.gestao.empresas.apolice.controller;

import br.com.gestao.empresas.apolice.dto.ApoliceFormDTO;
import br.com.gestao.empresas.apolice.model.Apolice;
import br.com.gestao.empresas.apolice.service.ApoliceService;
import br.com.gestao.empresas.local.service.LocalService;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/apolices")
public class ApoliceController {

        private final LocalService localService;
        private final ApoliceService apoliceService;
        private static final Logger logger = LoggerFactory.getLogger(ApoliceService.class);

        public ApoliceController(
                        LocalService localService,
                        ApoliceService apoliceService) {

                this.localService = localService;
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

                model.addAttribute(
                                "apolices",
                                apoliceService.listarPorLocal(localId));

                model.addAttribute("local", localService.buscarPorId(localId));

                return "apolices/lista";
        }

        @GetMapping("/nova")
        public String formulario(Model model) {

                model.addAttribute("apolice", new ApoliceFormDTO());
                model.addAttribute("locais", localService.listarTodos());
                model.addAttribute("acaoFormulario", "/apolices");

                return "apolices/formulario";
        }

        @PostMapping
        public String salvar(
                        @Valid @ModelAttribute("apolice") ApoliceFormDTO dadosFormulario,
                        BindingResult resultado,
                        Model model,
                        RedirectAttributes redirectAttributes) {

                if (resultado.hasErrors()) {
                        model.addAttribute("locais", localService.listarTodos());
                        model.addAttribute("acaoFormulario", "/apolices");
                        model.addAttribute("erro", "Verifique os campos destacados antes de continuar.");
                        return "apolices/formulario";
                }

                apoliceService.cadastrar(dadosFormulario);

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Apólice cadastrada com sucesso!");
                logger.info("Apólice cadastrada com sucesso!");
                return "redirect:/apolices";
        }

        @GetMapping("/{id}/editar")
        public String formularioEdicao(
                        @PathVariable Long id,
                        Model model) {

                Apolice apolice = apoliceService.buscarPorId(id);

                model.addAttribute("apolice", converterParaDTO(apolice));
                model.addAttribute("locais", localService.listarTodos());
                model.addAttribute(
                                "acaoFormulario",
                                "/apolices/" + id + "/editar");
                logger.info("Formulário de edição carregado para a apólice com ID: {}", id);
                return "apolices/formulario";
        }

        @PostMapping("/{id}/editar")
        public String atualizar(
                        @PathVariable Long id,
                        @Valid @ModelAttribute("apolice") ApoliceFormDTO dadosFormulario,
                        BindingResult resultado,
                        Model model,
                        RedirectAttributes redirectAttributes) {

                if (resultado.hasErrors()) {
                        model.addAttribute("locais", localService.listarTodos());
                        model.addAttribute("acaoFormulario", "/apolices/" + id + "/editar");
                        model.addAttribute("erro", "Verifique os campos destacados antes de continuar.");
                        return "apolices/formulario";
                }

                apoliceService.atualizar(id, dadosFormulario);

                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Apólice atualizada com sucesso!");
                logger.info("Apólice atualizada com sucesso. ID: {}", id);
                return "redirect:/apolices";
        }

        @PostMapping("/{id}/excluir")
        public String excluir(
                        @PathVariable Long id,
                        RedirectAttributes redirectAttributes) {

                apoliceService.excluir(id);
                redirectAttributes.addFlashAttribute(
                                "mensagem",
                                "Apólice excluída com sucesso!");
                logger.info("Apólice excluída com sucesso. ID: {}", id);
                return "redirect:/apolices";
        }

        private ApoliceFormDTO converterParaDTO(Apolice apolice) {

                ApoliceFormDTO formulario = new ApoliceFormDTO();

                formulario.setDescricao(apolice.getDescricao());
                formulario.setAnoModelo(apolice.getAnoModelo());
                formulario.setIdentificador(apolice.getIdentificador());
                formulario.setDataInicio(apolice.getDataInicio());
                formulario.setDataVencimento(apolice.getDataVencimento());
                formulario.setNumeroApolice(apolice.getNumeroApolice());
                formulario.setSeguradora(apolice.getSeguradora());
                formulario.setCorretor(apolice.getCorretor());
                formulario.setValorPremio(apolice.getValorPremio());
                formulario.setValorFranquia(apolice.getValorFranquia());
                formulario.setLmi(apolice.getLmi());
                formulario.setParcelas(apolice.getParcelas());
                formulario.setValorParcela(apolice.getValorParcela());
                formulario.setComissao(apolice.getComissao());

                if (apolice.getLocal() != null) {
                        formulario.setLocalId(apolice.getLocal().getId());
                }

                return formulario;
        }
}