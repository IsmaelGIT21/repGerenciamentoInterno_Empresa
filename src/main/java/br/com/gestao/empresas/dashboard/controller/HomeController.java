package br.com.gestao.empresas.dashboard.controller;

import br.com.gestao.empresas.dashboard.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String paginaInicial(Model model) {

        model.addAllAttributes(
                dashboardService.obterIndicadores());

        return "home";
    }
}