package br.com.gestao.empresas.fornecedor.repository;

import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    Optional<Fornecedor> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
    
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}