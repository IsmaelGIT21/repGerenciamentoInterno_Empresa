package br.com.gestao.empresas.repository;

import br.com.gestao.empresas.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByLocalId(Long localId);

}