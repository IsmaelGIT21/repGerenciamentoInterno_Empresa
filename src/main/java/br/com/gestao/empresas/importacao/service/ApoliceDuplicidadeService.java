package br.com.gestao.empresas.importacao.service;

import br.com.gestao.empresas.apolice.repository.ApoliceRepository;
import br.com.gestao.empresas.importacao.dto.ApoliceImportacaoDTO;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ApoliceDuplicidadeService {

    private final ApoliceRepository apoliceRepository;

    public ApoliceDuplicidadeService(ApoliceRepository apoliceRepository) {
        this.apoliceRepository = apoliceRepository;
    }

    public void verificarDuplicidades(List<ApoliceImportacaoDTO> apolices) {

        Set<String> registrosEncontrados = new HashSet<>();

        for (ApoliceImportacaoDTO apolice : apolices) {

            // Ignora registros inválidos.
            if (!apolice.isValido()) {
                continue;
            }

            String numero = normalizarOpcional(apolice.getNumeroApolice());
            String seguradora = normalizarOpcional(apolice.getSeguradora());
            String identificador = normalizarOpcional(apolice.getIdentificador());

            boolean duplicidadeBanco = false;
            String chave = null;

            // Verifica pelo número da apólice e seguradora.
            if (numero != null && seguradora != null) {

                duplicidadeBanco = apoliceRepository
                        .existsByNumeroApoliceIgnoreCaseAndSeguradoraIgnoreCase(
                                numero,
                                seguradora
                        );

                chave = "NUMERO|" + numero + "|" + seguradora;

            } else if (identificador != null
                    && seguradora != null
                    && apolice.getDataInicio() != null
                    && apolice.getDataVencimento() != null) {

                // Sem número, verifica identificador, seguradora e vigência.
                duplicidadeBanco = apoliceRepository
                        .existsByIdentificadorIgnoreCaseAndSeguradoraIgnoreCaseAndDataInicioAndDataVencimento(
                                identificador,
                                seguradora,
                                apolice.getDataInicio(),
                                apolice.getDataVencimento()
                        );

                chave = "IDENTIFICADOR|" + identificador + "|"
                        + seguradora + "|"
                        + apolice.getDataInicio() + "|"
                        + apolice.getDataVencimento();
            }

            // Verifica se a combinação já apareceu na planilha.
            boolean duplicidadePlanilha = chave != null
                    && !registrosEncontrados.add(chave);

            // Marca possíveis duplicidades encontradas.
            apolice.setPossivelDuplicidade(
                    duplicidadeBanco || duplicidadePlanilha
            );
        }
    }

    private String normalizarOpcional(String texto) {

        if (texto == null || texto.isBlank()) {
            return null;
        }

        return texto.trim().toLowerCase(Locale.ROOT);
    }
}
