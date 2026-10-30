package br.com.gestao.empresas.dashboard.controller;

import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.local.repository.LocalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ContaRepository contaRepository;
    private final LocalRepository localRepository;

    public HomeController(
            ContaRepository contaRepository,
            LocalRepository localRepository) {
        this.contaRepository = contaRepository;
        this.localRepository = localRepository;
    }

    @GetMapping("/")
    public String paginaInicial(Model model) {
        model.addAttribute(
                "totalLocais",
                localRepository.count());

        model.addAttribute(
                "contasPendentes",
                contaRepository.countByStatus("PENDENTE"));

        model.addAttribute(
                "contasPagas",
                contaRepository.countByStatus("PAGA"));

        model.addAttribute(
                "valorTotalPendente",
                contaRepository.somarValoresPorStatus("PENDENTE"));

        return "home";
    }
}
