package br.com.gestao.empresas.importacao.service;

import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.importacao.dto.ContaImportacaoDTO;
import br.com.gestao.empresas.local.repository.LocalRepository;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ContaDuplicidadeService {

    private final ContaRepository contaRepository;
    private final LocalRepository localRepository;

    public ContaDuplicidadeService(
            ContaRepository contaRepository,
            LocalRepository localRepository) {

        this.contaRepository = contaRepository;
        this.localRepository = localRepository;
    }

    public void verificarDuplicidades(List<ContaImportacaoDTO> contas) {

        Set<String> registrosEncontrados = new HashSet<>();

        for (ContaImportacaoDTO conta : contas) {

            // Ignora registros inválidos.
            if (!conta.isValido()) {
                continue;
            }

            // Confere se o Local existe no banco.
            boolean localExiste = localRepository
                    .findByNomeIgnoreCase(conta.getNomeLocal())
                    .isPresent();

            if (!localExiste) {
                conta.adicionarErro(
                        "Local '" + conta.getNomeLocal()
                                + "' não está cadastrado no sistema.");

                continue;
            }

            // Consulta possíveis duplicidades no banco.
            boolean duplicidadeBanco = contaRepository.existePossivelDuplicidade(
                    conta.getFornecedor(),
                    conta.getDescricao(),
                    conta.getNomeLocal(),
                    conta.getDataVencimento(),
                    conta.getValor());

            // Cria uma identificação dos cinco campos.
            String identificador = criarIdentificador(conta);

            // Verifica se a combinação já apareceu na planilha.
            boolean duplicidadePlanilha = !registrosEncontrados.add(identificador);

            // Marca possíveis duplicidades encontradas.
            conta.setPossivelDuplicidade(
                    duplicidadeBanco || duplicidadePlanilha);
        }
    }

    private String criarIdentificador(ContaImportacaoDTO conta) {

        return normalizar(conta.getFornecedor()) + "|"
                + normalizar(conta.getDescricao()) + "|"
                + normalizar(conta.getNomeLocal()) + "|"
                + conta.getDataVencimento() + "|"
                + conta.getValor().stripTrailingZeros().toPlainString();
    }

    private String normalizar(String texto) {
        return texto.trim().toLowerCase(Locale.ROOT);
    }
}