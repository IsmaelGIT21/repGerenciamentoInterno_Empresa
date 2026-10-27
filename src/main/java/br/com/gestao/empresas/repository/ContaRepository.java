package br.com.gestao.empresas.repository;

import br.com.gestao.empresas.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {
}
