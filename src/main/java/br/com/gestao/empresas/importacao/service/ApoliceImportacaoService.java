package br.com.gestao.empresas.importacao.service;

import br.com.gestao.empresas.apolice.model.Apolice;
import br.com.gestao.empresas.apolice.repository.ApoliceRepository;
import br.com.gestao.empresas.importacao.dto.ApoliceImportacaoDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApoliceImportacaoService {

    private final ApoliceRepository apoliceRepository;
    private final TransactionTemplate transactionTemplate;

    public ApoliceImportacaoService(
            ApoliceRepository apoliceRepository,
            PlatformTransactionManager transactionManager) {

        this.apoliceRepository = apoliceRepository;

        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public List<ApoliceImportacaoDTO> importar(
            List<ApoliceImportacaoDTO> apolices) {

        List<ApoliceImportacaoDTO> rejeitadas = new ArrayList<>();

        for (ApoliceImportacaoDTO dto : apolices) {

            // Ignora registros que possuem erros de leitura.
            if (!dto.isValido()) {
                rejeitadas.add(dto);
                continue;
            }

            // Ignora possíveis duplicidades.
            if (dto.isPossivelDuplicidade()) {
                continue;
            }

            try {
                // Cada apólice é salva em sua própria transação.
                transactionTemplate.execute(status -> {

                    Apolice apolice = new Apolice();

                    apolice.setDescricao(dto.getDescricao());
                    apolice.setAnoModelo(dto.getAnoModelo());
                    apolice.setIdentificador(dto.getIdentificador());
                    apolice.setDataInicio(dto.getDataInicio());
                    apolice.setDataVencimento(dto.getDataVencimento());
                    apolice.setNumeroApolice(dto.getNumeroApolice());
                    apolice.setSeguradora(dto.getSeguradora());
                    apolice.setCorretor(dto.getCorretor());
                    apolice.setValorPremio(dto.getValorPremio());
                    apolice.setValorFranquia(dto.getValorFranquia());
                    apolice.setLmi(dto.getLmi());
                    apolice.setParcelas(dto.getParcelas());
                    apolice.setValorParcela(dto.getValorParcela());
                    apolice.setComissao(dto.getComissao());

                    // A planilha não possui informação de Local.
                    apolice.setLocal(null);

                    apoliceRepository.save(apolice);

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
