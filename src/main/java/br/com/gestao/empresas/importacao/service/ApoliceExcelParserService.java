package br.com.gestao.empresas.importacao.service;

import br.com.gestao.empresas.importacao.dto.ApoliceImportacaoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ApoliceExcelParserService {

    private final ExcelLeitorService excelLeitorService;

    private static final DateTimeFormatter[] FORMATOS_DATA = {
            DateTimeFormatter.ofPattern("dd/MM/uuuu"),
            DateTimeFormatter.ofPattern("dd-MM-uuuu"),
            DateTimeFormatter.ofPattern("dd/MM/uu"),
            DateTimeFormatter.ofPattern("dd-MM-uu")
    };

    private static final Pattern FORMATO_PARCELAS =
            Pattern.compile("^(\\d+)\\s*x\\s*(.+)$", Pattern.CASE_INSENSITIVE);

    public ApoliceExcelParserService(ExcelLeitorService excelLeitorService) {
        this.excelLeitorService = excelLeitorService;
    }

    public List<ApoliceImportacaoDTO> interpretar(MultipartFile arquivo) {

        List<List<String>> linhas = excelLeitorService.lerPlanilha(arquivo);

        int linhaCabecalho = -1;

        int[] colunas = new int[13];

        for (int i = 0; i < colunas.length; i++) {
            colunas[i] = -1;
        }

        // Localiza o cabeçalho da planilha.
        for (int i = 0; i < linhas.size(); i++) {

            List<String> linha = linhas.get(i);

            for (int j = 0; j < linha.size(); j++) {

                String coluna = normalizar(linha.get(j));

                if (coluna.contains("item") && coluna.contains("descricao")) {
                    colunas[0] = j;
                } else if (coluna.contains("ano") && coluna.contains("modelo")) {
                    colunas[1] = j;
                } else if (coluna.contains("placa") || coluna.contains("identificador")) {
                    colunas[2] = j;
                } else if (coluna.equals("data inicio")) {
                    colunas[3] = j;
                } else if (coluna.startsWith("data venc")) {
                    colunas[4] = j;
                } else if (coluna.contains("apolice") || coluna.contains("proposta")) {
                    colunas[5] = j;
                } else if (coluna.equals("seguradora")) {
                    colunas[6] = j;
                } else if (coluna.equals("corretor")) {
                    colunas[7] = j;
                } else if (coluna.contains("valor premio")) {
                    colunas[8] = j;
                } else if (coluna.contains("valor franquia")) {
                    colunas[9] = j;
                } else if (coluna.contains("l.m.i") || coluna.equals("lmi")) {
                    colunas[10] = j;
                } else if (coluna.startsWith("parcelas")) {
                    colunas[11] = j;
                } else if (coluna.contains("comissao")) {
                    colunas[12] = j;
                }
            }

            boolean cabecalhoEncontrado = true;

            for (int coluna : colunas) {
                if (coluna < 0) {
                    cabecalhoEncontrado = false;
                    break;
                }
            }

            if (cabecalhoEncontrado) {
                linhaCabecalho = i;
                break;
            }
        }

        if (linhaCabecalho < 0) {
            throw new IllegalArgumentException(
                    "Não foi possível identificar o cabeçalho da planilha de Apólices.");
        }

        List<ApoliceImportacaoDTO> apolices = new ArrayList<>();

        // Processa as linhas abaixo do cabeçalho.
        for (int i = linhaCabecalho + 1; i < linhas.size(); i++) {

            List<String> linha = linhas.get(i);

            String descricao = obterCelula(linha, colunas[0]);
            String anoModelo = obterCelula(linha, colunas[1]);
            String identificador = obterCelula(linha, colunas[2]);
            String dataInicio = obterCelula(linha, colunas[3]);
            String dataVencimento = obterCelula(linha, colunas[4]);
            String numeroApolice = obterCelula(linha, colunas[5]);
            String seguradora = obterCelula(linha, colunas[6]);
            String corretor = obterCelula(linha, colunas[7]);
            String valorPremio = obterCelula(linha, colunas[8]);
            String valorFranquia = obterCelula(linha, colunas[9]);
            String lmi = obterCelula(linha, colunas[10]);
            String parcelas = obterCelula(linha, colunas[11]);
            String comissao = obterCelula(linha, colunas[12]);

            // Ignora linhas completamente vazias.
            if (linha.stream().allMatch(celula ->
                    celula == null || celula.isBlank())) {
                continue;
            }

            // Ignora registros identificados como sem seguro.
            if (normalizar(dataInicio).equals("sem seguro")) {
                continue;
            }

            // Ignora cabeçalhos repetidos.
            if (normalizar(descricao).contains("item")
                    && normalizar(descricao).contains("descricao")) {
                continue;
            }

            ApoliceImportacaoDTO apolice = new ApoliceImportacaoDTO();

            apolice.setLinhaExcel(i + 1);
            apolice.setDescricao(valorOuNull(descricao));
            apolice.setAnoModelo(valorOuNull(anoModelo));
            apolice.setIdentificador(valorOuNull(identificador));
            apolice.setNumeroApolice(valorOuNull(numeroApolice));
            apolice.setSeguradora(valorOuNull(seguradora));
            apolice.setCorretor(valorOuNull(corretor));

            if (descricao.isBlank() || descricao.equals("-")) {
                apolice.adicionarErro("Descrição não informada.");
            }

            try {
                apolice.setDataInicio(interpretarData(dataInicio));
            } catch (IllegalArgumentException e) {
                apolice.adicionarErro(e.getMessage());
            }

            try {
                apolice.setDataVencimento(interpretarData(dataVencimento));
            } catch (IllegalArgumentException e) {
                apolice.adicionarErro(e.getMessage());
            }

            try {
                apolice.setValorPremio(interpretarValorOpcional(valorPremio));
            } catch (IllegalArgumentException e) {
                apolice.adicionarErro(e.getMessage());
            }

            try {
                apolice.setValorFranquia(interpretarValorOpcional(valorFranquia));
            } catch (IllegalArgumentException e) {
                apolice.adicionarErro(e.getMessage());
            }

            try {
                apolice.setLmi(interpretarValorOpcional(lmi));
            } catch (IllegalArgumentException e) {
                apolice.adicionarErro(e.getMessage());
            }

            try {
                interpretarParcelas(parcelas, apolice);
            } catch (IllegalArgumentException e) {
                apolice.adicionarErro(e.getMessage());
            }

            try {
                apolice.setComissao(interpretarValorOpcional(comissao));
            } catch (IllegalArgumentException e) {
                apolice.adicionarErro(e.getMessage());
            }

            apolices.add(apolice);
        }

        return apolices;
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

    private String valorOuNull(String texto) {

        if (texto == null || texto.isBlank() || texto.trim().equals("-")) {
            return null;
        }

        return texto.trim();
    }

    private LocalDate interpretarData(String texto) {

        if (texto == null || texto.isBlank() || texto.trim().equals("-")) {
            throw new IllegalArgumentException("Data não informada.");
        }

        for (DateTimeFormatter formato : FORMATOS_DATA) {
            try {
                return LocalDate.parse(texto.trim(), formato);
            } catch (DateTimeParseException ignored) {
                // Tenta o próximo formato.
            }
        }

        throw new IllegalArgumentException("Data inválida: " + texto);
    }

    private BigDecimal interpretarValorOpcional(String texto) {

        if (texto == null || texto.isBlank() || texto.trim().equals("-")) {
            return null;
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
                        "O valor deve ser maior que zero: " + texto);
            }

            return valor;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido: " + texto);
        }
    }

    private void interpretarParcelas(
            String texto,
            ApoliceImportacaoDTO apolice) {

        if (texto == null || texto.isBlank() || texto.trim().equals("-")) {
            return;
        }

        Matcher matcher = FORMATO_PARCELAS.matcher(texto.trim());

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Formato de parcelas inválido: " + texto);
        }

        try {
            int quantidade = Integer.parseInt(matcher.group(1));

            if (quantidade <= 0) {
                throw new IllegalArgumentException(
                        "A quantidade de parcelas deve ser maior que zero.");
            }

            BigDecimal valor = interpretarValorOpcional(matcher.group(2));

            apolice.setParcelas(quantidade);
            apolice.setValorParcela(valor);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Quantidade de parcelas inválida: " + texto);
        }
    }
}
