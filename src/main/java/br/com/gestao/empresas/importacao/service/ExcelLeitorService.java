package br.com.gestao.empresas.importacao.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelLeitorService {

    public List<List<String>> lerPlanilha(MultipartFile arquivo) {

        List<List<String>> linhas = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(arquivo.getInputStream())) {

            Sheet planilha = workbook.getSheetAt(0);
            DataFormatter formatador = new DataFormatter();

            for (Row linha : planilha) {

                List<String> colunas = new ArrayList<>();

                for (int i = 0; i < linha.getLastCellNum(); i++) {

                    Cell celula = linha.getCell(
                            i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                    String valor = celula == null
                            ? ""
                            : formatador.formatCellValue(celula).trim();

                    colunas.add(valor);
                }

                linhas.add(colunas);
            }

        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Não foi possível ler o arquivo Excel.", e);
        }

        return linhas;
    }
}