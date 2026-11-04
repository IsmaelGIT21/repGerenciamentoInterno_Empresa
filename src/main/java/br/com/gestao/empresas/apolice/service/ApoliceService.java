package br.com.gestao.empresas.apolice.service;

import br.com.gestao.empresas.apolice.model.Apolice;
import br.com.gestao.empresas.apolice.repository.ApoliceRepository;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ApoliceService {

    private final ApoliceRepository apoliceRepository;
    private final LocalRepository localRepository;

    public ApoliceService(
            ApoliceRepository apoliceRepository,
            LocalRepository localRepository) {

        this.apoliceRepository = apoliceRepository;
        this.localRepository = localRepository;
    }

    public List<Apolice> listarTodas() {
        return apoliceRepository.findAll();
    }

    public Apolice buscarPorId(Long id) {
        return apoliceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Apólice não encontrada"));
    }

    public List<Apolice> listarPorLocal(Long localId) {
        return apoliceRepository.findByLocalId(localId);
    }

    public Apolice cadastrar(Apolice apolice) {

        Local local = null;

        if (apolice.getLocal() != null && apolice.getLocal().getId() != null) {
            local = localRepository.findById(apolice.getLocal().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Local não encontrado"));
        }

        apolice.setLocal(local);

        return apoliceRepository.save(apolice);
    }

    public Apolice atualizar(Long id, Apolice dadosFormulario) {

        Apolice apolice = buscarPorId(id);

        Local local = null;

        if (dadosFormulario.getLocal() != null
                && dadosFormulario.getLocal().getId() != null) {

            local = localRepository.findById(dadosFormulario.getLocal().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Local não encontrado"));
        }

        apolice.setDescricao(dadosFormulario.getDescricao());
        apolice.setAnoModelo(dadosFormulario.getAnoModelo());
        apolice.setIdentificador(dadosFormulario.getIdentificador());
        apolice.setDataInicio(dadosFormulario.getDataInicio());
        apolice.setDataVencimento(dadosFormulario.getDataVencimento());
        apolice.setNumeroApolice(dadosFormulario.getNumeroApolice());
        apolice.setSeguradora(dadosFormulario.getSeguradora());
        apolice.setCorretor(dadosFormulario.getCorretor());
        apolice.setValorPremio(dadosFormulario.getValorPremio());
        apolice.setValorFranquia(dadosFormulario.getValorFranquia());
        apolice.setLmi(dadosFormulario.getLmi());
        apolice.setParcelas(dadosFormulario.getParcelas());
        apolice.setValorParcela(dadosFormulario.getValorParcela());
        apolice.setComissao(dadosFormulario.getComissao());
        apolice.setLocal(local);

        return apoliceRepository.save(apolice);
    }

    public void excluir(Long id) {
        Apolice apolice = buscarPorId(id);
        apoliceRepository.delete(apolice);
    }
}