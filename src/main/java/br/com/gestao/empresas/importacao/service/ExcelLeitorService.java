package br.com.gestao.empresas.importacao.service;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelLeitorService {

    public List<List<String>> lerPlanilha(MultipartFile arquivo) {

        // Verifica se o arquivo foi enviado.
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "Selecione um arquivo Excel para continuar.");
        }

        List<List<String>> linhas = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(arquivo.getInputStream())) {

            // Verifica se o arquivo possui alguma aba.
            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException(
                        "O arquivo não possui nenhuma planilha.");
            }

            Sheet planilha = workbook.getSheetAt(0);

            // Verifica se a primeira aba possui linhas.
            if (planilha.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException(
                        "A planilha está vazia.");
            }

            DataFormatter formatador = new DataFormatter();

            for (Row linha : planilha) {

                List<String> colunas = new ArrayList<>();

                short ultimaColuna = linha.getLastCellNum();

                for (int i = 0; i < Math.max(ultimaColuna, 0); i++) {

                    Cell celula = linha.getCell(
                            i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                    String valor = celula == null
                            ? ""
                            : formatador.formatCellValue(celula).trim();

                    colunas.add(valor);
                }

                linhas.add(colunas);
            }

        } catch (EncryptedDocumentException e) {

            throw new IllegalArgumentException(
                    "O arquivo Excel está protegido por senha.");

        } catch (IOException e) {

            throw new IllegalArgumentException(
                    "O arquivo é inválido ou não pode ser lido como Excel.");
        }

        return linhas;
    }
} 