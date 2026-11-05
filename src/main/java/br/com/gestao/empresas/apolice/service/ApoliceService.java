package br.com.gestao.empresas.apolice.service;

import br.com.gestao.empresas.apolice.dto.ApoliceFormDTO;
import br.com.gestao.empresas.apolice.model.Apolice;
import br.com.gestao.empresas.apolice.repository.ApoliceRepository;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Apolice cadastrar(ApoliceFormDTO dadosFormulario) {

        Apolice apolice = new Apolice();

        preencherDados(apolice, dadosFormulario);

        return apoliceRepository.save(apolice);
    }

    @Transactional
    public Apolice atualizar(Long id, ApoliceFormDTO dadosFormulario) {

        Apolice apolice = buscarPorId(id);

        preencherDados(apolice, dadosFormulario);

        return apoliceRepository.save(apolice);
    }

    @Transactional
    public void excluir(Long id) {
        Apolice apolice = buscarPorId(id);
        apoliceRepository.delete(apolice);
    }

    private void preencherDados(
            Apolice apolice,
            ApoliceFormDTO dadosFormulario) {

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

        apolice.setLocal(buscarLocal(dadosFormulario.getLocalId()));
    }

    private Local buscarLocal(Long localId) {

        if (localId == null) {
            return null;
        }

        return localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));
    }
}