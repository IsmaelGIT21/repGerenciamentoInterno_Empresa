package br.com.gestao.empresas.local.repository;

import br.com.gestao.empresas.local.model.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalRepository extends JpaRepository<Local, Long> {

    Optional<Local> findByNomeIgnoreCase(String nome);
}
