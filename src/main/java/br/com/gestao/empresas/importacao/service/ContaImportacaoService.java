package br.com.gestao.empresas.importacao.service;

import br.com.gestao.empresas.conta.model.Conta;
import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.service.FornecedorService;
import br.com.gestao.empresas.importacao.dto.ContaImportacaoDTO;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContaImportacaoService {

    private final ContaRepository contaRepository;
    private final LocalRepository localRepository;
    private final FornecedorService fornecedorService;
    private final TransactionTemplate transactionTemplate;

    public ContaImportacaoService(
            ContaRepository contaRepository,
            LocalRepository localRepository,
            FornecedorService fornecedorService,
            PlatformTransactionManager transactionManager) {

        this.contaRepository = contaRepository;
        this.localRepository = localRepository;
        this.fornecedorService = fornecedorService;

        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public List<ContaImportacaoDTO> importar(
            List<ContaImportacaoDTO> contas) {

        List<ContaImportacaoDTO> rejeitadas = new ArrayList<>();

        for (ContaImportacaoDTO dto : contas) {

            // Ignora registros que já possuem erros de leitura.
            if (!dto.isValido()) {
                rejeitadas.add(dto);
                continue;
            }

            // Verifica se o local está cadastrado.
            Local local = localRepository
                    .findByNomeIgnoreCase(dto.getNomeLocal())
                    .orElse(null);

            if (local == null) {
                dto.adicionarErro(
                        "Local não cadastrado: " + dto.getNomeLocal());

                rejeitadas.add(dto);
                continue;
            }

            try {
                // Cada conta é salva em sua própria transação.
                transactionTemplate.execute(status -> {

                    Fornecedor fornecedor = fornecedorService.buscarOuCriar(
                            dto.getFornecedor());

                    Conta conta = new Conta();
                    conta.setFornecedor(fornecedor);
                    conta.setDescricao(dto.getDescricao());
                    conta.setValor(dto.getValor());
                    conta.setDataVencimento(dto.getDataVencimento());
                    conta.setLocal(local);
                    conta.setStatus("PENDENTE");

                    contaRepository.save(conta);

                    return null;
                });

            } catch (RuntimeException e) {
                dto.adicionarErro(
                        "Não foi possível salvar este registro no banco de dados.");

                rejeitadas.add(dto);
            }
        }

        return rejeitadas;
    }
}