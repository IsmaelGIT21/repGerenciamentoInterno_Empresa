package br.com.gestao.empresas.importacao.service;

import br.com.gestao.empresas.conta.model.Conta;
import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.service.FornecedorService;
import br.com.gestao.empresas.importacao.dto.ContaImportacaoDTO;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContaImportacaoService {

    private final ContaRepository contaRepository;
    private final LocalRepository localRepository;
    private final FornecedorService fornecedorService;

    public ContaImportacaoService(
            ContaRepository contaRepository,
            LocalRepository localRepository,
            FornecedorService fornecedorService) {

        this.contaRepository = contaRepository;
        this.localRepository = localRepository;
        this.fornecedorService = fornecedorService;
    }

    @Transactional
    public void importar(List<ContaImportacaoDTO> contas) {

        // Primeiro, verifica se existem erros na planilha.
        boolean possuiErros = false;

        for (ContaImportacaoDTO dto : contas) {

            if (dto.isValido()) {
                Local local = localRepository
                        .findByNomeIgnoreCase(dto.getNomeLocal())
                        .orElse(null);

                if (local == null) {
                    dto.adicionarErro(
                            "Local não cadastrado: " + dto.getNomeLocal());
                }
            }

            if (!dto.isValido()) {
                possuiErros = true;
            }
        }

        // Se houver algum erro, nenhuma conta será importada.
        if (possuiErros) {
            throw new IllegalArgumentException(
                    "A importação foi cancelada. Existem erros na planilha.");
        }

        // Com todos os dados validados, realiza a importação.
        for (ContaImportacaoDTO dto : contas) {

            Local local = localRepository
                    .findByNomeIgnoreCase(dto.getNomeLocal())
                    .orElseThrow();

            Fornecedor fornecedor = fornecedorService.buscarOuCriar(dto.getFornecedor());

            Conta conta = new Conta();
            conta.setFornecedor(fornecedor);
            conta.setDescricao(dto.getDescricao());
            conta.setValor(dto.getValor());
            conta.setDataVencimento(dto.getDataVencimento());
            conta.setLocal(local);
            conta.setStatus("PENDENTE");

            contaRepository.save(conta);
        }
    }
}
