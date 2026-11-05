package br.com.gestao.empresas.local.service;

import br.com.gestao.empresas.local.dto.LocalFormDTO;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.apolice.repository.ApoliceRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LocalService {

    private final LocalRepository localRepository;
    private final ContaRepository contaRepository;
    private final ApoliceRepository apoliceRepository;

    public LocalService(
            LocalRepository localRepository,
            ContaRepository contaRepository,
            ApoliceRepository apoliceRepository) {

        this.localRepository = localRepository;
        this.contaRepository = contaRepository;
        this.apoliceRepository = apoliceRepository;
    }

    public List<Local> listarTodos() {
        return localRepository.findAll();
    }

    public Local cadastrar(LocalFormDTO dadosFormulario) {

        Local local = new Local();

        local.setNome(dadosFormulario.getNome());
        local.setTipo(dadosFormulario.getTipo());
        local.setEndereco(dadosFormulario.getEndereco());
        local.setAtivo(
                dadosFormulario.getAtivo() != null
                        ? dadosFormulario.getAtivo()
                        : true);

        return localRepository.save(local);
    }

    public Local buscarPorId(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));
    }

    public Local atualizar(Long id, LocalFormDTO dadosFormulario) {

        Local local = buscarPorId(id);

        local.setNome(dadosFormulario.getNome());
        local.setTipo(dadosFormulario.getTipo());
        local.setEndereco(dadosFormulario.getEndereco());

        return localRepository.save(local);
    }

    public boolean excluir(Long id) {

        Local local = buscarPorId(id);

        boolean possuiContas = contaRepository.existsByLocalId(id);
        boolean possuiApolices = apoliceRepository.existsByLocalId(id);

        if (possuiContas || possuiApolices) {
            return false;
        }

        localRepository.delete(local);

        return true;
    }
}