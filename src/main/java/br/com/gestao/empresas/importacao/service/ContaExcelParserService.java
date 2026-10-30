package br.com.gestao.empresas.importacao.service;

import br.com.gestao.empresas.importacao.dto.ContaImportacaoDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ContaExcelParserService {

    private final ExcelLeitorService excelLeitorService;

    private static final DateTimeFormatter[] FORMATOS_DATA = {
            DateTimeFormatter.ofPattern("dd/MM/uuuu"),
            DateTimeFormatter.ofPattern("dd-MM-uuuu"),
            DateTimeFormatter.ofPattern("dd-MM-uu"),
            DateTimeFormatter.ofPattern("dd/MM/uu")
    };

    public ContaExcelParserService(ExcelLeitorService excelLeitorService) {
        this.excelLeitorService = excelLeitorService;
    }

    public List<ContaImportacaoDTO> interpretar(
            org.springframework.web.multipart.MultipartFile arquivo) {

        List<List<String>> linhas = excelLeitorService.lerPlanilha(arquivo);

        int linhaCabecalho = -1;
        int colunaFornecedor = -1;
        int colunaDescricao = -1;
        int colunaLocal = -1;
        int colunaVencimento = -1;
        int colunaValor = -1;

        // Localiza o cabeçalho da tabela.
        for (int i = 0; i < linhas.size(); i++) {
            List<String> linha = linhas.get(i);

            for (int j = 0; j < linha.size(); j++) {
                String coluna = normalizar(linha.get(j));

                if (coluna.equals("empresa")) {
                    colunaFornecedor = j;
                } else if (coluna.equals("descricao")) {
                    colunaDescricao = j;
                } else if (coluna.equals("local de recebimento")) {
                    colunaLocal = j;
                } else if (coluna.equals("vencimento")) {
                    colunaVencimento = j;
                } else if (coluna.startsWith("valor")) {
                    colunaValor = j;
                }
            }

            if (colunaFornecedor >= 0
                    && colunaDescricao >= 0
                    && colunaLocal >= 0
                    && colunaVencimento >= 0
                    && colunaValor >= 0) {

                linhaCabecalho = i;
                break;
            }
        }

        if (linhaCabecalho < 0) {
            throw new IllegalArgumentException(
                    "Não foi possível identificar o cabeçalho da planilha.");
        }

        List<ContaImportacaoDTO> contas = new ArrayList<>();

        // Processa as linhas abaixo do cabeçalho.
        for (int i = linhaCabecalho + 1; i < linhas.size(); i++) {

            List<String> linha = linhas.get(i);

            String fornecedor = obterCelula(linha, colunaFornecedor);
            String descricao = obterCelula(linha, colunaDescricao);
            String local = obterCelula(linha, colunaLocal);
            String vencimento = obterCelula(linha, colunaVencimento);
            String valor = obterCelula(linha, colunaValor);

            // Ignora linhas completamente vazias.
            if (fornecedor.isBlank()
                    && descricao.isBlank()
                    && local.isBlank()
                    && vencimento.isBlank()
                    && valor.isBlank()) {
                continue;
            }

            // Ignora linhas de totalização e observações.
            boolean linhaTotalOuObservacao = linha.stream()
                    .map(this::normalizar)
                    .anyMatch(coluna -> coluna.equals("total")
                            || coluna.startsWith("total ")
                            || coluna.startsWith("obs")
                            || coluna.startsWith("observacao"));

            if (linhaTotalOuObservacao) {
                continue;
            }

            // Ignora cabeçalhos repetidos.
            if (normalizar(fornecedor).equals("empresa")) {
                continue;
            }

            ContaImportacaoDTO conta = new ContaImportacaoDTO();

            // +1 porque as linhas do Excel começam em 1.
            conta.setLinhaExcel(i + 1);
            conta.setFornecedor(fornecedor.trim());
            conta.setDescricao(descricao.trim());
            conta.setNomeLocal(local.trim());

            if (fornecedor.isBlank()) {
                conta.adicionarErro("Fornecedor não informado.");
            }

            if (descricao.isBlank()) {
                conta.adicionarErro("Descrição não informada.");
            }

            if (local.isBlank()) {
                conta.adicionarErro("Local de recebimento não informado.");
            }

            try {
                conta.setDataVencimento(interpretarData(vencimento));
            } catch (IllegalArgumentException e) {
                conta.adicionarErro(e.getMessage());
            }

            try {
                conta.setValor(interpretarValor(valor));
            } catch (IllegalArgumentException e) {
                conta.adicionarErro(e.getMessage());
            }

            contas.add(conta);
        }

        return contas;
    }

    private String obterCelula(List<String> linha, int indice) {
        if (indice < 0 || indice >= linha.size() || linha.get(indice) == null) {
            return "";
        }

        return linha.get(indice).trim();
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }

        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private LocalDate interpretarData(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Data de vencimento não informada.");
        }

        for (DateTimeFormatter formato : FORMATOS_DATA) {
            try {
                return LocalDate.parse(texto.trim(), formato);
            } catch (DateTimeParseException ignored) {
                // Tenta o próximo formato.
            }
        }

        throw new IllegalArgumentException(
                "Data inválida ou incompleta: " + texto);
    }

    private BigDecimal interpretarValor(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Valor não informado.");
        }

        String valorLimpo = texto.replaceAll("[^0-9,.-]", "");

        if (valorLimpo.contains(",")) {
            valorLimpo = valorLimpo
                    .replace(".", "")
                    .replace(",", ".");
        }

        try {
            BigDecimal valor = new BigDecimal(valorLimpo);

            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "O valor deve ser maior que zero.");
            }

            return valor;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Valor inválido: " + texto);
        }
    }
}