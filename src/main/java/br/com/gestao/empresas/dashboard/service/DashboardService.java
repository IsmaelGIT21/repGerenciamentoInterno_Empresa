
package br.com.gestao.empresas.dashboard.service;

import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.local.repository.LocalRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

@Service
public class DashboardService {

    private final ContaRepository contaRepository;
    private final LocalRepository localRepository;

    public DashboardService(
            ContaRepository contaRepository,
            LocalRepository localRepository) {
        this.contaRepository = contaRepository;
        this.localRepository = localRepository;
    }

    public Map<String, Object> obterIndicadores() {

        LocalDate hoje = LocalDate.now();

        YearMonth mesAtual = YearMonth.now();
        LocalDate inicioMes = mesAtual.atDay(1);
        LocalDate fimMes = mesAtual.atEndOfMonth();

        LocalDate fimProximosVencimentos = hoje.plusDays(7);

        return Map.of(
                "totalLocais",
                localRepository.count(),

                "contasPendentes",
                contaRepository.countByStatus("PENDENTE"),

                "contasPagas",
                contaRepository.countByStatus("PAGA"),

                "contasVencidas",
                contaRepository.countByStatusAndDataVencimentoBefore(
                        "PENDENTE", hoje),

                "valorTotalPendente",
                contaRepository.somarValoresPorStatus("PENDENTE"),

                "despesasDoMes",
                contaRepository.somarPorStatusEPeriodoVencimento(
                        "PENDENTE", inicioMes, fimMes),

                "pagamentosDoMes",
                contaRepository.somarPorStatusEPeriodoPagamento(
                        "PAGA", inicioMes, fimMes),

                "proximosVencimentos",
                contaRepository.findByStatusAndDataVencimentoBetweenOrderByDataVencimentoAsc(
                        "PENDENTE", hoje, fimProximosVencimentos)
        );
    }
}

