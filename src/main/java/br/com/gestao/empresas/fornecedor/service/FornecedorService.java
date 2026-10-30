package br.com.gestao.empresas.fornecedor.service;

import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.repository.FornecedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    @Transactional
    public Fornecedor cadastrar(Fornecedor fornecedor) {

        validarNome(fornecedor.getNome());

        String nome = fornecedor.getNome().trim();

        if (fornecedorRepository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException(
                    "Já existe um fornecedor cadastrado com esse nome.");
        }

        fornecedor.setNome(nome);
        fornecedor.setAtivo(true);

        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado."));
    }

    @Transactional
    public Fornecedor atualizar(Long id, Fornecedor dadosFormulario) {

        Fornecedor fornecedor = buscarPorId(id);

        validarNome(dadosFormulario.getNome());

        String nome = dadosFormulario.getNome().trim();

        if (fornecedorRepository.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new IllegalArgumentException(
                    "Já existe outro fornecedor com esse nome.");
        }

        fornecedor.setNome(nome);

        return fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public void alterarStatus(Long id) {

        Fornecedor fornecedor = buscarPorId(id);

        fornecedor.setAtivo(!fornecedor.isAtivo());

        fornecedorRepository.save(fornecedor);
    }

    @Transactional
    public Fornecedor buscarOuCriar(String nome) {

        validarNome(nome);

        String nomeNormalizado = nome.trim();

        return fornecedorRepository.findByNomeIgnoreCase(nomeNormalizado)
                .orElseGet(() -> {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setNome(nomeNormalizado);
                    fornecedor.setAtivo(true);

                    return fornecedorRepository.save(fornecedor);
                });
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome do fornecedor não pode estar vazio.");
        }

        if (nome.trim().length() > 150) {
            throw new IllegalArgumentException(
                    "O nome do fornecedor deve ter no máximo 150 caracteres.");
        }
    }
}