package br.com.gestao.empresas.local.service;

import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import br.com.gestao.empresas.conta.repository.ContaRepository;

import java.util.List;

@Service
public class LocalService {

    private final LocalRepository localRepository;
    private final ContaRepository contaRepository;

    public LocalService(
            LocalRepository localRepository,
            ContaRepository contaRepository) {
        this.localRepository = localRepository;
        this.contaRepository = contaRepository;
    }

    public List<Local> listarTodos() {
        return localRepository.findAll();
    }

    public Local cadastrar(Local local) {
        return localRepository.save(local);
    }

    public Local buscarPorId(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));
    }

    public Local atualizar(Long id, Local dadosFormulario) {
        Local local = buscarPorId(id);

        local.setNome(dadosFormulario.getNome());
        local.setTipo(dadosFormulario.getTipo());
        local.setEndereco(dadosFormulario.getEndereco());

        return localRepository.save(local);
    }

    public boolean excluir(Long id) {
        Local local = buscarPorId(id);

        if (!contaRepository.findByLocalId(id).isEmpty()) {
            return false;
        }

        localRepository.delete(local);
        return true;
    }

}
