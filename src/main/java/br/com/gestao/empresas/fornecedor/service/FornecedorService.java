package br.com.gestao.empresas.fornecedor.service;

import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public Fornecedor buscarOuCriar(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do fornecedor não pode estar vazio.");
        }

        String nomeNormalizado = nome.trim();

        return fornecedorRepository.findByNomeIgnoreCase(nomeNormalizado)
                .orElseGet(() -> {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setNome(nomeNormalizado);
                    fornecedor.setAtivo(true);

                    return fornecedorRepository.save(fornecedor);
                });
    }
}