package br.com.gestao.empresas.importacao;

import br.com.gestao.empresas.importacao.dto.ContaImportacaoDTO;
import br.com.gestao.empresas.importacao.service.ContaExcelParserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Configuration
public class TesteImportacaoExcel {

    @Bean
    public CommandLineRunner testarParser(ContaExcelParserService parser) {

        return args -> {
            File arquivo = new File(
                    "/home/ismael/Downloads/contas_exemplo.xlsx");

            MultipartFile multipartFile = new MultipartFile() {

                @Override
                public String getName() {
                    return "arquivo";
                }

                @Override
                public String getOriginalFilename() {
                    return arquivo.getName();
                }

                @Override
                public String getContentType() {
                    return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                }

                @Override
                public boolean isEmpty() {
                    return arquivo.length() == 0;
                }

                @Override
                public long getSize() {
                    return arquivo.length();
                }

                @Override
                public byte[] getBytes() throws java.io.IOException {
                    return java.nio.file.Files.readAllBytes(arquivo.toPath());
                }

                @Override
                public InputStream getInputStream() throws java.io.IOException {
                    return new FileInputStream(arquivo);
                }

                @Override
                public void transferTo(File destino) throws java.io.IOException {
                    java.nio.file.Files.copy(
                            arquivo.toPath(),
                            destino.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            };

            List<ContaImportacaoDTO> contas =
                    parser.interpretar(multipartFile);

            System.out.println("\n========== RESULTADO DA LEITURA ==========");

            for (ContaImportacaoDTO conta : contas) {
                System.out.println("\nLinha Excel: " + conta.getLinhaExcel());
                System.out.println("Fornecedor: " + conta.getFornecedor());
                System.out.println("Descrição: " + conta.getDescricao());
                System.out.println("Local: " + conta.getNomeLocal());
                System.out.println("Vencimento: " + conta.getDataVencimento());
                System.out.println("Valor: " + conta.getValor());
                System.out.println("Erros: " + conta.getErros());
            }

            System.out.println("\nTotal de registros: " + contas.size());
        };
    }
}
