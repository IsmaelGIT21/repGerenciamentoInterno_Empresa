package br.com.gestao.empresas.conta.repository;

import br.com.gestao.empresas.conta.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByLocalId(Long localId);

    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Conta c WHERE c.status = :status")
    BigDecimal somarValoresPorStatus(@Param("status") String status);

	long countByStatusAndDataVencimentoBefore(
        String status,
        LocalDate data);

@Query("""
        SELECT COALESCE(SUM(c.valor), 0)
        FROM Conta c
        WHERE c.status = :status
        AND c.dataVencimento BETWEEN :inicio AND :fim
        """)
BigDecimal somarPorStatusEPeriodoVencimento(
        @Param("status") String status,
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim);

@Query("""
        SELECT COALESCE(SUM(c.valor), 0)
        FROM Conta c
        WHERE c.status = :status
        AND c.dataPagamento BETWEEN :inicio AND :fim
        """)
BigDecimal somarPorStatusEPeriodoPagamento(
        @Param("status") String status,
        @Param("inicio") LocalDate inicio,
        @Param("fim") LocalDate fim);

List<Conta> findByStatusAndDataVencimentoBetweenOrderByDataVencimentoAsc(
        String status,
        LocalDate inicio,
        LocalDate fim);

}
