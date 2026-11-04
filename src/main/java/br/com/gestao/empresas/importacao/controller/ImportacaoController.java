package br.com.gestao.empresas.importacao.controller;

import br.com.gestao.empresas.importacao.dto.ContaImportacaoDTO;
import br.com.gestao.empresas.importacao.dto.ApoliceImportacaoDTO;

import br.com.gestao.empresas.importacao.service.ContaExcelParserService;
import br.com.gestao.empresas.importacao.service.ContaDuplicidadeService;
import br.com.gestao.empresas.importacao.service.ContaImportacaoService;

import br.com.gestao.empresas.importacao.service.ApoliceExcelParserService;
import br.com.gestao.empresas.importacao.service.ApoliceDuplicidadeService;

import jakarta.servlet.http.HttpSession;
import br.com.gestao.empresas.importacao.service.ApoliceImportacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/importacao")
public class ImportacaoController {

    private final ContaImportacaoService contaImportacaoService;
    private final ContaExcelParserService contaExcelParserService;
    private final ContaDuplicidadeService contaDuplicidadeService;
    private final ApoliceImportacaoService apoliceImportacaoService;
    private final ApoliceExcelParserService apoliceExcelParserService;
    private final ApoliceDuplicidadeService apoliceDuplicidadeService;

    public ImportacaoController(
            ContaImportacaoService contaImportacaoService,
            ContaExcelParserService contaExcelParserService,
            ContaDuplicidadeService contaDuplicidadeService,
            ApoliceExcelParserService apoliceExcelParserService,
            ApoliceDuplicidadeService apoliceDuplicidadeService,
            ApoliceImportacaoService apoliceImportacaoService) {

        this.contaImportacaoService = contaImportacaoService;
        this.contaExcelParserService = contaExcelParserService;
        this.contaDuplicidadeService = contaDuplicidadeService;

        this.apoliceExcelParserService = apoliceExcelParserService;
        this.apoliceDuplicidadeService = apoliceDuplicidadeService;
        this.apoliceImportacaoService = apoliceImportacaoService;
    }

    @GetMapping
    public String index() {
        return "importacao/index";
    }

    @PostMapping("/contas/preview")
    public String visualizarContas(
            @RequestParam("arquivo") MultipartFile arquivo,
            Model model,
            HttpSession session) {

        session.removeAttribute("contasImportacaoPreview");
        session.removeAttribute("contasImportacaoToken");

        try {

            List<ContaImportacaoDTO> contas = contaExcelParserService.interpretar(arquivo);

            contaDuplicidadeService.verificarDuplicidades(contas);

            String token = UUID.randomUUID().toString();

            session.setAttribute("contasImportacaoPreview", contas);
            session.setAttribute("contasImportacaoToken", token);

            long validas = contas.stream()
                    .filter(ContaImportacaoDTO::isValido)
                    .count();

            long invalidas = contas.stream()
                    .filter(conta -> !conta.isValido())
                    .count();

            long duplicidades = contas.stream()
                    .filter(ContaImportacaoDTO::isPossivelDuplicidade)
                    .count();

            model.addAttribute("totalDuplicidades", duplicidades);
            model.addAttribute("contas", contas);
            model.addAttribute("totalRegistros", contas.size());
            model.addAttribute("totalValidos", validas);
            model.addAttribute("totalInvalidos", invalidas);
            model.addAttribute("tokenImportacao", token);

        } catch (IllegalArgumentException e) {

            model.addAttribute("erro", e.getMessage());
        }

        return "importacao/index";
    }

    @PostMapping("/apolices/preview")
    public String visualizarApolices(
            @RequestParam("arquivo") MultipartFile arquivo,
            Model model,
            HttpSession session) {

        // Remove uma pré-visualização anterior de Apólices.
        session.removeAttribute("apolicesImportacaoPreview");
        session.removeAttribute("apolicesImportacaoToken");

        try {

            List<ApoliceImportacaoDTO> apolices = apoliceExcelParserService.interpretar(arquivo);

            // Identifica possíveis duplicidades no banco e na planilha.
            apoliceDuplicidadeService.verificarDuplicidades(apolices);

            // Gera um identificador exclusivo para esta importação.
            String token = UUID.randomUUID().toString();

            // Guarda os registros temporariamente no servidor.
            session.setAttribute("apolicesImportacaoPreview", apolices);
            session.setAttribute("apolicesImportacaoToken", token);

            long validas = apolices.stream()
                    .filter(ApoliceImportacaoDTO::isValido)
                    .count();

            long invalidas = apolices.stream()
                    .filter(apolice -> !apolice.isValido())
                    .count();

            long duplicidades = apolices.stream()
                    .filter(ApoliceImportacaoDTO::isPossivelDuplicidade)
                    .count();

            model.addAttribute("apolices", apolices);
            model.addAttribute("totalRegistros", apolices.size());
            model.addAttribute("totalValidos", validas);
            model.addAttribute("totalInvalidos", invalidas);
            model.addAttribute("totalDuplicidades", duplicidades);
            model.addAttribute("tokenImportacao", token);

        } catch (IllegalArgumentException e) {

            model.addAttribute("erro", e.getMessage());
        }

        return "importacao/index";
    }

    @PostMapping("/contas/confirmar")
    public String confirmarImportacao(
            @RequestParam("token") String token,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object preview = session.getAttribute("contasImportacaoPreview");
        Object tokenSessao = session.getAttribute("contasImportacaoToken");

        if (!(preview instanceof List<?>)
                || !Objects.equals(token, tokenSessao)) {

            redirectAttributes.addFlashAttribute(
                    "erro", "Pré-visualização inválida ou expirada. Envie a planilha novamente.");

            return "redirect:/importacao";
        }

        @SuppressWarnings("unchecked")
        List<ContaImportacaoDTO> contas = (List<ContaImportacaoDTO>) preview;

        long invalidas = contas.stream()
                .filter(conta -> !conta.isValido())
                .count();

        long duplicidades = contas.stream()
                .filter(ContaImportacaoDTO::isPossivelDuplicidade)
                .count();

        long importaveis = contas.stream()
                .filter(conta -> conta.isValido()
                        && !conta.isPossivelDuplicidade())
                .count();

        session.removeAttribute("contasImportacaoPreview");
        session.removeAttribute("contasImportacaoToken");

        List<ContaImportacaoDTO> rejeitadas = contaImportacaoService.importar(contas);

        long falhas = Math.max(0, rejeitadas.size() - invalidas);
        long importadas = importaveis - falhas;

        redirectAttributes.addFlashAttribute(
                "sucesso", "Importação concluída!");

        redirectAttributes.addFlashAttribute("totalImportadas", importadas);
        redirectAttributes.addFlashAttribute("totalDuplicidadesIgnoradas", duplicidades);
        redirectAttributes.addFlashAttribute("totalInvalidas", invalidas);
        redirectAttributes.addFlashAttribute("totalFalhas", falhas);

        return "redirect:/importacao";
    }

    @PostMapping("/apolices/confirmar")
    public String confirmarImportacaoApolices(
            @RequestParam("token") String token,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Object preview = session.getAttribute("apolicesImportacaoPreview");
        Object tokenSessao = session.getAttribute("apolicesImportacaoToken");

        if (!(preview instanceof List<?>)
                || !Objects.equals(token, tokenSessao)) {

            redirectAttributes.addFlashAttribute(
                    "erro", "Pré-visualização inválida ou expirada. Envie a planilha novamente.");

            return "redirect:/importacao";
        }

        @SuppressWarnings("unchecked")
        List<ApoliceImportacaoDTO> apolices = (List<ApoliceImportacaoDTO>) preview;

        long invalidas = apolices.stream()
                .filter(apolice -> !apolice.isValido())
                .count();

        long duplicidades = apolices.stream()
                .filter(ApoliceImportacaoDTO::isPossivelDuplicidade)
                .count();

        long importaveis = apolices.stream()
                .filter(apolice -> apolice.isValido()
                        && !apolice.isPossivelDuplicidade())
                .count();

        // Consome a pré-visualização para impedir reutilização.
        session.removeAttribute("apolicesImportacaoPreview");
        session.removeAttribute("apolicesImportacaoToken");

        List<ApoliceImportacaoDTO> rejeitadas = apoliceImportacaoService.importar(apolices);

        long falhas = Math.max(0, rejeitadas.size() - invalidas);
        long importadas = importaveis - falhas;

        redirectAttributes.addFlashAttribute(
                "sucesso", "Importação de Apólices concluída!");

        redirectAttributes.addFlashAttribute("totalImportadas", importadas);
        redirectAttributes.addFlashAttribute("totalDuplicidadesIgnoradas", duplicidades);
        redirectAttributes.addFlashAttribute("totalInvalidas", invalidas);
        redirectAttributes.addFlashAttribute("totalFalhas", falhas);

        return "redirect:/importacao";
    }
}