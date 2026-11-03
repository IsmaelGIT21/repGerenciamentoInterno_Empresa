package br.com.gestao.empresas.importacao.controller;

import br.com.gestao.empresas.importacao.dto.ContaImportacaoDTO;
import br.com.gestao.empresas.importacao.service.ContaExcelParserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/importacao")
public class ImportacaoController {

    private final ContaExcelParserService contaExcelParserService;

    public ImportacaoController(
            ContaExcelParserService contaExcelParserService) {
        this.contaExcelParserService = contaExcelParserService;
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

        // Remove uma pré-visualização anterior.
        session.removeAttribute("contasImportacaoPreview");
        session.removeAttribute("contasImportacaoToken");

        try {

            List<ContaImportacaoDTO> contas = contaExcelParserService.interpretar(arquivo);

            // Gera um identificador exclusivo para esta importação.
            String token = UUID.randomUUID().toString();

            // Guarda os registros temporariamente no servidor.
            session.setAttribute("contasImportacaoPreview", contas);
            session.setAttribute("contasImportacaoToken", token);

            long validas = contas.stream()
                    .filter(ContaImportacaoDTO::isValido)
                    .count();

            long invalidas = contas.size() - validas;

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
}